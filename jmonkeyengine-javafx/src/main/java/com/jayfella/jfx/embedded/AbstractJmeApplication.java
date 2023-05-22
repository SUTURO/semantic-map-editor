package com.jayfella.jfx.embedded;

import com.jayfella.jfx.embedded.jfx.EditorFxImageView;
import com.jayfella.jfx.embedded.jfx.FrameTransferSceneProcessor;
import com.jayfella.jfx.embedded.jfx.ImageViewFrameTransferSceneProcessor;
import com.jayfella.jfx.embedded.jfx.JfxMouseInput;
import com.jayfella.jfx.embedded.jme.JmeOffscreenSurfaceContext;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public abstract class AbstractJmeApplication extends SimpleApplication {

    private final Thread jmeThread;

    private EditorFxImageView imageView;

    private boolean started = false;
    private final CountDownLatch initializedLatch = new CountDownLatch(1);

    public AbstractJmeApplication(AppState... initialStates) {

        super(initialStates);

        jmeThread = Thread.currentThread();

        AppSettings settings = new AppSettings(true);

        settings.setCustomRenderer(JmeOffscreenSurfaceContext.class);
        settings.setResizable(true);

        setSettings(settings);

        createCanvas();
    }

    /**
     * Starts the JME application and initializes the JavaFX image view.
     *
     * @return A CountDownLatch that will count down when the application has been initialized.
     */
    public CountDownLatch completeInitialization() {

        JmeOffscreenSurfaceContext canvasContext = (JmeOffscreenSurfaceContext) getContext();
        canvasContext.setApplication(this);
        canvasContext.setSystemListener(this);
        startCanvas(true);

        return initializedLatch;
    }

    private void initJavaFxImage() {

        imageView = new EditorFxImageView();
        imageView.getProperties().put(JfxMouseInput.PROP_USE_LOCAL_COORDS, true);

        imageView.setFocusTraversable(true);
        imageView.requestFocus();

        List<ViewPort> vps = renderManager.getPostViews();
        ViewPort last = vps.get(vps.size() - 1);

        ImageViewFrameTransferSceneProcessor sceneProcessor = new ImageViewFrameTransferSceneProcessor();
        sceneProcessor.bind(imageView, this, last);
        sceneProcessor.setEnabled(true);

        sceneProcessor.setTransferMode(FrameTransferSceneProcessor.TransferMode.ON_CHANGES);
    }

    @Override
    public void simpleInitApp() {

        initJavaFxImage();

        viewPort.setBackgroundColor(ColorRGBA.Black);

        started = true;

        initApp();

        initializedLatch.countDown();
    }

    public EditorFxImageView getImageView() {
        return imageView;
    }

    /**
     * Indicates that the first of two phases of the engine startup sequence is complete. It has started.
     * The engine is ready for input, but has not yet been initialized.
     *
     * @return whether the engine has started.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Indicates that the second of two phases are complete. The engine is initialized.
     *
     * @return whether the engine is initialized.
     */
    public boolean isInitialized() {
        return initializedLatch.getCount() == 0;
    }

    public boolean isJmeThread() {
        return Thread.currentThread() == jmeThread;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public abstract void initApp();

}
