package homeprime.rest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import homeprime.FileStoreUtils;
import homeprime.core.commander.CmdResponse;
import homeprime.core.commander.LocalCmdExecutionFactory;
import homeprime.core.exception.ThingException;
import homeprime.core.model.readers.config.ConfigurationReader;
import homeprime.core.properties.ThingProperties;
import homeprime.file.pojos.UploadData;
import homeprime.image.ImageUtils;
import homeprime.image.pojos.Image;
import homeprime.image.pojos.Images;

/**
 * Spring REST controller for thing info definition.
 *
 * @author Milan Ramljak
 */
@RestController
public class ImagesController {

    /**
     * Set active image running the agent application.
     *
     * @return 200 OK
     * @throws ThingException
     */
    @PostMapping("/Images/set-active")
    public ResponseEntity<?> setActiveImage(@RequestBody(required = true) Image image) throws ThingException {

        final String findActiveImageSymbolicLinkCmd = "ln -sf " + ThingProperties.APP_ROOT_PATH + image.getName() + " "
                + ThingProperties.APP_ROOT_PATH + "homeprime-agent-active";
        if (!new File(ThingProperties.APP_ROOT_PATH + image.getName()).exists()) {
            return new ResponseEntity<String>("Image cannot be set as active if it does not exist",
                    HttpStatus.NOT_FOUND);
        }
        final CmdResponse cmdResponse = LocalCmdExecutionFactory.getLocalSession()
                .execute(findActiveImageSymbolicLinkCmd);
        if (cmdResponse.getExitCode() == 0) {
            return new ResponseEntity<String>("Image set to active successfully", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Failed to set active image", HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("/Images/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestPart(required = false) UploadData uploadData) {
        try {
            Boolean storeFileResult;
            if (uploadData != null) {
                storeFileResult = FileStoreUtils.storeFile(file, uploadData.getDestinationDir(),
                        uploadData.getDestinationFileName());
            } else {
                storeFileResult = FileStoreUtils.storeFile(file, ThingProperties.APP_ROOT_PATH, null);
            }
            if (!storeFileResult) {
                // failed to upload image
                return new ResponseEntity<Boolean>(storeFileResult, HttpStatus.EXPECTATION_FAILED);
            }
            return new ResponseEntity<Boolean>(storeFileResult, HttpStatus.OK);
        } catch (ThingException e) {
            return new ResponseEntity<String>("Failed to store file to local file system",
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/Images")
    public ResponseEntity<?> getAvailableImages() throws ThingException {
        // look up for certain files
        final List<String> imageFileNames = FileStoreUtils.findFileMatchesInDirectory("homeprime-agent-.*",
                new File(ThingProperties.APP_ROOT_PATH));
        String thingUuid = ConfigurationReader.getConfiguration().getUuid();
        final Images images = ImageUtils.createImages(thingUuid, imageFileNames);
        return new ResponseEntity<Images>(images, HttpStatus.OK);
    }

    @GetMapping("/Images/download")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
        final String activeImageName = "homeprime-agent-active";
        // Load file as Resource
        Resource resource;
        // Try to determine file's content type
        String contentType = null;
        try {
            resource = FileStoreUtils.loadFileAsResource(ThingProperties.APP_ROOT_PATH + activeImageName);
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                System.out.println("Could not determine file type.");
            }
            // Fallback to the default content type if type could not be determined
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + activeImageName + "\"")
                    .body(resource);
        } catch (ThingException e) {
            throw new RuntimeException("Failed to download file: " + activeImageName);
        }

    }

}
