package homeprime.manager.tasks.actions.operations;

import java.util.concurrent.Callable;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.manager.tasks.actions.ActionUtils;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

public class AgentTerminate implements Callable<Task> {

    private Thing thing;
    private Task task;

    public AgentTerminate(Thing thing, Task task) {
        task.setDescription("Task for Agent " + thing.getUuid() + " " + AgentTerminate.class.getSimpleName());
        this.thing = thing;
        this.task = task;
    }

    @Override
    public Task call() {
        task.setStatus(TaskStatusType.Running);
        // check that agent rest service is working
        if (AgentRestApiClient.isThingAlive(thing)) {
            // thing is alive, now enable maintenance mode on it
            AgentRestApiClient.setMaintenanceMode(thing, true);
            // perform agent application terminate request
            AgentRestApiClient.performAppTermination(thing);
            // ideally agent application is already down
            ActionUtils.waitAgentRestServiceIsDown(thing, 20);
            // at this moment service must be already down
            if (AgentRestApiClient.isThingAlive(thing)) {
                // if terminate request failed to be performed at this point we are sure it failed.
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Failed to terminate agent application");
            } else {
                task.setStatus(TaskStatusType.Completed);
                task.setMessage("Agent application terminated successfully");
            }
        } else {
            task.setStatus(TaskStatusType.Failed);
            task.setMessage("Cannot perform agent application terminate if thing is not alive");
        }
        return task;

    }

}
