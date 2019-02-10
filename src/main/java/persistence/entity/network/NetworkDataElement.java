package persistence.entity.network;

import application.entity.network.NetworkType;
import persistence.entity.base.DataElement;

public class NetworkDataElement extends DataElement {

    private NetworkType type;

    private int posX;
    private int posY;


    public NetworkType getType() {
        return type;
    }

    public void setType(NetworkType type) {
        this.type = type;
    }

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

}
