package ui;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import ui.node.DraggableNode;
import ui.node.NetworkNode;

import java.util.ListIterator;
import java.util.Optional;
import java.util.UUID;

public class NodeLinkLine extends Line {

    private Line node_link; // Don't use FXML because overlapping rectangles will prevent click listener!

    private DraggableNode source, target;

    public DraggableNode getSource() {
        return source;
    }

    public DraggableNode getTarget() {
        return target;
    }

    private Label adapterNameLabel = new Label();


    public NodeLinkLine() {

        // provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());
        //testing...
        node_link = this;
        this.setStrokeWidth(5.0);
        this.setStroke(Color.BLACK);

        adapterNameLabel.setVisible(false);

        System.out.println("nodelinkline constructor");
    }

    public NodeLinkLine(Pane parent) {
        this(); // call default constructor
        parent.getChildren().add(adapterNameLabel);
    }

    private void createEventHandlers() {
        node_link.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeLinkLine();
            }
        });

        adapterNameLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeLinkLine();
            }
        });
    }

    private void removeLinkLine() {
        double posX = node_link.getScene().getWindow().getX() + node_link.getScene().getWindow().getWidth() / 2;
        double posY = node_link.getScene().getWindow().getY() + node_link.getScene().getWindow().getHeight() / 2;

        Alert alert = DialogUtil.createCenteredAlertDialog(
                AlertType.CONFIRMATION,
                "Remove connection?",
                "The connection will be removed.",
                "Do you really want to remove the connection?",
                posX, posY);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            AnchorPane parent = (AnchorPane) getParent();
            if (parent != null) {
                //could be null if multiple alert dialogs are open at the same time
                for (ListIterator<Node> iterNode = parent.getChildren().listIterator(); iterNode.hasNext(); ) {
                    Node node = iterNode.next();
                    if (node.getId() == null) {
                        continue;
                    }

                    if (node.getId().equals(getId())) {
                        iterNode.remove();
                        removeLinksInNodes();
                        parent.getChildren().remove(adapterNameLabel);
                        break;
                    }
                }
            }
        }
    }

    public void setStart(Point2D startPoint) {
        node_link.setStartX(startPoint.getX());
        node_link.setStartY(startPoint.getY());
    }

    public void setEnd(Point2D endPoint) {
        node_link.setEndX(endPoint.getX());
        node_link.setEndY(endPoint.getY());
    }

    public void bindEnds(DraggableNode source, DraggableNode target, String adapterName) {
        // use property for width and height too, otherwise connections are misplaced after import
        node_link.startXProperty().bind(Bindings.add(source.layoutXProperty(), (source.widthProperty().divide(2) )));
        node_link.startYProperty().bind(Bindings.add(source.layoutYProperty(), (source.heightProperty().divide(2) )));

        node_link.endXProperty().bind(Bindings.add(target.layoutXProperty(), (target.widthProperty().divide(2) )));
        node_link.endYProperty().bind(Bindings.add(target.layoutYProperty(), (target.heightProperty().divide(2) )));

        source.registerLink(getId(), target, adapterName);
        target.registerLink(getId(), source, adapterName);
        this.source = source;
        this.target = target;

        createEventHandlers();
        createAdapterLabel(adapterName);

        System.out.println("Connection for adapter \"" + adapterName + "\" created: " + getId());
    }

    private void createAdapterLabel(String adapterName) {
        if (adapterName == null) {
            return;
        }

        adapterNameLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        adapterNameLabel.setAlignment(Pos.CENTER);
        adapterNameLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3))));

        adapterNameLabel.setText(adapterName);
        adapterNameLabel.setVisible(true);
        adapterNameLabel.setManaged(false); // important! otherwise wrong size (0, 0) from UI manager
        //adapterNameLabel.setMinWidth(50); // didn't work
        //adapterNameLabel.setMinHeight(25); // didn't work
        adapterNameLabel.resize(50,25); // TODO: what about dynamic sizing?
        //adapterNameLabel.autosize(); // didn't work
        adapterNameLabel.setLayoutX(100);
        adapterNameLabel.setLayoutY(50);

        float divider = 4f;
        // testing for label position near container node
        if (this.source instanceof NetworkNode) {
            divider = 0.75f;
        }
        divider = 2f; // override as a temporary fix

        adapterNameLabel.layoutXProperty().bind(
                Bindings.add( node_link.startXProperty(), node_link.endXProperty() )
                        .divide(divider)
                        .subtract(adapterNameLabel.widthProperty().divide(2))
        );
        adapterNameLabel.layoutYProperty().bind(
                Bindings.add( node_link.startYProperty(), node_link.endYProperty() )
                        .divide(divider)
                        .subtract(adapterNameLabel.heightProperty().divide(2))
        );
		/*System.out.println(adapterNameLabel.getWidth() + " " + adapterNameLabel.getHeight());
		System.out.println(adapterNameLabel.getLayoutX() + " " + adapterNameLabel.getLayoutY());*/
    }

    public void removeLinksInNodes() {
        this.source.removeLink(getId(), target);
        this.target.removeLink(getId(), source);
    }

    public Label getAdapterNameLabel() {
        return adapterNameLabel;
    }

}
