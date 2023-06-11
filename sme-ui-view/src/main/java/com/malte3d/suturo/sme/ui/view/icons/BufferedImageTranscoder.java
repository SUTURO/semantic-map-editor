package com.malte3d.suturo.sme.ui.view.icons;

import lombok.Getter;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;

/**
 * Transcoder for converting svg images to buffered images.
 */
class BufferedImageTranscoder extends ImageTranscoder {

    @Getter
    private BufferedImage img;

    @Override
    public BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void writeImage(BufferedImage img, TranscoderOutput output) {
        this.img = img;
    }

}
