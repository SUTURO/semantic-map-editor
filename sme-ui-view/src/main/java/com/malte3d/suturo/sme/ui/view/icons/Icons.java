package com.malte3d.suturo.sme.ui.view.icons;

import com.malte3d.suturo.commons.messages.Messages;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
@UtilityClass
public class Icons {

    public static final Image APP_ICON = new Image(Objects.requireNonNull(Icons.class.getResourceAsStream("semantic-map-editor-app-icon-128.png")));

    public static final Image TOOLBAR_MOVE = svgToImage("move.svg");
    public static final Image TOOLBAR_ROTATE = svgToImage("rotate.svg");
    public static final Image TOOLBAR_SCALE = svgToImage("scale.svg");

    public static final Image TOOLBAR_NULL = svgToImage("stack.svg");
    public static final Image TOOLBAR_BOX = svgToImage("box.svg");
    public static final Image TOOLBAR_SPHERE = svgToImage("circle.svg");
    public static final Image TOOLBAR_CYLINDER = svgToImage("cylinder.svg");
    public static final Image TOOLBAR_PLANE = svgToImage("plane.svg");

    private static Image svgToImage(@NonNull String path) {

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

        try (InputStream file = Icons.class.getResourceAsStream(path)) {

            TranscoderInput input = new TranscoderInput(file);

            try {

                transcoder.transcode(input, null);

                return SwingFXUtils.toFXImage(transcoder.getImg(), null);

            } catch (TranscoderException e) {
                log.error(Messages.format("Error while converting svg image {}", path), e);
                throw new IllegalStateException(e);
            }

        } catch (IOException e) {
            log.error(Messages.format("Error while loading svg image {}", path), e);
            throw new IllegalStateException(e);
        }
    }

}
