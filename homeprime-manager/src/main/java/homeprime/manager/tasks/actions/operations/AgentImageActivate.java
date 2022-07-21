package homeprime.manager.tasks.actions.operations;

import java.util.concurrent.Callable;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.image.pojos.Image;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

public class AgentImageActivate implements Callable<Task> {

    private Thing thing;
    private Task task;
    private Image image;

    public AgentImageActivate(Thing thing, Task task, Image image) {
        task.setDescription("Task for Agent " + thing.getUuid() + " " + AgentImageActivate.class.getSimpleName());
        this.thing = thing;
        this.task = task;
        this.image = image;
    }

    @Override
    public Task call() {
        task.setStatus(TaskStatusType.Running);
        // check that agent rest service is working
        if (AgentRestApiClient.isThingAlive(thing)) {
            final Boolean setActiveResponse = AgentRestApiClient.setActiveImage(thing, image);
            if (setActiveResponse == null) {
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Could not set active image as image does not exist");
            } else if (!setActiveResponse) {
                // if terminate request failed to be performed at this point we are sure it failed.
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Failed to set active image");
            } else {
                task.setStatus(TaskStatusType.Completed);
                task.setMessage("Agent active image set successfully");
            }
        } else {
            task.setStatus(TaskStatusType.Failed);
            task.setMessage("Cannot perform agent set active image if thing is not alive");
        }
        return task;

    }

}
