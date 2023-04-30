package com.jayfella.jfx.embedded.jfx;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The resizable image view.
 *
 * @author malte3d
 * @author JavaSaBr
 * @author jayfella
 */
@Getter
@Setter
public class EditorFxImageView extends ImageView {

    private static final long DEFAULT_RESIZE_DELAY = 100;
    private static final double MIN_SIZE = 128;
    private static final double MAX_SIZE = Double.MAX_VALUE;

    private final Timer timer = new Timer();
    private TimerTask resizeTask;

    private double maxWidth = MAX_SIZE;
    private double maxHeight = MAX_SIZE;

    private long resizeDelayInMs = DEFAULT_RESIZE_DELAY;
    private double lastWidth = -1;
    private double lastHeight = -1;

    @Override
    public double minHeight(double width) {
        return MIN_SIZE;
    }

    @Override
    public double maxHeight(double width) {
        return Math.max(maxHeight, minHeight(width));
    }

    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }

    @Override
    public double minWidth(double height) {
        return MIN_SIZE;
    }

    @Override
    public double maxWidth(double height) {
        return Math.max(maxWidth, minWidth(height));
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    /**
     * To prevent to frequent resizing, the resize will only be scheduled after the defined delay
     */
    @Override
    public void resize(double width, double height) {

        if (width != lastWidth || height != lastHeight) {

            lastWidth = width;
            lastHeight = height;

            if (resizeTask != null)
                resizeTask.cancel();

            resizeTask = new TimerTask() {
                @Override
                public void run() {
                    resizeAfter(width, height);
                }
            };

            timer.schedule(resizeTask, resizeDelayInMs);
        }
    }

    private void resizeAfter(double width, double height) {
        super.resize(width, height);
        super.setFitWidth(width);
        super.setFitHeight(height);
    }

}
