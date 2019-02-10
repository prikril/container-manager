package ui.node;

import application.GlobalInventory;
import application.entity.container.ContainerEntity;
import application.task.TaskService;
import ui.DragIconType;

public class ContainerNode extends DraggableNode {

    public ContainerNode(String name) {
        super(name, DragIconType.server);
    }

    @Override
    public void registerLink(String linkId, DraggableNode other, String adapterName) {
        super.registerLink(linkId, other, adapterName);

        if (other instanceof NetworkNode) {
            ContainerEntity containerEntity = GlobalInventory.getInstance().containers.get(this.getName());
            containerEntity.getNetworks().put(adapterName, other.getName());
        }
    }

    @Override
    public void removeNode() {
        super.removeNode();

        GlobalInventory.getInstance().containers.remove(getName());
        TaskService.getInstance().removeContainerTasksByName(getName());
        System.out.println("Removed container: \"" + getName() + "\"");
    }

    @Override
    public void removeLink(String id, DraggableNode other) {
        super.removeLink(id, other);

        if (other instanceof NetworkNode) {
            ContainerEntity containerEntity = GlobalInventory.getInstance().containers.get(this.getName());
            containerEntity.getNetworks().values().remove(other.getName()); // assumes that network is present only once
        }
    }

}
