package persistence.entity.container;

import persistence.entity.base.DataElement;

import java.util.HashMap;
import java.util.Map;

public class ContainerDataElement extends DataElement {

    private int posX;
    private int posY;

    private Map<String, String> networks = new HashMap<>();


    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Map<String, String> getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, String> networks) {
        this.networks = networks;
    }

}
