package api.base;

public class DeviceElement {

    /*
    contents of:

        "eth0": {
            "nictype": "bridged",
            "parent": "net123",
            "type": "nic"
            }
     */

    private String nictype;
    private String parent;
    private String type;

    public DeviceElement(String nictype, String parent, String type) {
        this.nictype = nictype;
        this.parent = parent;
        this.type = type;
    }

    public String getNictype() {
        return nictype;
    }

    public String getParent() {
        return parent;
    }

    public String getType() {
        return type;
    }

}
