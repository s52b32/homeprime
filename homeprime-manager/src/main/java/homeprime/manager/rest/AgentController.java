package homeprime.manager.rest;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.agent.config.pojos.Things;
import homeprime.core.exception.ThingException;
import homeprime.core.logger.IoTLogger;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ThingUtils;
import homeprime.file.pojos.DownloadData;
import homeprime.file.pojos.UploadData;
import homeprime.image.pojos.Image;
import homeprime.manager.pojos.BackupFiles;
import homeprime.manager.pojos.Backups;
import homeprime.manager.pojos.enums.BackupType;
import homeprime.manager.tasks.TaskService;
import homeprime.manager.tasks.actions.operations.AgentConfigsBackup;
import homeprime.manager.tasks.actions.operations.AgentImageActivate;
import homeprime.manager.tasks.actions.operations.AgentImageDownload;
import homeprime.manager.tasks.actions.operations.AgentImageUpload;
import homeprime.manager.tasks.actions.operations.AgentServiceRestart;
import homeprime.manager.tasks.actions.operations.AgentSystemReboot;
import homeprime.manager.tasks.actions.operations.AgentTerminate;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.Tasks;

/**
 * Spring REST controller for HomePrime agent remote management.
 *
 * @author Milan Ramljak
 */
@RestController
public class AgentController {

    public static String managerRevision = "R8A01";

    @GetMapping("/")
    public String index() {
        return "HomePrime Manager " + managerRevision;

    }

    @GetMapping("/Backups")
    public ResponseEntity<?> showLocalBackups() {
        String fileExtension = ".zip";
        final Set<String> listDirectories = ThingUtils
                .listDirectories(ThingProperties.getInstance().getBackupsRootPath() + "things/");
        final Backups backups = new Backups();
        if (!listDirectories.isEmpty()) {
            for (String dirName : listDirectories) {
                final Set<String> listFiles = ThingUtils
                        .listFiles(ThingProperties.getInstance().getBackupsRootPath() + "things/" + dirName);
                final BackupFiles backup = new BackupFiles();
                backup.setThingUuid(dirName);
                if (!listFiles.isEmpty()) {
                    for (String fileName : listFiles) {
                        final homeprime.manager.pojos.BackupFile fileEntry = new homeprime.manager.pojos.BackupFile();
                        fileEntry.setName(fileName);
                        final String[] split = fileName.split("_");
                        fileEntry.setAppVersion(split[0]);
                        fileEntry.setTimestamp(Long.parseLong(split[2].replace(fileExtension, "")));
                        if (fileName.contains("configs")) {
                            fileEntry.setType(BackupType.Configs);
                        } else if (fileName.contains("image")) {
                            fileEntry.setType(BackupType.Image);
                        }
                        backup.getFiles().add(fileEntry);
                    }
                }
                backups.getBackups().add(backup);
            }
        }
        return new ResponseEntity<Backups>(backups, HttpStatus.OK);

    }

    @GetMapping("/Things")
    public ResponseEntity<?> listManagedThings() {
        Things thingConfigurations = new Things();
        try {
            thingConfigurations = ConfigurationReader.getThingConfigurations();
            thingConfigurations = checkIsActive(thingConfigurations);
        } catch (ThingException e) {
            e.printStackTrace();
            new ResponseEntity<String>("Exception happened " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Things>(thingConfigurations, HttpStatus.OK);
    }

    @PostMapping("/Things/manage")
    public ResponseEntity<?> manageThing(@RequestPart(required = true) Thing thing) {
        try {
            Things thingConfigurations = ConfigurationReader.getThingConfigurations();
            if (thingConfigurations.getThings().isEmpty()) {
                thingConfigurations.getThings().add(thing);
            } else {
                if (!exists(thingConfigurations.getThings(), thing)) {
                    // if thing with UUID doesn't already exist, add it
                    thingConfigurations.getThings().add(thing);
                    // force file overwrite
                    if (!writeToFile(thingConfigurations, true)) {
                        // this means that thing with provided UUID exist
                        return new ResponseEntity<String>("Failed to write new configuration to things.json",
                                HttpStatus.CONFLICT);
                    }
                } else {
                    // this means that thing with provided UUID exist
                    return new ResponseEntity<String>("Thing " + thing.getUuid() + " cannot be added, already exists",
                            HttpStatus.CONFLICT);
                }
            }
        } catch (ThingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add new thing!");
        }
        return new ResponseEntity<String>("Thing " + thing.getUuid() + " successfully added", HttpStatus.ACCEPTED);
    }

    @GetMapping("/Things/{uuid}")
    public ResponseEntity<?> getThing(@PathVariable(value = "uuid", required = true) String uuid)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations.getThings().isEmpty()) {
            // this means that no things exist
            return new ResponseEntity<String>("List of things is empty, not possible to query remote agent",
                    HttpStatus.NO_CONTENT);
        } else {
            // check thing with uuid exists
            final Thing thing = getThingById(thingConfigurations.getThings(), uuid);
            if (thing != null) {
                final Thing thingResponse = AgentRestApiClient.getThingConfiguration(thing);
                if (thingResponse != null && thingResponse.getUuid().equals(uuid)) {
                    return new ResponseEntity<Thing>(thingResponse, HttpStatus.OK);
                } else if (thingResponse != null && !thingResponse.getUuid().equals(uuid)) {
                    // this means that thing did respond but UUID does not match
                    return new ResponseEntity<String>("Thing " + uuid + " does not match one in configuration",
                            HttpStatus.CONFLICT);
                } else {
                    // this means that thing didn't respond
                    return new ResponseEntity<String>("Thing " + uuid + " failed to respond", HttpStatus.BAD_REQUEST);
                }
            } else {
                // this means that thing with provided UUID doesn't exist
                return new ResponseEntity<String>("Thing " + uuid + " cannot be queried, does not exist",
                        HttpStatus.NOT_FOUND);
            }
        }

    }

    @GetMapping("/Things/{uuid}/unmanage")
    public ResponseEntity<?> unmanageThing(@PathVariable(value = "uuid", required = true) String uuid) {
        try {
            Things thingConfigurations = ConfigurationReader.getThingConfigurations();
            if (thingConfigurations.getThings().isEmpty()) {
                // this means that no things exist
                return new ResponseEntity<String>("List of things is empty, not possible to proceed with unmanage",
                        HttpStatus.NO_CONTENT);
            } else {
                if (exists(thingConfigurations.getThings(), uuid)) {
                    // if thing with UUID exist, unmanage it
                    thingConfigurations.getThings().remove(getThingById(thingConfigurations.getThings(), uuid));
                    // force file overwrite
                    if (!writeToFile(thingConfigurations, true)) {
                        return new ResponseEntity<String>("Failed to write new configuration to things.json",
                                HttpStatus.NOT_FOUND);
                    }
                } else {
                    // this means that thing with provided UUID doesn't exist
                    return new ResponseEntity<String>("Thing " + uuid + " cannot be unmanaged, does not exist",
                            HttpStatus.CONFLICT);
                }
            }
        } catch (ThingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect new thing!");
        }
        return new ResponseEntity<String>("Thing " + uuid + " successfully unmanaged", HttpStatus.ACCEPTED);
    }

    /**
     * Restart homeprime service.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @RequestMapping("/Things/{uuid}/restart")
    public ResponseEntity<?> restartAgentService(@PathVariable(value = "uuid", required = true) String uuid)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        // check thing with uuid exists
        Thing thing = getThingById(thingConfigurations.getThings(), uuid);
        if (thing != null) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            Task asrTask = new Task(thing.getUuid());
            // add this task to task service
            TaskService.getInstance().addTask(asrTask);
            AgentServiceRestart asr = new AgentServiceRestart(thing, asrTask);
            executor.schedule(asr, 5, TimeUnit.SECONDS);
            return new ResponseEntity<Task>(asrTask, HttpStatus.ACCEPTED);
        } else {
            // this means that thing with provided UUID doesn't exist
            return new ResponseEntity<String>(
                    "Agent service restart cannot be performed. Thing " + uuid + " does not exist",
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Restart homeprime service on all things.
     *
     * @return 202 ACCEPTED
     * @throws ThingException
     */
    @RequestMapping("/Things/restart")
    public ResponseEntity<?> restartAllAgentServices() throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations.getThings().isEmpty()) {
            // this means that no things exist
            return new ResponseEntity<String>(
                    "List of things is empty, not possible to proceed with agent service restart",
                    HttpStatus.NO_CONTENT);
        } else {
            final Tasks restartTasks = new Tasks();
            for (Thing thing : thingConfigurations.getThings()) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
                Task asrTask = new Task(thing.getUuid());
                // add this task to task service
                TaskService.getInstance().addTask(asrTask);
                AgentServiceRestart asr = new AgentServiceRestart(thing, asrTask);
                // initiate task execution
                executor.submit(asr);
                // add this task to task list response holder
                restartTasks.getTasks().add(asrTask);
            }
            return new ResponseEntity<Tasks>(restartTasks, HttpStatus.ACCEPTED);
        }
    }

    /**
     * Reboot agent system.
     *
     * @return 202 ACCEPTED
     * @throws ThingException
     */
    @RequestMapping("/Things/{uuid}/reboot")
    public ResponseEntity<?> rebootAgent(@PathVariable(value = "uuid", required = true) String uuid)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        // check thing with uuid exists
        Thing thing = getThingById(thingConfigurations.getThings(), uuid);
        if (thing != null) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            Task asrTask = new Task(thing.getUuid());
            // add this task to task service
            TaskService.getInstance().addTask(asrTask);
            AgentServiceRestart asr = new AgentServiceRestart(thing, asrTask);
            executor.schedule(asr, 5, TimeUnit.SECONDS);
            return new ResponseEntity<Task>(asrTask, HttpStatus.ACCEPTED);
        } else {
            // this means that thing with provided UUID doesn't exist
            return new ResponseEntity<String>(
                    "Agent system reboot cannot be performed. Thing " + uuid + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Reboot system on all things.
     *
     * @return 202 ACCEPTED
     * @throws ThingException
     */
    @RequestMapping("/Things/reboot")
    public ResponseEntity<?> rebootAllAgents() throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations.getThings().isEmpty()) {
            // this means that no things exist
            return new ResponseEntity<String>(
                    "List of things is empty, not possible to proceed with agent system reboot", HttpStatus.NO_CONTENT);
        } else {
            final Tasks rebootTasks = new Tasks();
            for (Thing thing : thingConfigurations.getThings()) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
                Task asrTask = new Task(thing.getUuid());
                // add this task to task service
                TaskService.getInstance().addTask(asrTask);
                AgentSystemReboot asr = new AgentSystemReboot(thing, asrTask);
                // initiate task execution
                executor.submit(asr);
                // add this task to task list response holder
                rebootTasks.getTasks().add(asrTask);
            }
            return new ResponseEntity<Tasks>(rebootTasks, HttpStatus.ACCEPTED);
        }
    }

    /**
     * Terminate agent application of certain thing.
     *
     * @return 202 ACCEPTED
     * @throws ThingException
     */
    @RequestMapping("/Things/{uuid}/terminate")
    public ResponseEntity<?> terminateAgentApp(@PathVariable(value = "uuid", required = true) String uuid)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        // check thing with uuid exists
        Thing thing = getThingById(thingConfigurations.getThings(), uuid);
        if (thing != null) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
            Task asrTask = new Task(thing.getUuid());
            // add this task to task service
            TaskService.getInstance().addTask(asrTask);
            AgentTerminate asr = new AgentTerminate(thing, asrTask);
            // initiate task execution in 5 seconds from now
            executor.schedule(asr, 5, TimeUnit.SECONDS);
            return new ResponseEntity<Task>(asrTask, HttpStatus.ACCEPTED);
        } else {
            // this means that thing with provided UUID doesn't exist
            return new ResponseEntity<String>(
                    "Agent application terminate cannot be performed. Thing " + uuid + " does not exist",
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Terminate agent application on all things.
     *
     * @return 202 ACCEPTED
     * @throws ThingException
     */
    @RequestMapping("/Things/terminate")
    public ResponseEntity<?> terminateAllAgentsApp() throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations.getThings().isEmpty()) {
            // this means that no things exist
            return new ResponseEntity<String>("List of things is empty, not possible to proceed with termination",
                    HttpStatus.NO_CONTENT);
        } else {
            final Tasks terminateTasks = new Tasks();
            for (Thing thing : thingConfigurations.getThings()) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
                Task atTask = new Task(thing.getUuid());
                // add this task to task service
                TaskService.getInstance().addTask(atTask);
                AgentTerminate at = new AgentTerminate(thing, atTask);
                // initiate task execution
                executor.submit(at);
                // add this task to task list response holder
                terminateTasks.getTasks().add(atTask);
            }
            return new ResponseEntity<Tasks>(terminateTasks, HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/Things/{uuid}/Images/distribute")
    public ResponseEntity<?> distributeApplicationImage(@PathVariable(value = "uuid", required = true) String uuid,
            @RequestParam("image") String imageFilePath, @RequestPart(required = false) UploadData uploadData)
            throws ThingException {
        final File file = new File(imageFilePath);
        if (!file.exists() || !file.isFile()) {
            // this means that image on provided path does not exist
            return new ResponseEntity<String>("Image on provided path doesn't exist", HttpStatus.NO_CONTENT);
        }
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        Thing thing = getThingById(thingConfigurations.getThings(), uuid);
        if (thing == null) {
            // this means that thing with provided UUID doesn't exist
            return new ResponseEntity<String>("Cannot distribute image to things if no thing with provided id exist",
                    HttpStatus.NOT_FOUND);
        }
        final Tasks distributeTasks = new Tasks();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Task uploadTask = new Task(thing.getUuid());
        // add this task to task service
        TaskService.getInstance().addTask(uploadTask);
        // initiate task execution
        executor.submit(new AgentImageUpload(thing, uploadTask, file, uploadData));
        // add this task to task list response holder
        distributeTasks.getTasks().add(uploadTask);
        return new ResponseEntity<Tasks>(distributeTasks, HttpStatus.ACCEPTED);
    }

    @PostMapping("/Things/Images/distribute")
    public ResponseEntity<?> distributeApplicationImage(@RequestParam("image") String imageFilePath,
            @RequestPart(required = false) UploadData uploadData) throws ThingException {
        final File file = new File(imageFilePath);
        if (!file.exists() || !file.isFile()) {
            // this means that image on provided path does not exist
            return new ResponseEntity<String>("Image on provided path doesn't exist", HttpStatus.NO_CONTENT);
        }
        UploadData uploadDataTmp = uploadData;
        if (uploadData == null) {
            uploadDataTmp = new UploadData();
            uploadDataTmp.setDestinationDir(ThingProperties.APP_ROOT_PATH_MANAGER);
        }
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations != null && !thingConfigurations.getThings().isEmpty()) {
            final Tasks distributeTasks = new Tasks();
            for (Thing thing : thingConfigurations.getThings()) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
                Task uploadTask = new Task(thing.getUuid());
                // add this task to task service
                TaskService.getInstance().addTask(uploadTask);
                // initiate task execution
                executor.submit(new AgentImageUpload(thing, uploadTask, file, uploadDataTmp));
                // add this task to task list response holder
                distributeTasks.getTasks().add(uploadTask);
            }
            return new ResponseEntity<Tasks>(distributeTasks, HttpStatus.ACCEPTED);
        }
        // this means that thing with provided UUID doesn't exist
        return new ResponseEntity<String>("Cannot distribute image to things if no things exist",
                HttpStatus.NO_CONTENT);
    }

    @PostMapping("/Things/{uuid}/Configs/backup")
    public ResponseEntity<?> downloadApplicationBackup(@PathVariable(value = "uuid", required = true) String uuid,
            @RequestPart(required = false) DownloadData downloadData) throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        Thing thing = getThingById(thingConfigurations.getThings(), uuid);
        if (thing == null) {
            // this means that thing with provided UUID doesn't exist
            return new ResponseEntity<String>(
                    "Cannot create configuration backup of thing if no thing with provided id exist",
                    HttpStatus.NOT_FOUND);
        }
        final Tasks backupDownloadTasks = new Tasks();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Task backupDownloadTask = new Task(thing.getUuid());
        // add this task to task service
        TaskService.getInstance().addTask(backupDownloadTask);
        // initiate task execution
        executor.submit(new AgentConfigsBackup(thing, backupDownloadTask, downloadData));
        // add this task to task list response holder
        backupDownloadTasks.getTasks().add(backupDownloadTask);
        return new ResponseEntity<Tasks>(backupDownloadTasks, HttpStatus.ACCEPTED);

    }

    @PostMapping("/Things/Configs/backup")
    public ResponseEntity<?> downloadApplicationBackup(@RequestPart(required = false) DownloadData downloadData)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations != null && !thingConfigurations.getThings().isEmpty()) {
            final Tasks backupDownloadTasks = new Tasks();
            for (Thing thing : thingConfigurations.getThings()) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
                Task backupDownloadTask = new Task(thing.getUuid());
                // add this task to task service
                TaskService.getInstance().addTask(backupDownloadTask);
                // initiate task execution
                executor.submit(new AgentConfigsBackup(thing, backupDownloadTask, downloadData));
                // add this task to task list response holder
                backupDownloadTasks.getTasks().add(backupDownloadTask);
            }
            return new ResponseEntity<Tasks>(backupDownloadTasks, HttpStatus.ACCEPTED);
        }
        // this means that thing with provided UUID doesn't exist
        return new ResponseEntity<String>("Cannot download backup of thing configs if no things exist",
                HttpStatus.NO_CONTENT);
    }

    @PostMapping("/Things/{uuid}/Images/download")
    public ResponseEntity<?> downloadActiveImage(@PathVariable(value = "uuid", required = true) String uuid,
            @RequestPart(required = false) DownloadData downloadData) throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        Thing thing = getThingById(thingConfigurations.getThings(), uuid);
        if (thing == null) {
            // this means that thing with provided UUID doesn't exist
            return new ResponseEntity<String>(
                    "Cannot download active image of thing if no thing with provided id exist", HttpStatus.NOT_FOUND);
        }
        final Tasks imageDownloadTasks = new Tasks();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Task imageDownloadTask = new Task(thing.getUuid());
        // add this task to task service
        TaskService.getInstance().addTask(imageDownloadTask);
        // initiate task execution
        executor.submit(new AgentImageDownload(thing, imageDownloadTask, downloadData));
        // add this task to task list response holder
        imageDownloadTasks.getTasks().add(imageDownloadTask);
        return new ResponseEntity<Tasks>(imageDownloadTasks, HttpStatus.ACCEPTED);

    }

    @PostMapping("/Things/Images/download")
    public ResponseEntity<?> downloadActiveImage(@RequestPart(required = false) DownloadData downloadData)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations != null && !thingConfigurations.getThings().isEmpty()) {
            final Tasks imageDownloadTasks = new Tasks();
            for (Thing thing : thingConfigurations.getThings()) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
                Task imageDownloadTask = new Task(thing.getUuid());
                // add this task to task service
                TaskService.getInstance().addTask(imageDownloadTask);
                // initiate task execution
                executor.submit(new AgentImageDownload(thing, imageDownloadTask, downloadData));
                // add this task to task list response holder
                imageDownloadTasks.getTasks().add(imageDownloadTask);
            }
            return new ResponseEntity<Tasks>(imageDownloadTasks, HttpStatus.ACCEPTED);
        }
        // this means that thing with provided UUID doesn't exist
        return new ResponseEntity<String>("Cannot download active images of thing configs if no things exist",
                HttpStatus.NO_CONTENT);
    }

    @PostMapping("/Things/{uuid}/Images/set-active")
    public ResponseEntity<?> activateApplicationImage(@PathVariable(value = "uuid", required = true) String uuid,
            @RequestBody(required = true) Image image) throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        Thing thing = getThingById(thingConfigurations.getThings(), uuid);
        if (thing == null) {
            // this means that thing with provided UUID doesn't exist
            return new ResponseEntity<String>("Cannot activate image on thing if no thing with provided id exist",
                    HttpStatus.NOT_FOUND);
        }
        final Tasks distributeTasks = new Tasks();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        Task uploadTask = new Task(thing.getUuid());
        // add this task to task service
        TaskService.getInstance().addTask(uploadTask);
        // initiate task execution
        executor.submit(new AgentImageActivate(thing, uploadTask, image));
        // add this task to task list response holder
        distributeTasks.getTasks().add(uploadTask);
        return new ResponseEntity<Tasks>(distributeTasks, HttpStatus.ACCEPTED);
    }

    @PostMapping("/Things/Images/set-active")
    public ResponseEntity<?> activateApplicationImages(@RequestBody(required = true) Image image)
            throws ThingException {
        Things thingConfigurations = ConfigurationReader.getThingConfigurations();
        if (thingConfigurations != null && !thingConfigurations.getThings().isEmpty()) {
            final Tasks distributeTasks = new Tasks();
            for (Thing thing : thingConfigurations.getThings()) {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
                Task uploadTask = new Task(thing.getUuid());
                // add this task to task service
                TaskService.getInstance().addTask(uploadTask);
                // initiate task execution
                executor.submit(new AgentImageActivate(thing, uploadTask, image));
                // add this task to task list response holder
                distributeTasks.getTasks().add(uploadTask);
            }
            return new ResponseEntity<Tasks>(distributeTasks, HttpStatus.ACCEPTED);
        }
        // this means that thing with provided UUID doesn't exist
        return new ResponseEntity<String>("Cannot activate image on things if no things exist", HttpStatus.NO_CONTENT);
    }

    private Things checkIsActive(Things things) {
        for (Thing thing : things.getThings()) {
            // check if agent REST is reachable
            if (AgentRestApiClient.isThingAlive(thing)) {
                thing.setActive(true);
                // check if agent has maintenance mode activated
                if (AgentRestApiClient.readMaintenanceState(thing)) {
                    thing.setMaintenance(true);
                } else {
                    thing.setMaintenance(false);
                }
            } else {
                thing.setActive(false);
            }
        }
        return things;
    }

    private boolean exists(List<Thing> things, Thing thing) {
        return exists(things, thing.getUuid());
    }

    private boolean exists(List<Thing> things, String thingUuid) {
        boolean exists = false;
        if (things != null && !things.isEmpty()) {
            for (Thing thingItem : things) {
                // make sure things are unique by UUID
                if (thingItem.getUuid().equals(thingUuid)) {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    private Thing getThingById(List<Thing> things, String thingUuid) {
        if (thingUuid != null) {
            for (Thing thingItem : things) {
                // make sure things are unique by UUID
                if (thingItem.getUuid().equals(thingUuid)) {
                    return thingItem;
                }
            }
        }
        return null;
    }

    private boolean writeToFile(Things thingConfigurations, boolean force) {
        // save the file if it doesn't exist or force set to true
        if (!ThingUtils.fileExists(
                ThingProperties.getInstance().getConfigsRootPath() + ConfigurationReader.THINGS_CONFIGURATION_FILE_NAME)
                || force) {
            try {
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(new File(ThingProperties.getInstance().getConfigsRootPath()
                        + ConfigurationReader.THINGS_CONFIGURATION_FILE_NAME), thingConfigurations);
            } catch (Exception e) {
                IoTLogger.getInstance().error("ThingConfiguration.buildDefaultConfiguration() Failed to create "
                        + ConfigurationReader.THINGS_CONFIGURATION_FILE_NAME);
                return false;
            }
        }
        return true;
    }

}
