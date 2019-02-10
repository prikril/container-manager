package ui.node;

import application.GlobalInventory;
import application.task.TaskService;
import ui.DragIconType;

public class NetworkNode extends DraggableNode {

    public NetworkNode(String name) {
        super(name, DragIconType.switch_hub);
    }

    @Override
    public void removeNode() {
        GlobalInventory.getInstance().networks.remove(getName());
        TaskService.getInstance().removeNetworkTasksByName(getName());
        System.out.println("Removed network: \"" + getName() + "\"");
    }

}
