package persistence.file;

import application.GlobalInventory;
import application.entity.container.ContainerEntity;
import application.entity.network.NetworkEntity;
import application.logging.LoggingFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import persistence.entity.container.ContainerDataElement;
import persistence.entity.containernetwork.ContainerNetworkDataElement;
import persistence.entity.network.NetworkDataElement;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExportHandler {

    private LoggingFacade logger = LoggingFacade.getInstance();

    public void exportAll(String filePath) {
        logger.log("Starting export...");
        List<NetworkEntity> networkEntities = new ArrayList<>(GlobalInventory.getInstance().networks.values());
        List<NetworkDataElement> networkDataElements = convertToNetworkDataElement(networkEntities);
        List<ContainerEntity> containerEntities = new ArrayList<>(GlobalInventory.getInstance().containers.values());
        List<ContainerDataElement> containerDataElements = convertToContainerDataElement(containerEntities);

        ContainerNetworkDataElement containerNetworkDataElement = new ContainerNetworkDataElement();
        containerNetworkDataElement.setNetworks(networkDataElements);
        containerNetworkDataElement.setContainers(containerDataElements);

        writeToJsonFile(filePath, containerNetworkDataElement);
    }

    private List<NetworkDataElement> convertToNetworkDataElement(List<NetworkEntity> networkEntities) {
        List<NetworkDataElement> networkDataElements = new ArrayList<>();
        for (NetworkEntity entity : networkEntities) {
            NetworkDataElement networkDataElement = new NetworkDataElement();
            networkDataElement.setName(entity.getName());
            networkDataElement.setType(entity.getType());
            networkDataElement.setPosX((int) entity.getNode().getLayoutX());
            networkDataElement.setPosY((int) entity.getNode().getLayoutY());

            networkDataElements.add(networkDataElement);
        }
        return networkDataElements;
    }

    private List<ContainerDataElement> convertToContainerDataElement(List<ContainerEntity> containerEntities) {
        List<ContainerDataElement> containerDataElements = new ArrayList<>();
        for (ContainerEntity entity : containerEntities) {
            ContainerDataElement containerDataElement = new ContainerDataElement();
            containerDataElement.setName(entity.getName());
            containerDataElement.setPosX((int) entity.getNode().getLayoutX());
            containerDataElement.setPosY((int) entity.getNode().getLayoutY());
            containerDataElement.setNetworks(entity.getNetworks());
            containerDataElements.add(containerDataElement);
        }
        return containerDataElements;
    }

    private void writeToJsonFile(String filePath, ContainerNetworkDataElement containerNetworkDataElement) {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        String jsonString = prettyGson.toJson(containerNetworkDataElement);

        Path file = Paths.get(filePath);
        try {
            Files.write(file, Arrays.asList(jsonString.split("\n")), Charset.forName("UTF-8"));
            logger.log("Export finished.");
            logger.log("Saved file to: " + filePath);
        } catch (IOException e) {
            logger.log("Error during export.");
            e.printStackTrace();
        }
    }

}
