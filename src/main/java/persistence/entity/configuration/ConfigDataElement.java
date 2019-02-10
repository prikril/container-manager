package persistence.entity.configuration;

public class ConfigDataElement {

    private String certPath;
    private String keyfilePath;
    private String lxdImageAlias;
    private String serverUrl;


    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getKeyfilePath() {
        return keyfilePath;
    }

    public void setKeyfilePath(String keyfilePath) {
        this.keyfilePath = keyfilePath;
    }

    public String getLxdImageAlias() {
        return lxdImageAlias;
    }

    public void setLxdImageAlias(String lxdImageAlias) {
        this.lxdImageAlias = lxdImageAlias;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

}
