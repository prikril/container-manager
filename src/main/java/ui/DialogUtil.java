package ui;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

public class DialogUtil {

    public static TextInputDialog createCenteredTextInputDialog(String title, String headerText, String contentText,
                                                                String defaultValue, double posX, double posY) {

        TextInputDialog tempDialog = new TextInputDialog(defaultValue);
        tempDialog.initModality(Modality.NONE); // Fix for resize issue of main window (linux-only), but causes break of dragdrop
        // JDK Bug 8140491  https://stackoverflow.com/questions/33134791/javafx-stage-cant-be-resized-after-showing-a-dialog
        tempDialog.setTitle(title);
        tempDialog.setHeaderText(headerText);
        tempDialog.setContentText(contentText);
        tempDialog.setX(-10000);
        tempDialog.setY(-10000);
        tempDialog.show();

        double width = tempDialog.getWidth();
        double height = tempDialog.getHeight();

        tempDialog.close();



        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initModality(Modality.NONE); // Fix for resize issue of main window (linux-only), but causes break of dragdrop
        // JDK Bug 8140491  https://stackoverflow.com/questions/33134791/javafx-stage-cant-be-resized-after-showing-a-dialog
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.setX(-10000);
        dialog.setY(-10000);
        //dialog.show(); // must not be shown, because setting x and y won't work then anymore, JDK bug?

        // workaround: use size values from temp dialog

        double dialogPosX = posX - width / 2;
        double dialogPosY = posY - height / 2;

        //dialog.hide(); // hiding means closing, setting x and y won't work then anymore, JDK bug?
        dialog.setX(dialogPosX);
        dialog.setY(dialogPosY);

        return dialog;
    }

    public static Alert createCenteredAlertDialog(Alert.AlertType type, String title, String headerText,
                                                  String contentText, double posX, double posY) {

        Alert tempAlert = new Alert(type);
        tempAlert.initModality(Modality.NONE); // Fix for resize issue of main window (linux-only), but causes break of dragdrop
        // JDK Bug 8140491  https://stackoverflow.com/questions/33134791/javafx-stage-cant-be-resized-after-showing-a-dialog
        tempAlert.setTitle(title);
        tempAlert.setHeaderText(headerText);
        tempAlert.setContentText(contentText);
        tempAlert.setX(-10000);
        tempAlert.setY(-10000);
        tempAlert.show();

        double width = tempAlert.getWidth();
        double height = tempAlert.getHeight();

        tempAlert.close();

        Alert alert = new Alert(type);
        alert.initModality(Modality.NONE); // Fix for resize issue of main window (linux-only), but causes break of dragdrop
        // JDK Bug 8140491  https://stackoverflow.com/questions/33134791/javafx-stage-cant-be-resized-after-showing-a-dialog
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setX(-10000);
        alert.setY(-10000);

        double alertPosX = posX - width / 2;
        double alertPosY = posY - height / 2;

        alert.setX(alertPosX);
        alert.setY(alertPosY);

        return alert;
    }

}
