package homeprime.manager.tasks.actions.operations;

import java.io.File;
import java.util.concurrent.Callable;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.file.pojos.UploadData;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

public class AgentImageUpload implements Callable<Task> {

    private Thing thing;
    private Task task;
    private File image;
    private UploadData uploadData;

    public AgentImageUpload(Thing thing, Task task, File image, UploadData uploadData) {
        task.setDescription("Task for Agent " + thing.getUuid() + " " + AgentImageUpload.class.getSimpleName());
        this.thing = thing;
        this.task = task;
        this.image = image;
        this.uploadData = uploadData;
    }

    @Override
    public Task call() {
        task.setStatus(TaskStatusType.Running);
        // check that agent rest service is working
        if (AgentRestApiClient.isThingAlive(thing)) {
            // thing is alive, now initiate upload
            if (!AgentRestApiClient.upload(thing, image, uploadData)) {
                // if terminate request failed to be performed at this point we are sure it failed.
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Failed to upload image to " + uploadData.getDestinationDir());
            } else {
                task.setStatus(TaskStatusType.Completed);
                task.setMessage("Image succesffully uploaded to " + uploadData.getDestinationDir());
            }
        } else {
            task.setStatus(TaskStatusType.Failed);
            task.setMessage("Cannot perform agent image upload if thing is not alive");
        }
        return task;

    }

}
