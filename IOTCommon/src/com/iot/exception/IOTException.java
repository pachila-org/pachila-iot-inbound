 /**
 * Licensed Materials - Property of 
 */
package com.iot.exception;

import java.util.ResourceBundle;

/**
 * This class handles Custom Exception
 * 
 * @author 
 * 
 */
public class IOTException extends Exception {
    
    /** serialVersionUID of the class */
    public static final long serialVersionUID = 0;
    
    static {
        localizedErrorMessages = ResourceBundle.getBundle("com.iot.exception.errors");
    }
    
    /** Bundle to hold localized error messages */
    private static ResourceBundle localizedErrorMessages;
    
    /** error code */
    private int errorCode;
    
    /** error message */
    
    /**
     * Get the error code
     * @return error code
     */
    public int getErrorCode() {
        return errorCode;
    }
    
    /**
     * Constructor
     * @param message message
     * @param errorCode error code
     */
    public IOTException(int errorCode) {
        super(getMsgForCode(errorCode));
        this.errorCode = errorCode;
    }
    
    /**
     * Get the localized message
     * @return localized message
     */
    public String getLocalizedMessage() {
        String strErrorCode = null;
        try {
            strErrorCode = Integer.toString(this.errorCode);
            return localizedErrorMessages.getString(strErrorCode);
        } catch (RuntimeException e) {
            return strErrorCode;
        }
    }
    
    /**
     * @param errorCode
     * @return
     */
    public static String getMsgForCode(int errorCode) {
        String strErrorCode = null;
        try {
            strErrorCode = Integer.toString(errorCode);
            return localizedErrorMessages.getString(strErrorCode);
        } catch (RuntimeException e) {
            return strErrorCode;
        }
    }
}
