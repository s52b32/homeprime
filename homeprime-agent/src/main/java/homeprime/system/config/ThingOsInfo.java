package homeprime.system.config;

import homeprime.core.exceptions.ThingException;

/**
 * Interface describing thing operating system information.
 * 
 * @author Milan Ramljak
 * 
 */
public interface ThingOsInfo {

    public String getOsName() throws ThingException;

    public String getOsVersion() throws ThingException;

    public String getOsArchitecture() throws ThingException;

    public String getOsFirmwareBuild() throws ThingException;

    public String getOsFirmwareDate() throws ThingException;

}
