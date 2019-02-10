package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DragIcon extends AnchorPane {

    @FXML
    private AnchorPane root_pane;

    @FXML
    private AnchorPane image_pane;

    @FXML
    private Label icon_label;

    private DragIconType mType = null;

    public DragIcon() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DragIcon.fxml"));
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
    }

    public void relocateToPoint(Point2D p) {
        // relocates the object to a point that has been converted to scene coordinates
        Point2D localCoords = root_pane.getParent().sceneToLocal(p);
        int sceneX = (int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2));
        int sceneY = (int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2));
        relocate(sceneX, sceneY);
    }

    public DragIconType getType() {
        return mType;
    }

    public void setType(DragIconType type) {
        mType = type;

        getStyleClass().clear(); // does not work! JDK bug since 2011
        // https://stackoverflow.com/questions/10887525/javafx-style-class-wont-refresh
        // https://community.oracle.com/thread/2312650
        getStyleClass().add("dragicon");


        switch (mType) {
            case server:
                image_pane.getStyleClass().add("icon-server");
                icon_label.setText("Container");
                break;

            case switch_hub:
                image_pane.getStyleClass().add("icon-switch-hub");
                icon_label.setText("Network");
                break;
        }

    }
}
