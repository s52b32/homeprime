package homeprime.manager.tasks.actions.operations;

import java.util.concurrent.Callable;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.manager.tasks.actions.ActionUtils;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

public class AgentServiceRestart implements Callable<Task> {

    /**
     * Max time to restart homeprime service and have it running again is 60 seconds.
     */
    private final int SERVICE_RESTART_MAX_TIME = 60;
    private Thing thing;
    private Task task;

    public AgentServiceRestart(Thing thing, Task task) {
        task.setDescription("Task for Agent " + thing.getUuid() + " " + AgentServiceRestart.class.getSimpleName());
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
            // this initiates restart
            AgentRestApiClient.performServiceRestart(thing);
            // we need to be sure, agent service actually stopped
            if (AgentRestApiClient.isThingAlive(thing)) {
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Agent service still running after successful service restart was intiated");
                return task;
            }
            // at this moment service must be already down
            ActionUtils.waitAgentRestServiceIsUp(thing, SERVICE_RESTART_MAX_TIME);
            // at this moment service must be already up
            if (!AgentRestApiClient.isThingAlive(thing)) {
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Agent service didn't start in expected time");
            } else {
                // thing is alive again, now disable maintenance mode on it
                AgentRestApiClient.setMaintenanceMode(thing, false);
                task.setStatus(TaskStatusType.Completed);
                task.setMessage("Agent service restarted successfully");
            }
        } else {
            task.setStatus(TaskStatusType.Failed);
            task.setMessage("Cannot perform agent service restart if thing is not alive");
        }
        return task;
    }

}
