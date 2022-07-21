package homeprime.manager.tasks.actions.operations;

import java.util.concurrent.Callable;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.core.properties.ThingProperties;
import homeprime.file.pojos.DownloadData;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

public class AgentConfigsBackup implements Callable<Task> {

    private Thing thing;
    private Task task;
    private DownloadData downloadData;

    public AgentConfigsBackup(Thing thing, Task task, DownloadData downloadData) {
        task.setDescription(AgentConfigsBackup.class.getSimpleName() + " task for agent " + thing.getUuid() + " "
                + thing.getName());
        this.thing = thing;
        this.task = task;
        this.downloadData = downloadData;
    }

    @Override
    public Task call() {
        task.setStatus(TaskStatusType.Running);
        // make sure that DownloadData is valid.
        checkPrepareDownloadData();
        // check that agent rest service is working
        if (AgentRestApiClient.isThingAlive(thing)) {
            final Thing thingConfig = AgentRestApiClient.getThingConfiguration(thing);
            if (thingConfig == null) {
                // if no active image is found
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Failed to get active image version from thing");
            } else {
                downloadData
                        .setDestinationFileName(thingConfig.getVersion() + "_" + downloadData.getDestinationFileName());
                // thing is alive, now initiate upload
                if (!AgentRestApiClient.download(thing, downloadData)) {
                    // if terminate request failed to be performed at this point we are sure it failed.
                    task.setStatus(TaskStatusType.Failed);
                    task.setMessage("Failed to download backup to: " + downloadData.getAbsoluteFilePath());
                } else {
                    task.setStatus(TaskStatusType.Completed);
                    task.setMessage("Backup succesffully downloaded to: " + downloadData.getAbsoluteFilePath());
                }
            }
        } else {
            task.setStatus(TaskStatusType.Failed);
            task.setMessage("Cannot perform agent configs backup download if thing is not alive");
        }
        return task;
    }

    /**
     * Helper method to build valid download data settings from user provided input. If user didn't provide DownloadData
     * in request, create it it with default values.
     *
     * Default download destination paths is: {@code ThingProperties.BACKUPS_ROOT_PATH + "things/" + thing.getUuid()}
     */
    private void checkPrepareDownloadData() {
        if (downloadData == null) {
            // if downloadData is not provided in request. Make default one.
            downloadData = new DownloadData();
            downloadData.setDestinationPath(
                    ThingProperties.getInstance().getBackupsRootPath() + "things/" + thing.getUuid());
            downloadData.setDestinationFileName("configs_" + System.currentTimeMillis() + ".zip");
        } else {
            downloadData.setDestinationPath(downloadData.getDestinationFileName() + "things/" + thing.getUuid());
        }
    }

}
