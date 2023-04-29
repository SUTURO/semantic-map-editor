package com.jayfella.jfx.embedded.jfx;

import javafx.scene.image.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The resizable image view.
 *
 * @author malte3d
 * @author JavaSaBr
 * @author jayfella
 */
public class EditorFxImageView extends ImageView {

    private static final long DEFAULT_RESIZE_DELAY = 50;
    private static final double MIN_SIZE = 128;
    private static final double MAX_SIZE = Double.MAX_VALUE;

    private final Timer timer = new Timer();

    private TimerTask resizeTask = null;
    private long resizeDelay = DEFAULT_RESIZE_DELAY;

    @Override
    public double minHeight(double width) {
        return MIN_SIZE;
    }

    @Override
    public double maxHeight(double width) {
        return MAX_SIZE;
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
        return MAX_SIZE;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {

        if (resizeTask != null)
            resizeTask.cancel();

        resizeTask = new TimerTask() {
            @Override
            public void run() {
                resizeAfter(width, height);
            }
        };

        timer.schedule(resizeTask, resizeDelay);
    }

    private void resizeAfter(double width, double height) {
        super.resize(width, height);
        super.setFitWidth(width);
        super.setFitHeight(height);
    }

    /**
     * Returns the delay time of resizing the canvas.
     *
     * @return the delay time of resizing the canvas.
     */
    public long getResizeDelay() {
        return resizeDelay;
    }

    /**
     * Delays resizing the JME viewport and thus reducing the continual resizes when a window is resized.
     *
     * @param resizeDelay the time in ms to delay a window redraw.
     */
    public void setResizeDelay(long resizeDelay) {
        this.resizeDelay = resizeDelay;
    }

}
