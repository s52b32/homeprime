package homeprime;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import homeprime.core.exception.ThingException;

public class FileStoreUtils {

    public static Boolean storeFile(MultipartFile file, String destinationPath, String destinationFilename)
            throws ThingException {
        String destinationFullPath = "";
        // If destination path is not provided (null) use app root
        if (destinationPath != null) {
            destinationFullPath = destinationPath + File.separator;
        }
        final File dirObj = new File(destinationFullPath);
        // create a directory if it doesn't exist
        if (!dirObj.exists()) {
            dirObj.mkdirs();
        }
        // If file name is not provided (null) use original file name
        if (destinationFilename == null) {
            destinationFullPath = destinationFullPath + StringUtils.cleanPath(file.getOriginalFilename());
        } else {
            destinationFullPath = destinationFullPath + destinationFilename;
        }

        try {
            // Copy file to the target location. Replace if same file already exists.
            Files.copy(file.getInputStream(), Paths.get(destinationFullPath).normalize(),
                    StandardCopyOption.REPLACE_EXISTING);

            return true;
        } catch (IOException ex) {
            throw new ThingException("Could not store file " + destinationFullPath + ". Please try again!", ex);
        }
    }

    public static Resource loadFileAsResource(String fileName) throws ThingException {
        try {
            Path filePath = Paths.get(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ThingException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ThingException("File not found " + fileName, ex);
        }
    }

    public static List<String> findFileMatchesInDirectory(final String pattern, final File folder) {
        final List<String> imageFileNames = new ArrayList<>();
        for (final File f : folder.listFiles()) {
            if (f.isDirectory()) {
                // do not look deeper, only parent directory
                continue;
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    imageFileNames.add(f.getAbsolutePath());
                }
            }
        }
        return imageFileNames;
    }

}