package homeprime.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import homeprime.image.pojos.Image;
import homeprime.image.pojos.Images;

public class ImageUtils {

    public static Image createImage(String imageFileName) {
        final Image image = new Image();
        final File tmpFile = new File(imageFileName);
        if (imageFileName.contains("active")) {
            image.setActive(true);
        }
        try {
            final Path tmpPath = tmpFile.toPath().toRealPath();
            image.setName(tmpPath.getFileName().toString());
        } catch (IOException e) {
            image.setName(imageFileName);
        }
        // at this point we already know real file name (not symbolic link)
        image.setVersion(extractImageVersion(image.getName()));
        return image;
    }

    public static Images createImages(String owner, List<String> imageFileNames) {
        final Images images = new Images(owner);
        // go through all the files and create Image object describing them
        for (String imageFileName : imageFileNames) {
            final Image image = createImage(imageFileName);
            // add image info to list of Images object
            images.getImages().add(image);
        }
        return images;
    }

    public static String extractImageVersion(String imageFileName) {
        // homeprime-agent-R2A01.jar
        return imageFileName.replace("homeprime-agent-", "").replace(".jar", "");

    }

}
