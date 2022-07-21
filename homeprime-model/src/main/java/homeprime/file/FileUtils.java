package homeprime.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import homeprime.file.pojos.FileInfo;

public class FileUtils {

    /**
     * Helper method to describe file by @FileInfo object.
     *
     * @param fileNames - list of file name strings
     * @return list of @FileInfo
     */
    public static List<FileInfo> createFileInfos(List<String> fileNames) {
        final List<FileInfo> fileInfos = new ArrayList<>();
        // go through all the files and create ImageInfo object describing them
        for (String fileName : fileNames) {
            final FileInfo fileInfo = createFileInfo(fileName);
            // add image info to list of ImageInfo objects
            fileInfos.add(fileInfo);
        }
        return fileInfos;
    }

    public static FileInfo createFileInfo(String fileName) {
        final FileInfo fileInfo = new FileInfo();
        final File tmpFile = new File(fileName);
        fileInfo.setFileName(tmpFile.getName());
        try {
            fileInfo.setFilePath(tmpFile.toPath().toRealPath().toString());
        } catch (IOException e) {
            fileInfo.setFilePath(tmpFile.getAbsolutePath());
        }
        // get file size in B
        fileInfo.setFileSize(tmpFile.length());
        fileInfo.setCreationDate(tmpFile.lastModified());
        return fileInfo;

    }

    public static boolean checkCreateDirectory(String dirPath) {
        final File dir = new File(dirPath);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

}
