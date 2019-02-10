package application.logging;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LoggingFacade {

    private static TextArea loggingOutput;

    private static Queue<String> loggingQueue = new ConcurrentLinkedQueue<>();

    private static boolean currentlyLogging = false;

    private static boolean cleanupRequested = false;


    private static LoggingFacade loggingInstance = new LoggingFacade();

    public static LoggingFacade getInstance() {
        return loggingInstance;
    }

    private LoggingFacade() {

    }

    public void setLoggingOutput(TextArea textArea) {
        loggingOutput = textArea;

        // from: https://stackoverflow.com/questions/17799160/javafx-textarea-and-autoscroll
        // but seems not to work sometimes...
        loggingOutput.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                loggingOutput.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
    }

    public void log(String text) {
        Calendar cal  = Calendar.getInstance();
        Date time = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("dd.MM.yyyy HH:mm:ss.SSS" );

        String currentTime = formatter.format( time );
        loggingQueue.add(currentTime + "\n" + text + "\n");
        printLog();
    }

    private void printLog() {
        if (!currentlyLogging) {
            if (loggingOutput != null) {
                currentlyLogging = true;

                while (loggingQueue.peek() != null) {
                    if (cleanupRequested) {
                        loggingOutput.clear();
                        cleanupRequested = false;
                    }

                    String message = loggingQueue.poll() + "\n";
                    // prevent javaFX exception
                    // see: https://stackoverflow.com/questions/30863862/javafx-append-text-to-textarea-throws-exception
                    Platform.runLater(() -> loggingOutput.appendText(message));
                }

                currentlyLogging = false;
            }
        }
    }

    public void clear() {
        if (!currentlyLogging) {
            currentlyLogging = true;
            loggingOutput.clear();
            currentlyLogging = false;
        } else {
            cleanupRequested = true;
        }
    }

    /**
     * print loggingQueue without adding a new log entry
     */
    public void flush() {
        printLog();
    }

}
