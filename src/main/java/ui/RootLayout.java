package ui;

import api.container.ContainerController;
import api.network.NetworkController;
import application.GlobalInventory;
import application.entity.container.ContainerEntity;
import application.entity.network.NetworkEntity;
import application.entity.network.NetworkType;
import application.event.task.AllTasksExecutedEvent;
import application.logging.LoggingFacade;
import application.task.TaskService;
import application.task.container.CreateContainerTask;
import application.task.network.CreateNetworkTask;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Subscribe;
import persistence.file.ExportHandler;
import persistence.file.ImportHandler;
import ui.node.ContainerNode;
import ui.node.DraggableNode;
import ui.node.NetworkNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RootLayout extends VBox {

    @FXML
    VBox root_window;

    @FXML
    AnchorPane root_pane;

    @FXML
    SplitPane base_pane;

    @FXML
    AnchorPane right_pane;

    @FXML
    VBox left_pane;

    @FXML
    MenuItem menuitem_import;

    @FXML
    MenuItem menuitem_export;

    @FXML
    MenuItem menuitem_close;

    @FXML
    MenuItem menuitem_cleanup;

    @FXML
    MenuItem menuitem_lxd_load_networks;

    @FXML
    MenuItem menuitem_lxd_load_containers;

    @FXML
    MenuItem menuitem_lxd_clean_host;

    @FXML
    MenuItem menuitem_lxd_apply_changes;

    @FXML
    MenuItem menuitem_about;

    @FXML
    TextArea textarea_logging;

    @FXML
    Button button_clear_log;

    @FXML
    Button button_apply_now;

    private LoggingFacade logger = LoggingFacade.getInstance();

    private DragIcon mDragOverIcon = null;

    private EventHandler<DragEvent> mIconDragOverRoot = null;
    private EventHandler<DragEvent> mIconDragDropped = null;
    private EventHandler<DragEvent> mIconDragOverRightPane = null;

    public RootLayout() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RootLayout.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {

        logger.setLoggingOutput(textarea_logging);
        logger.flush();

        createDragOverIcon();

        // populate left pane with multiple colored icons for testing
        int iconTypes = DragIconType.values().length;
        for (int i = 0; i < iconTypes; i++) {

            DragIcon icn = new DragIcon();

            addDragDetection(icn);

            icn.setType(DragIconType.values()[i]);
            left_pane.getChildren().add(icn);
        }

        buildDragHandlers();

        buildMenuBarEventHandlers();
        buildButtonEventHandlers();
    }

    private void createDragOverIcon() {
        // Add one icon that will be used for the drag-drop process
        // This is added as a child to the root anchorpane so it can be visible
        // on both sides of the split pane.
        mDragOverIcon = new DragIcon();
        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        root_pane.getChildren().add(mDragOverIcon);
    }

    private void addDragDetection(DragIcon dragIcon) {

        dragIcon.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("caught onDragDetected event");
                // set drag event handlers on their respective objects
                base_pane.setOnDragOver(mIconDragOverRoot);
                right_pane.setOnDragOver(mIconDragOverRightPane);
                right_pane.setOnDragDropped(mIconDragDropped);

                // recreate dragOverIcon to prevent styleClass bug
                // https://stackoverflow.com/questions/10887525/javafx-style-class-wont-refresh
                // https://community.oracle.com/thread/2312650
                if (mDragOverIcon != null) {
                    root_pane.getChildren().remove(mDragOverIcon);
                }

                createDragOverIcon();

                // get a reference to the clicked DragIcon object
                DragIcon icn = (DragIcon) event.getSource();
                System.out.println("event icon type: " + icn.getType().toString());
                if (mDragOverIcon.getType() != null) {
                    System.out.println("dragovericontype (before): " + mDragOverIcon.getType().toString());
                } else {
                    System.out.println("dragovericontype (before): null");
                }

                // begin drag ops
                mDragOverIcon.setType(icn.getType());
                mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                System.out.println("dragovericontype (after): " + mDragOverIcon.getType().toString());

                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData("type", mDragOverIcon.getType().toString());
                content.put(DragContainer.AddNode, container);

                mDragOverIcon.startDragAndDrop(TransferMode.ANY).setContent(content);
                mDragOverIcon.setVisible(true);
                mDragOverIcon.setMouseTransparent(true);
                event.consume();
            }
        });
    }

    private void buildDragHandlers() {

        // drag over transition to move widget form left pane to right pane
        mIconDragOverRoot = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

                // turn on transfer mode and track in the right-pane's context
                // if (and only if) the mouse cursor falls within the right pane's bounds.
                if (!right_pane.boundsInLocalProperty().get().contains(p)) {
                    event.acceptTransferModes(TransferMode.ANY);
                    mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                    return;
                }

                event.consume();
            }
        };

        mIconDragOverRightPane = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);

                // convert the mouse coordinates to scene coordinates,
                // then convert back to coordinates that are relative to
                // the parent of mDragIcon. Since mDragIcon is a child of the root
                // pane, coodinates must be in the root pane's coordinate system to work
                // properly.
                mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                event.consume();
            }
        };

        mIconDragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

                container.addData("scene_coords", new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                content.put(DragContainer.AddNode, container);

                event.getDragboard().setContent(content);
                event.setDropCompleted(true);
            }
        };

        //called when dragged from left to right part of splitpane
        this.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
                right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
                base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

                mDragOverIcon.setVisible(false);

                double posX = getScene().getWindow().getX() + getScene().getWindow().getWidth() / 2;
                double posY = getScene().getWindow().getY() + getScene().getWindow().getHeight() / 2;

                // Create node drag operation
                DragContainer dragContainer = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);
                // is not null when a new node is dragged to right_pane
                // is null when new line is created
                if (dragContainer != null) {
                    if (dragContainer.getValue("scene_coords") != null) {

                        DraggableNode node = null;

                        if (dragContainer.getValue("type").equals(DragIconType.switch_hub.toString())) {
                            // network icon

                            List<String> networkNames = new ArrayList<>(GlobalInventory.getInstance().networks.keySet());

                            TextInputDialog dialog = DialogUtil.createCenteredTextInputDialog("Creating new network",
                                    "A new network will be created.", "Please enter a name:",
                                    "net123", posX, posY);

                            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

                            dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
                                @Override
                                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                    if (networkNames.contains(newValue) || newValue.contains(" ")) {
                                        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                                        System.out.println(newValue + " is already a network!");
                                    } else {
                                        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                        System.out.println("Network name \"" + newValue + "\" accepted.");
                                    }
                                }
                            });

                            // Traditional way to get the response value.
                            Optional<String> result = dialog.showAndWait();

                            if (result.isPresent()) {
                                String networkName = result.get();
                                NetworkNode networkNode = new NetworkNode(networkName);
                                node = networkNode;

                                System.out.println("Painted new network with name \"" + networkName + "\" and nodeId " + node.getId());
                                GlobalInventory.getInstance().networks.put(networkName, new NetworkEntity(networkName, NetworkType.BRIDGE, networkNode)); //TODO: get type dynamically
                                TaskService.getInstance().addNetworkTask(new CreateNetworkTask(networkName));
                            }

                        } else if (dragContainer.getValue("type").equals(DragIconType.server.toString())) {
                            // container icon

                            List<String> containerNames = new ArrayList<>(GlobalInventory.getInstance().containers.keySet());

                            TextInputDialog dialog = DialogUtil.createCenteredTextInputDialog("Creating new container",
                                    "A new container will be created.", "Please enter a name:",
                                    "cont123", posX, posY);

                            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

                            dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
                                @Override
                                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                    if (containerNames.contains(newValue) || newValue.contains(" ")) {
                                        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                                        System.out.println(newValue + " is already a container!");
                                    } else {
                                        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                        System.out.println("Container name \"" + newValue + "\" accepted.");
                                    }
                                }
                            });

                            // Traditional way to get the response value.
                            Optional<String> result = dialog.showAndWait();

                            if (result.isPresent()) {
                                String containerName = result.get();
                                ContainerNode containerNode = new ContainerNode(containerName);
                                node = containerNode;

                                System.out.println("Painted new container with name \"" + containerName + "\" and nodeId " + node.getId());
                                GlobalInventory.getInstance().containers.put(containerName, new ContainerEntity(containerName, containerNode));
                                TaskService.getInstance().addContainerTask(new CreateContainerTask(containerName));
                            }

                        }

                        if (node != null) {
                            node.setType(DragIconType.valueOf(dragContainer.getValue("type")));
                            right_pane.getChildren().add(node);
                            Point2D cursorPoint = dragContainer.getValue("scene_coords");
                            node.relocateToPoint(new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32));
                            /*System.out.println("Neuer Container mit Namen \"" + containerName + "\" gezeichnet. " + node.getId());
                            TaskService.getInstance().addContainerTask(new CreateContainerTask(containerName));*/
                        }

                    }
                }

                /*
                 * //Move node drag operation container = (DragContainer) event.getDragboard().getContent(DragContainer.DragNode);
                 *
                 * if (container != null) {
                 * 		if (container.getValue("type") != null)
                 * 			System.out.println ("Moved node " + container.getValue("type"));
                 * }
                 */

                // AddLink drag operation (link ist going to be created)
                dragContainer = (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);
                // dragContainer is null when a new node is dragged to right_pane
                if (dragContainer != null) {

                    // bind the ends of our link to the nodes whose id's are stored in the drag
                    // container
                    String sourceId = dragContainer.getValue("source");
                    String targetId = dragContainer.getValue("target"); // is null when not dragged to another node

                    if (sourceId != null && targetId != null) {
                        DraggableNode source = null;
                        DraggableNode target = null;

                        for (Node n : right_pane.getChildren()) {
                            if (n.getId() == null) {
                                continue;
                            }

                            if (n.getId().equals(sourceId)) {
                                source = (DraggableNode) n;
                            }

                            if (n.getId().equals(targetId)) {
                                target = (DraggableNode) n;
                            }
                        }

                        if (source != null && target != null) {
                            if (!source.connectionExists(target)) {

                                if (source.getType() == target.getType()) {
                                    Alert alert = DialogUtil.createCenteredAlertDialog(AlertType.INFORMATION, "Information",
                                            "Connection was not made!", "Connection not allowed!", posX, posY);

                                    alert.show();
                                } else {
                                    String containerName;
                                    if (source instanceof ContainerNode) {
                                        containerName = ((ContainerNode) source).getName();
                                    } else {
                                        containerName = ((ContainerNode) target).getName();
                                    }

                                    List<String> adapterNames = new ArrayList<>(GlobalInventory.getInstance().containers.get(containerName).getNetworks().keySet());

                                    TextInputDialog dialog = DialogUtil.createCenteredTextInputDialog("Creating new connection", "Adapter name needed.",
                                            "Please enter a name:", "eth123", posX, posY);

                                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

                                    dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
                                        @Override
                                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                            if (adapterNames.contains(newValue) || newValue.contains(" ")) {
                                                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                                                System.out.println(newValue + " is already a adapter!");
                                            } else {
                                                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                                                System.out.println("Adapter name \"" + newValue + "\" accepted.");
                                            }
                                        }
                                    });

                                    // Traditional way to get the response value.
                                    Optional<String> result = dialog.showAndWait();

                                    if (result.isPresent()) {
                                        NodeLinkLine link = new NodeLinkLine(right_pane);
                                        // add our link at the top of the rendering order so it's rendered first
                                        right_pane.getChildren().add(0, link);
                                        link.bindEnds(source, target, result.get());
                                    }
                                }
                            } else {
                                Alert alert = DialogUtil.createCenteredAlertDialog(AlertType.INFORMATION, "Information",
                                        "Connection was not made!", "Connection already exists!", posX, posY);
                                
                                alert.show();
                            }
                        }
                    }

                }

                event.consume();
            }
        });
    } // end of buildDragHandlers()


    private void buildMenuBarEventHandlers() {

        // File menu
        menuitem_import.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String filePath = showOpenFileDialog();
                if (filePath == null) {
                    return;
                }

                if (new ImportHandler().importFile(filePath)) {
                    paintGlobalInventory();
                    createTasksForInventory();
                }
            }
        });

        menuitem_export.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String filePath = showSaveFileDialog();
                if (filePath == null) {
                    return;
                }

                new ExportHandler().exportAll(filePath);
            }
        });

        menuitem_close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });


        // Edit menu
        menuitem_cleanup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cleanup();
            }
        });


        // LXD menu
        menuitem_lxd_load_networks.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NetworkController networkController = new NetworkController();
                networkController.fetchNetworkNames();
            }
        });

        menuitem_lxd_load_containers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ContainerController containerController = new ContainerController();
                containerController.fetchContainerNames();
            }
        });

        menuitem_lxd_clean_host.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TaskService.getInstance().cleanHost();
            }
        });

        menuitem_lxd_apply_changes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TaskService.getInstance().executeAllTasks();
            }
        });

        menuitem_about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double posX = getScene().getWindow().getX() + getScene().getWindow().getWidth() / 2;
                double posY = getScene().getWindow().getY() + getScene().getWindow().getHeight() / 2;

                Dialog dialog = DialogUtil.createCenteredAlertDialog(AlertType.INFORMATION, "About",
                        "Copyright by Dominik Prikril\n2018", "More information at <link>", posX, posY);
                dialog.show();
            }
        });

    }

    private void buildButtonEventHandlers() {
        button_clear_log.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.clear();
            }
        });

        button_apply_now.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TaskService.getInstance().executeAllTasks();
            }
        });
    }

    private void paintGlobalInventory() {
        right_pane.getChildren().clear();

        for (NetworkEntity entity : GlobalInventory.getInstance().networks.values()) {
            right_pane.getChildren().add(entity.getNode());
        }

        for (ContainerEntity entity : GlobalInventory.getInstance().containers.values()) {
            right_pane.getChildren().add(entity.getNode());
            Map<String, String> networks = new HashMap<>(entity.getNetworks());
            for (String adapterName : networks.keySet()) {
                NodeLinkLine nodeLinkLine = new NodeLinkLine(right_pane);
                right_pane.getChildren().add(0, nodeLinkLine);
                NetworkEntity networkEntity = GlobalInventory.getInstance().networks.get(networks.get(adapterName));
                // warning: bindEnds will cause ContainerNode to add network again in registerLink!!!
                nodeLinkLine.bindEnds(entity.getNode(), networkEntity.getNode(), adapterName);
            }
            entity.setNetworks(networks); // fixes behaviour from registerLink in ContainerNode
        }

        logger.log("Import finished.");
    }

    private void createTasksForInventory() {
        TaskService.getInstance().clear();

        for (ContainerEntity entity : GlobalInventory.getInstance().containers.values()) {
            TaskService.getInstance().addContainerTask(new CreateContainerTask(entity.getName()));
        }

        for (NetworkEntity entity : GlobalInventory.getInstance().networks.values()) {
            TaskService.getInstance().addNetworkTask(new CreateNetworkTask(entity.getName()));
        }

    }

    private void cleanup() {
        right_pane.getChildren().clear();
        GlobalInventory.getInstance().clear();
        TaskService.getInstance().clear();
    }

    private String showOpenFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        double oldX = getParent().getScene().getWindow().getX();
        double oldY = getParent().getScene().getWindow().getY();
        double oldWidth = getParent().getScene().getWindow().getWidth();
        double oldHeight = getParent().getScene().getWindow().getHeight();

        /*
        // half working dirty fix - filedialog is in wrong place, always centered at first screen
        Stage tempStage = new Stage();
        tempStage.setScene(new Scene(new BorderPane()));
        tempStage.setTitle("File dialog helper window");
        tempStage.show();
        tempStage.setX(oldX);
        tempStage.setY(oldY);
        tempStage.toFront();
        File file = fileChooser.showOpenDialog(tempStage);
        tempStage.hide();
        */

        // working but ugly and dirty fix for:
        // https://stackoverflow.com/questions/38243363/modal-javafx-stage-initowner-prevents-owner-from-resizing-bug/47189826#47189826
        File file = fileChooser.showOpenDialog(getParent().getScene().getWindow());
        getParent().getScene().getWindow().hide();
        ( (Stage) getParent().getScene().getWindow() ).show();
        getParent().getScene().getWindow().setX(oldX);
        getParent().getScene().getWindow().setY(oldY);
        getParent().getScene().getWindow().setWidth(oldWidth);
        getParent().getScene().getWindow().setHeight(oldHeight);

        if (file != null) {
            return file.getAbsolutePath();
        }

        return null;
    }

    private String showSaveFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        double oldX = getParent().getScene().getWindow().getX();
        double oldY = getParent().getScene().getWindow().getY();
        double oldWidth = getParent().getScene().getWindow().getWidth();
        double oldHeight = getParent().getScene().getWindow().getHeight();

        File file = fileChooser.showSaveDialog(getParent().getScene().getWindow());

        // same ugly fix as in openFileDialog
        getParent().getScene().getWindow().hide();
        ( (Stage) getParent().getScene().getWindow() ).show();
        getParent().getScene().getWindow().setX(oldX);
        getParent().getScene().getWindow().setY(oldY);
        getParent().getScene().getWindow().setWidth(oldWidth);
        getParent().getScene().getWindow().setHeight(oldHeight);

        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".json")) {
                file = new File(file.toString() + ".json");
            }
            return file.getAbsolutePath();
        }

        return null;
    }

    @Subscribe
    public void onAllTasksExecutedEvent(AllTasksExecutedEvent event) {
        // button_apply_now.setDisable(false);
    }

}
