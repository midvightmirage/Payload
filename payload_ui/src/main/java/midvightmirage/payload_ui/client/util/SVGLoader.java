package midvightmirage.payload_ui.client.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class SVGLoader {
    public static BufferedImage loadSVG(Path file, int width, int height) throws Exception {
        return loadSVG(Files.newInputStream(file), width, height);
    }

    public static BufferedImage loadSVG(InputStream input, int width, int height) throws Exception {
        TranscoderInput svgInput = new TranscoderInput(input);

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        transcoder.addTranscodingHint(
                PNGTranscoder.KEY_WIDTH,
                (float) width
        );
        transcoder.addTranscodingHint(
                PNGTranscoder.KEY_HEIGHT,
                (float) height
        );

        transcoder.transcode(svgInput, null);

        return transcoder.getBufferedImage();
    }

    public static NativeImage toNativeImage(BufferedImage image) {
        NativeImage nativeImage = new NativeImage(
                image.getWidth(),
                image.getHeight(),
                true
        );

        ByteBuffer buffer = nativeImage.getPixelBytes();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int argb = image.getRGB(x, y);

                buffer.put((byte)((argb >> 16) & 0xFF)); // R
                buffer.put((byte)((argb >> 8) & 0xFF));  // G
                buffer.put((byte)(argb & 0xFF));         // B
                buffer.put((byte)((argb >> 24) & 0xFF)); // A
            }
        }

        buffer.flip();
        return nativeImage;
    }

    public static Identifier registerImage(NativeImage image, Identifier id) {
        DynamicTexture texture = new DynamicTexture(id::getPath, image);
        Minecraft.getInstance()
                .getTextureManager()
                .register(id, texture);
        return id;
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {

        private BufferedImage image;

        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(
                    width,
                    height,
                    BufferedImage.TYPE_INT_ARGB
            );
        }

        @Override
        public void writeImage(BufferedImage img, TranscoderOutput output) {
            image = img;
        }

        public BufferedImage getBufferedImage() {
            return image;
        }
    }
}
