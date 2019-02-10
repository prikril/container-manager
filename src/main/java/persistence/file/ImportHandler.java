package persistence.file;

import application.GlobalInventory;
import application.entity.container.ContainerEntity;
import application.entity.network.NetworkEntity;
import application.logging.LoggingFacade;
import com.google.gson.Gson;
import persistence.entity.container.ContainerDataElement;
import persistence.entity.containernetwork.ContainerNetworkDataElement;
import persistence.entity.network.NetworkDataElement;
import ui.DragIconType;
import ui.node.ContainerNode;
import ui.node.NetworkNode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImportHandler {

    private LoggingFacade logger = LoggingFacade.getInstance();

    public boolean importFile(String filePath) {
        ContainerNetworkDataElement containerNetworkDataElement = readFromJsonFile(filePath);
        if (containerNetworkDataElement == null) {
            return false;
        }

        GlobalInventory.getInstance().clear();

        List<NetworkDataElement> networkDataElements = containerNetworkDataElement.getNetworks();
        List<NetworkEntity> networkEntities = convertToNetworkEntities(networkDataElements);
        for (NetworkEntity entity : networkEntities) {
            GlobalInventory.getInstance().networks.put(entity.getName(), entity);
        }

        List<ContainerDataElement> containerDataElements = containerNetworkDataElement.getContainers();
        List<ContainerEntity> containerEntities = convertToContainerEntities(containerDataElements);
        for (ContainerEntity entity : containerEntities) {
            GlobalInventory.getInstance().containers.put(entity.getName(), entity);
        }

        return true;
    }

    private List<NetworkEntity> convertToNetworkEntities(List<NetworkDataElement> networkDataElements) {
        List<NetworkEntity> networkEntities = new ArrayList<>();
        for (NetworkDataElement element : networkDataElements) {
            NetworkEntity networkEntity = new NetworkEntity();
            networkEntity.setName(element.getName());
            networkEntity.setType(element.getType());

            NetworkNode node = new NetworkNode(element.getName());
            node.setLayoutX(element.getPosX());
            node.setLayoutY(element.getPosY());
            node.setType(DragIconType.switch_hub);

            networkEntity.setNode(node);

            networkEntities.add(networkEntity);
        }
        return networkEntities;
    }

    private List<ContainerEntity> convertToContainerEntities(List<ContainerDataElement> containerDataElements) {
        List<ContainerEntity> containerEntities = new ArrayList<>();
        for (ContainerDataElement element : containerDataElements) {
            ContainerEntity containerEntity = new ContainerEntity(element.getName());

            ContainerNode node = new ContainerNode(element.getName());
            node.setLayoutX(element.getPosX());
            node.setLayoutY(element.getPosY());
            node.setType(DragIconType.server);

            containerEntity.setNode(node);
            containerEntity.setNetworks(element.getNetworks());
            containerEntities.add(containerEntity);
        }
        return containerEntities;
    }

    private ContainerNetworkDataElement readFromJsonFile(String filePath) {
        logger.log("Starting import...");
        StringBuilder jsonStringBuilder = new StringBuilder();
        List<String> lines = new ArrayList<>();

        Path file = Paths.get(filePath);
        logger.log("Reading file \"" + filePath + "\"");
        try {
            lines = Files.readAllLines(file, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            jsonStringBuilder.append(line);
        }

        try {
            ContainerNetworkDataElement containerNetworkDataElement = new Gson().fromJson(jsonStringBuilder.toString(), ContainerNetworkDataElement.class);
            return containerNetworkDataElement;
        } catch (Exception e) {
            e.printStackTrace();
            logger.log("Error during import!");
        }

        return null;
    }

}
