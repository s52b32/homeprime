package homeprime.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This class generates a zip archive containing all the
 * files within provided path.
 */
public class ZipUtils {

    /**
     * This method creates the zip archive and then goes through
     * each file in the chosen directory, adding each one to the
     * archive. Note the use of the try with resource to avoid
     * any finally blocks.
     */
    public static boolean createZip(String dirName, String outputZipFileName) {
        boolean result = false;
        // the directory to be zipped
        Path directory = Paths.get(dirName);
        // the zip file name that we will create
        File zipFileName = Paths.get(outputZipFileName).toFile();
        // open the zip stream in a try resource block, no finally needed
        try (ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            addToZipFile(directory, zipStream);
            result = true;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    private static void addToZipFile(Path dirPath, ZipOutputStream zipStream) throws IOException {
        if (dirPath.toFile().isDirectory()) {
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath);
            for (Path dirPathDepth : dirStream) {
                addToZipFile(dirPathDepth, zipStream);
            }
        } else {
            // add individual file
            addFileToZip(dirPath, zipStream);
        }
    }

    private static void addFileToZip(Path file, ZipOutputStream zipStream) throws IOException {

        try {
            String inputFileName = file.toFile().getPath();
            FileInputStream inputStream = new FileInputStream(inputFileName);

            // create a new ZipEntry, which is basically another file
            // within the archive. We omit the path from the filename
            ZipEntry entry = new ZipEntry(inputFileName);
            entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            entry.setComment("HomePrime");
            zipStream.putNextEntry(entry);

            byte[] readBuffer = new byte[2048];
            int amountRead;
            @SuppressWarnings("unused")
            int written = 0;

            while ((amountRead = inputStream.read(readBuffer)) > 0) {
                zipStream.write(readBuffer, 0, amountRead);
                written += amountRead;
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}