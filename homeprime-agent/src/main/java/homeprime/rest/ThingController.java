package homeprime.rest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeprime.core.system.ThingInfo;

/**
 * Spring REST controller for thing info definition.
 * 
 * @author Milan Ramljak
 */
@RestController
public class ThingController implements ErrorController {

    @RequestMapping("/")
    public ThingInfo index() {
        return new ThingInfo();
    }

    public String getErrorPath() {
        return "/error";
    }

}
