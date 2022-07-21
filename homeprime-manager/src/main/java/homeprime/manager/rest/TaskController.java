package homeprime.manager.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import homeprime.manager.tasks.TaskService;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.Tasks;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

/**
 * Spring REST controller for HomePrime agent remote management.
 *
 * @author Milan Ramljak
 */
@RestController
public class TaskController {

    @GetMapping("/Tasks")
    public ResponseEntity<?> listTasks(@RequestParam(required = false) TaskStatusType taskStatusType) {
        if (taskStatusType == null) {
            return new ResponseEntity<Tasks>(TaskService.getInstance().getTasks(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Tasks>(TaskService.getInstance().getTasksByStatus(taskStatusType), HttpStatus.OK);
        }
    }

    @GetMapping("/Tasks/delete")
    public ResponseEntity<?> deleteAllTasks() {
        TaskService.getInstance().deleteAllTasks();
        return new ResponseEntity<String>("All tasks were deleted", HttpStatus.OK);
    }

    @GetMapping("/Tasks/{uuid}")
    public ResponseEntity<?> getTask(@PathVariable(value = "uuid", required = true) String uuid) {
        Task taskByUuid = TaskService.getInstance().getTaskByUuid(uuid);
        if (taskByUuid != null) {
            return new ResponseEntity<Task>(taskByUuid, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Task with id " + uuid + " doesn't exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/Tasks/{uuid}/delete")
    public ResponseEntity<?> deleteTask(@PathVariable(value = "uuid", required = true) String uuid) {
        Task taskByUuid = TaskService.getInstance().getTaskByUuid(uuid);
        if (taskByUuid != null) {
            TaskService.getInstance().deleteTask(taskByUuid);
            return new ResponseEntity<String>("Task with id " + uuid + " removed from list", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Task with id " + uuid + " doesn't exist, cannot be removed",
                HttpStatus.NOT_FOUND);
    }

}
