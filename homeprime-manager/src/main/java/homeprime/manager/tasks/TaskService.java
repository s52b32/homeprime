package homeprime.manager.tasks;

import java.util.ArrayList;

import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.Tasks;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

public class TaskService {

    private static TaskService instance;
    private static Tasks tasks = new Tasks();

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public Tasks getTasksByStatus(TaskStatusType taskStatusType) {
        final Tasks tasksTmp = new Tasks();
        if (tasks.getTasks() != null) {
            for (Task task : tasks.getTasks()) {
                if (task.getStatus() == taskStatusType) {
                    tasksTmp.getTasks().add(task);
                }
            }
        }
        return tasksTmp;
    }

    public Task getTaskByUuid(String taskUuid) {
        Task match = null;
        if (tasks.getTasks() != null) {
            for (Task task : tasks.getTasks()) {
                if (task.getUuid().equals(taskUuid)) {
                    match = task;
                    break;
                }
            }
        }
        return match;
    }

    public void addTask(Task task) {
        // if null, create new
        if (tasks.getTasks() == null) {
            tasks.setTasks(new ArrayList<Task>());
        }
        tasks.getTasks().add(task);
    }

    public void deleteTask(Task task) {
        // if null, create new
        if (tasks.getTasks() == null) {
            tasks.setTasks(new ArrayList<Task>());
        }
        tasks.getTasks().remove(task);
    }

    public void deleteAllTasks() {
        tasks = null;
    }

}
