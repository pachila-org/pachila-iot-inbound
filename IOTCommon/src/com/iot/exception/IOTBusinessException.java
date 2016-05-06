 /**
 * Licensed Materials - Property of Pachila
 */
package com.iot.exception;

/**
 * This class handles Custom Business Exception
 * 
 * @author Pachila
 * 
 */
public class IOTBusinessException extends IOTException {
    
    /** serialVersionUID of the class */
    public static final long serialVersionUID = 0;

    /**
     * @param errorCode
     */
    public IOTBusinessException(int errorCode) {
        super(errorCode);
    }

}
