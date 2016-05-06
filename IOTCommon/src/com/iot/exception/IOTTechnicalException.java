 /**
 * Licensed Materials - Property of Pachila
 *
 */
package com.iot.exception;

/**
 * This class handles Custom Technical Exception
 * 
 * @author Pachila
 * 
 */
public class IOTTechnicalException extends IOTException {
    
    /** serialVersionUID of the class */
    public static final long serialVersionUID = 0;

    /**
     * @param errorCode
     */
    public IOTTechnicalException(int errorCode) {
        super(errorCode);
    }
}
