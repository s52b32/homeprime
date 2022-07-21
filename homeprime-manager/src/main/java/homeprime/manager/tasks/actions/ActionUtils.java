package homeprime.manager.tasks.actions;

import homeprime.agent.client.AgentRestApiClient;
import homeprime.agent.config.pojos.Thing;

public class ActionUtils {

    public static void waitAgentRestServiceIsDown(Thing thing, int tryoutCountMax) {
        int tryoutCountTmp = 0;
        while (AgentRestApiClient.isThingAlive(thing) && tryoutCountTmp < tryoutCountMax) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tryoutCountTmp++;
        }
    }

    public static void waitAgentRestServiceIsUp(Thing thing, int tryoutCountMax) {
        int tryoutCountTmp = 0;
        while (!AgentRestApiClient.isThingAlive(thing) && tryoutCountTmp < tryoutCountMax) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tryoutCountTmp++;
        }
    }

}
