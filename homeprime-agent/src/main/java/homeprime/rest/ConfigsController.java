package homeprime.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.FileStoreUtils;
import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.properties.ThingProperties;
import homeprime.core.utils.ZipUtils;
import homeprime.file.pojos.FileInfo;
import homeprime.file.pojos.FileList;

/**
 * Spring REST controller for thing configs operations.
 *
 * @author Milan Ramljak
 */
@RestController
public class ConfigsController {

    @GetMapping("/Configs")
    public ResponseEntity<?> getAvailableConfigs() {
        final FileList imageList = new FileList();
        // look up for certain file extensions (json)
        final List<String> configFileNames = FileStoreUtils.findFileMatchesInDirectory(".*\\.json",
                new File(ThingProperties.getInstance().getConfigsRootPath()));
        final List<FileInfo> configFileInfos = createFileInfos(configFileNames);
        // link list of FileInfo objects to FileList object holder
        imageList.setFileList(configFileInfos);
        return new ResponseEntity<FileList>(imageList, HttpStatus.OK);

    }

    @GetMapping("/Configs/backup")
    public ResponseEntity<?> createConfigurationBackup(HttpServletRequest request) {
        final String tmpBackupZipFileName = "configs_backup.zip";
        if (!ZipUtils.createZip(ThingProperties.getInstance().getConfigsRootPath(), tmpBackupZipFileName)) {
            return new ResponseEntity<String>(
                    "Failed to create configuration backup of: " + ThingProperties.getInstance().getConfigsRootPath(),
                    HttpStatus.EXPECTATION_FAILED);
        }
        // Load file as Resource
        Resource resource;
        // Try to determine file's content type
        String contentType = "application/zip";
        try {
            resource = FileStoreUtils.loadFileAsResource(tmpBackupZipFileName);
            final String tmpDestinationFileName = ConfigurationReader.getConfiguration().getUuid() + "_"
                    + System.currentTimeMillis() + ".zip";
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tmpDestinationFileName + "\"")
                    .body(resource);
        } catch (ThingException e) {
            throw new RuntimeException("Failed to download file from: " + tmpBackupZipFileName);
        }
    }

    /**
     * Helper method to describe file by @FileInfo object.
     *
     * @param fileNames - list of file name strings
     * @return list of @FileInfo
     */
    private List<FileInfo> createFileInfos(List<String> fileNames) {
        final List<FileInfo> fileInfos = new ArrayList<>();
        // go through all the files and create ImageInfo object describing them
        for (String fileName : fileNames) {
            final FileInfo fileInfo = createFileInfo(fileName);
            // add image info to list of ImageInfo objects
            fileInfos.add(fileInfo);
        }
        return fileInfos;
    }

    private FileInfo createFileInfo(String fileName) {
        final FileInfo fileInfo = new FileInfo();
        final File tmpFile = new File(fileName);
        fileInfo.setFileName(tmpFile.getName());
        fileInfo.setFilePath(fileName);
        // get file size in B
        fileInfo.setFileSize(tmpFile.length());
        fileInfo.setCreationDate(tmpFile.lastModified());
        return fileInfo;

    }

}
