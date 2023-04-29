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

@Slf4j
public abstract class SimpleJfxApplication extends SimpleApplication {

    private final Thread jmeThread;

    private EditorFxImageView imageView;

    private boolean started = false;
    protected boolean initialized = false;

    public SimpleJfxApplication(AppState... initialStates) {
        
        super(initialStates);

        jmeThread = Thread.currentThread();

        AppSettings settings = new AppSettings(true);

        settings.setCustomRenderer(JmeOffscreenSurfaceContext.class);
        settings.setResizable(true);

        setSettings(settings);

        createCanvas();

    }

    public void start() {
        JmeOffscreenSurfaceContext canvasContext = (JmeOffscreenSurfaceContext) getContext();
        canvasContext.setApplication(this);
        canvasContext.setSystemListener(this);
        startCanvas(true);
    }

    private void initJavaFxImage() {

        imageView = new EditorFxImageView();
        imageView.getProperties().put(JfxMouseInput.PROP_USE_LOCAL_COORDS, true);
        imageView.setFocusTraversable(true);

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

        log.info("jMonkeyEngine Started.");

        initApp();
    }

    public EditorFxImageView getImageView() {
        return imageView;
    }

    /**
     * Indicates that the first of two phases of the engine startup sequence is complete. It has started.
     * The engine is ready for input, but has not yet been initialized.
     * You must set initialized = true in your initApp method.
     *
     * @return whether the engine has started.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Indicates that the second of two phases are complete. The engine is initialized.
     * The engine is waiting for the class to set initialized = true.
     * This usually occurs in the users initApp() method.
     *
     * @return whether the engine is initialized.
     */
    public boolean isInitialized() {
        return initialized;
    }

    public boolean isJmeThread() {
        return Thread.currentThread() == jmeThread;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public abstract void initApp();

}
