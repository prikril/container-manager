package application.entity.network;

public enum NetworkType {

    UNKNOWN(-1), BRIDGE(1000);

    private int type;

    NetworkType(int type) {
        this.type = type;
    }

    public int getTypeCode() {
        return type;
    }

}
