package ui.node;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.*;

import api.container.ContainerController;
import application.GlobalInventory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import ui.DialogUtil;
import ui.DragContainer;
import ui.DragIconType;
import ui.NodeLinkLine;


public class DraggableNode extends AnchorPane {

    @FXML
    private AnchorPane root_pane;

    @FXML
    private AnchorPane left_link_handle;

    @FXML
    private AnchorPane right_link_handle;

    @FXML
    private Label title_bar;

    @FXML
    private Label close_button;

    @FXML
    private HBox image_pane;

    private EventHandler<MouseEvent> mLinkHandleDragDetected;
    private EventHandler<DragEvent> mLinkHandleDragDropped;
    private EventHandler<DragEvent> mContextLinkDragOver;
    private EventHandler<DragEvent> mContextLinkDragDropped;

    private EventHandler<DragEvent> mContextDragOver;
    private EventHandler<DragEvent> mContextDragDropped;

    private DragIconType mType = null;

    private Point2D mDragOffset = new Point2D(0.0, 0.0);

    private final DraggableNode self;

    private NodeLinkLine mDragLink = null;
    private AnchorPane right_pane = null;

    private final List<String> mLinkIds = new ArrayList<String>();

    private final String name;
    private final DragIconType type;

    public DraggableNode(String name, DragIconType type) {
        this.name = name;
        this.type = type;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DraggableNode.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        self = this;

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        // provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());

        this.title_bar.setText(name);
    }

    public String getName() {
        return name;
    }

    @FXML
    private void initialize() {
        buildNodeDragHandlers();
        buildLinkDragHandlers();

        left_link_handle.setOnDragDetected(mLinkHandleDragDetected);
        right_link_handle.setOnDragDetected(mLinkHandleDragDetected);

        left_link_handle.setOnDragDropped(mLinkHandleDragDropped);
        right_link_handle.setOnDragDropped(mLinkHandleDragDropped);

        mDragLink = new NodeLinkLine();
        mDragLink.setVisible(false);

        parentProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                right_pane = (AnchorPane) getParent();
            }

        });

        if (type == DragIconType.server) {
            createContainerContextMenu();
        }

    }

    public void registerLink(String linkId, DraggableNode other, String adapterName) {
        mLinkIds.add(linkId);
        System.out.println("Connections at " + name + ": " + mLinkIds.size());
    }

    public void relocateToPoint(Point2D p) {
        // relocates the object to a point that has been converted to scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);
        final int additionalOffset = 0; // (Dirty) Fix to reposition nodes, if needed
        final int sceneX = (int) (localCoords.getX() - mDragOffset.getX() + additionalOffset);
        final int sceneY = (int) (localCoords.getY() - mDragOffset.getY() + additionalOffset);
        relocate(sceneX, sceneY);
    }

    public DragIconType getType() {
        return mType;
    }

    public void setType(DragIconType type) {
        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");

        switch (mType) {

            case switch_hub:
                image_pane.getStyleClass().add("icon-switch-hub");
                break;

            default:
                image_pane.getStyleClass().add("icon-server");
                break;
        }
    }

    private void buildNodeDragHandlers() {
        mContextDragOver = new EventHandler<DragEvent>() {
            // dragover to handle node dragging in the right pane view
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                event.consume();
            }
        };

        // dragdrop for node dragging
        mContextDragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                event.setDropCompleted(true);

                event.consume();
            }
        };

        // close button click
        close_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Window window = self.getScene().getWindow();
                double posX = window.getX() + window.getWidth() / 2;
                double posY = window.getY() + window.getHeight() / 2;

                String typeDescription = type == DragIconType.server ? "container" : "network";

                Alert alert = DialogUtil.createCenteredAlertDialog(
                        Alert.AlertType.CONFIRMATION,
                        "Remove " + typeDescription + "?",
                        "The " + typeDescription + " will be removed.",
                        "Do you really want to remove the " + typeDescription + "?",
                        posX, posY);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() != ButtonType.OK) {
                    return;
                }

                AnchorPane parent = (AnchorPane) self.getParent();

                for (ListIterator<String> iterator = mLinkIds.listIterator(); iterator.hasNext(); ) {
                    String id = iterator.next();

                    // Search nodeLinkLines in child elements
                    List<Node> childNodes = new ArrayList<>(parent.getChildren()); // Prevent ConcurrentModificationException
                    for (ListIterator<Node> iterNode = childNodes.listIterator(); iterNode.hasNext(); ) {
                        Node node = iterNode.next();

                        if (node.getId() == null) {
                            continue;
                        }

                        // if connection is found in child elements
                        if (node.getId().equals(id)) {
                            if (node instanceof NodeLinkLine) {
                                NodeLinkLine nodeLink = (NodeLinkLine) node;
                                // Prevent ConcurrentModificationException
                                if (nodeLink.getTarget().getId().equals(getId())) {
                                    nodeLink.getSource().removeLink(id, self);
                                } else {
                                    nodeLink.getTarget().removeLink(id, self);
                                }
                                parent.getChildren().remove(nodeLink.getAdapterNameLabel()); // remove link label from GUI
                                parent.getChildren().remove(node); // remove link from GUI
                            }
                        }
                    }

                    iterator.remove(); // remove link from internal list
                    System.out.println("Connection " + id + " removed from " + name +" (with iterator).");
                    System.out.println("Connections at \"" + name + "\" : " + mLinkIds.size());
                }
                parent.getChildren().remove(self);
                removeNode();
            }

        });

        // drag detection for node dragging
        title_bar.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);
                getParent().setOnDragOver(mContextDragOver);
                getParent().setOnDragDropped(mContextDragDropped);

                // begin drag ops
                mDragOffset = new Point2D(event.getX(), event.getY());

                relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData("type", mType.toString());
                content.put(DragContainer.AddNode, container);

                startDragAndDrop(TransferMode.ANY).setContent(content);

                event.consume();
            }

        });
    } // end of buildNodeDragHandlers()

    private void buildLinkDragHandlers() {
        mLinkHandleDragDetected = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver(mContextLinkDragOver);
                getParent().setOnDragDropped(mContextLinkDragDropped);

                // Set up user-draggable link
                right_pane.getChildren().add(0, mDragLink);

                mDragLink.setVisible(false);

                Point2D p = new Point2D(getLayoutX() + (getWidth() / 2.0), getLayoutY() + (getHeight() / 2.0));
                mDragLink.setStart(p);

                // Drag content code
                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                // pass the UUID of the source node for later lookup
                container.addData("source", getId());

                content.put(DragContainer.AddLink, container);

                startDragAndDrop(TransferMode.ANY).setContent(content);

                event.consume();
            }
        };

        mLinkHandleDragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                // get the drag data. If it's null, abort.
                // This isn't the drag event we're looking for.
                DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

                if (container == null) {
                    return;
                }

                // hide the draggable NodeLink and remove it from the right-hand AnchorPane's
                // children
                mDragLink.setVisible(false);
                right_pane.getChildren().remove(0);

                //AnchorPane link_handle = (AnchorPane) event.getSource();

                ClipboardContent content = new ClipboardContent();

                // pass the UUID of the target node for later lookup
                container.addData("target", getId());

                content.put(DragContainer.AddLink, container);

                event.getDragboard().setContent(content);
                event.setDropCompleted(true);
                event.consume();
            }
        };

        mContextLinkDragOver = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);

                // Relocate end of user-draggable link
                if (!mDragLink.isVisible()) {
                    mDragLink.setVisible(true);
                }

                mDragLink.setEnd(new Point2D(event.getX(), event.getY()));

                event.consume();

            }
        };

        // drop event for link creation
        mContextLinkDragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("Dropped connection from " + self.toString());

                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                // hide the draggable NodeLink and remove it from the right-hand AnchorPane's
                // children
                //mDragLink.setVisible(false); //commented out for testing 2018-03-13
                right_pane.getChildren().remove(0);

                event.setDropCompleted(true);
                event.consume();
            }

        };

    } // end of buildLinkDragHandlers

    public void removeLink(String id, DraggableNode other) {
        if (mLinkIds.contains(id)) {
            mLinkIds.remove(id);
            System.out.println("Connection " + id + " removed from " + name + ".");
            System.out.println("Connections at \"" + this.name + "\" : " + mLinkIds.size());
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    public boolean connectionExists(DraggableNode target) {
        AnchorPane parent = (AnchorPane) self.getParent();

        for (String linkId : mLinkIds) {
            for (Node node : parent.getChildren()) {
                if (node.getId() == null) {
                    continue;
                }

                if (node.getId().equals(linkId)) {
                    if (node instanceof NodeLinkLine) {
                        NodeLinkLine nodeLink = (NodeLinkLine) node;
                        // Prevent ConcurrentModificationException
                        if (nodeLink.getTarget().getId().equals(getId())) {
                            // this node is the target of a connection
                            if (nodeLink.getSource().getId().equals(target.getId())) {
                                return true;
                            }
                        } else {
                            // this node is the source of a connection
                            if (nodeLink.getTarget().getId().equals(target.getId())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        // no connections found
        return false;
    }

    private void createContainerContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Start");
        item1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                new ContainerController().startContainer(name);
            }
        });

        MenuItem item2 = new MenuItem("Stop");
        item2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                new ContainerController().stopContainer(name);
            }
        });

        MenuItem item3 = new MenuItem("Terminal");
        item3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                startTerminal(name);
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(item1, item2, item3);

        root_pane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(root_pane, event.getScreenX(), event.getScreenY());
            }
        });

    }

    private void startTerminal(String containerName) {
        Runtime runtime = Runtime.getRuntime();
        //String command = "/usr/bin/xterm";
        //String command = "/usr/bin/xfce4-terminal --command 'lxc exec " + containerName + " -- /bin/bash' --hold"; //doesn't work
        String[] command = {"/usr/bin/xfce4-terminal", "-e", "lxc exec " + containerName + " -- /bin/bash", "--hold", "-T", containerName};

        try {
            Process process = runtime.exec(command);
            int exitValue = process.waitFor();
            System.out.println("Started terminal with exit code:");
            System.out.println(exitValue);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // read the output from the command
            System.out.println("Standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeNode() {

    }

}
