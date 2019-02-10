package application;

import application.logging.LoggingFacade;
import com.google.gson.Gson;
import persistence.entity.configuration.ConfigDataElement;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GlobalConfiguration {

    private LoggingFacade logger = LoggingFacade.getInstance();

    private static GlobalConfiguration globalConfiguration = new GlobalConfiguration();

    public static GlobalConfiguration getInstance() {
        return globalConfiguration;
    }

    private GlobalConfiguration() {
    }


    private String terminalCommand; // TODO
    private String certPath  = "lxd-client.crt";
    private String keyfilePath = "lxd-client.key";
    private String lxdImageAlias = "ubuntu14";
    private String serverUrl = "https://localhost:8443";


    public String getCertPath() {
        return certPath;
    }

    public String getKeyfilePath() {
        return keyfilePath;
    }

    public String getLxdImageAlias() {
        return lxdImageAlias;
    }

    public String getServerUrl() {
        return serverUrl;
    }


    public void readFromFile(String filePath) {

        logger.log("Reading config file...");
        StringBuilder jsonStringBuilder = new StringBuilder();
        List<String> lines = new ArrayList<>();

        Path file = Paths.get(filePath);
        try {
            lines = Files.readAllLines(file, Charset.forName("UTF-8"));

            for (String line : lines) {
                jsonStringBuilder.append(line);
            }

            ConfigDataElement configDataElement = new Gson().fromJson(jsonStringBuilder.toString(), ConfigDataElement.class);
            int count = setParametersFromConfig(configDataElement);

            logger.log("Config file imported. Set " + count + " parameters.");
        } catch (Exception e) {
            logger.log("Cannot read config file from: " + filePath);
            logger.log("Using default values instead.");
            e.printStackTrace();
        }

    }

    private int setParametersFromConfig(ConfigDataElement configDataElement) {
        int count = 0;
        if (configDataElement != null) {
            if (configDataElement.getCertPath() != null) {
                count++;
                certPath = configDataElement.getCertPath();
            }

            if (configDataElement.getKeyfilePath() != null) {
                count++;
                keyfilePath = configDataElement.getKeyfilePath();
            }

            if (configDataElement.getLxdImageAlias() != null) {
                count++;
                lxdImageAlias = configDataElement.getLxdImageAlias();
            }

            if (configDataElement.getServerUrl() != null) {
                count++;
                serverUrl = configDataElement.getServerUrl();
            }

        }

        return count;
    }

}
