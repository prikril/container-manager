package api.container.element;

public class ContainerActionElement {
    /*
    {
        "action": "stop",       # State change action (stop, start, restart, freeze or unfreeze)
        "timeout": 30,          # A timeout after which the state change is considered as failed
        "force": true,          # Force the state change (currently only valid for stop and restart where it means killing the container)
        "stateful": true        # Whether to store or restore runtime state before stopping or starting (only valid for stop and start, defaults to false)
    }
     */

    private String action;
    private int timeout;
    private boolean force;
    private boolean stateful = false;


    public ContainerActionElement(String action, int timeout, boolean force, boolean stateful) {
        this.action = action;
        this.timeout = timeout;
        this.force = force;
        this.stateful = stateful;
    }

}
