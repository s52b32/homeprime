package homeprime.manager.tasks.actions.operations;

import java.util.concurrent.Callable;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;
import homeprime.manager.tasks.actions.ActionUtils;
import homeprime.manager.tasks.pojos.Task;
import homeprime.manager.tasks.pojos.enums.TaskStatusType;

public class AgentSystemReboot implements Callable<Task> {

    private Thing thing;
    private Task task;

    public AgentSystemReboot(Thing thing, Task task) {
        task.setDescription("Task for Agent " + thing.getUuid() + " " + AgentSystemReboot.class.getSimpleName());
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
            AgentRestApiClient.performSystemReboot(thing);
            ActionUtils.waitAgentRestServiceIsDown(thing, 20);
            // at this moment service must be already down
            if (AgentRestApiClient.isThingAlive(thing)) {
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("System reboot failed to start");
            }
            // system is pre-configured to auto start on reboot
            ActionUtils.waitAgentRestServiceIsUp(thing, 60);
            // at this moment service must be already up
            if (!AgentRestApiClient.isThingAlive(thing)) {
                task.setStatus(TaskStatusType.Failed);
                task.setMessage("Agent failed to auto start after system reboot");
            } else {
                // thing is alive again, now disable maintenance mode on it
                AgentRestApiClient.setMaintenanceMode(thing, false);
                task.setStatus(TaskStatusType.Completed);
                task.setMessage("Agent system rebooted successfully");
            }
        } else {
            task.setStatus(TaskStatusType.Failed);
            task.setMessage("Cannot perform agent system reboot if thing is not alive");
        }
        return task;
    }

}
