package com.example.admin.myapplication.camera2;

import android.media.Image;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * create by yx
 * on 2020/6/1
 */
public class Camera2Helper {

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    public SparseIntArray ORIENTATIONS = new SparseIntArray();

    public Camera2Helper() {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    public Size getOptimalSize(Size[] supportedSizes, int maxWidth, int maxHeight) {
        float aspectRatio = maxWidth * 1.0F / maxHeight;
        if (supportedSizes != null) {
            for (Size size : supportedSizes) {
                if (size.getWidth() * 1.0F / size.getHeight() == aspectRatio && size.getHeight() <= maxHeight && size.getWidth() <= maxWidth) {
                    return size;
                }
            }
        }
        return null;
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
   public static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    public static class ImageSaver implements Runnable {
        /**
         * The JPEG image
         */
        private Image image;

        /**
         * The file we save the image into.
         */
        private File file;

        public ImageSaver(Image image, File file) {
            this.image = image;
            this.file = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                output = new FileOutputStream(file);
                output.write(bytes);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                image.close();
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
