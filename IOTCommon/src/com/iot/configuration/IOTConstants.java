/**
 * Licensed Materials - Property of Pachila
 * 
 * 
 */
package com.iot.configuration;

/**
 * 
 * 
 * @author Pachila
 * 
 */
public class IOTConstants {
    
    // For logging properties
    public final static String FILEPATTERN_FOR_LOGGING = "LOGGING_FILEHANDLER_FILEPATTERN";
    public final static String FILEBYTESLIMIT_FOR_LOGGING = "LOGGING_FILEHANDLER_FILEBYTESLIMIT";
    public final static String FILECOUNT_FOR_LOGGING = "LOGGING_FILEHANDLER_FILECOUNT";
    public final static String APPENDMODE_FOR_LOGGING = "LOGGING_FILEHANDLER_APPEND";
    public final static String FORMATTERNAME_FOR_LOGGING = "LOGGING_FILEHANDLER_FORMATTERNAME";
    public final static String LOGLEVEL_OF_LOGGING = "LOGGING_FILEHANDLER_LOGLEVEL";
    public final static String LOGGING_FILEHANDLER_LOG_TARGET_TYPE = "LOGGING_FILEHANDLER_LOG_TARGET_TYPE";
    public final static String INIT_ERROR_MSG = "INIT_ERROR_MSG";
    public final static String ACTION_MSG = "ACTION_MSG";
    public final static String ASYNC_CALL_FINISH = "ASYNC_CALL_FINISH";
    public final static String SERVICE_CALL_FAIL="SERVICE_CALL_FAIL";
    public final static String DEVICE_ONLINE="online";
    public final static String DEVICE_OFFLINE="offline";
    // Some commonly used string constants
    public final static String SUCCESS = "SUCCESS";
    public final static String ERROR = "ERROR";
    public final static String TRUE = "TRUE";
    public final static String FALSE = "FALSE";
    public final static String FAILURE = "FAILURE";
    public final static String ON = "ON";
    public final static String OFF = "OFF";
    public final static String UNKNOWN = "UNKNOWN";
    public final static String LOG_TARGET_FILE = "FILE";
    public final static String LOG_TARGET_CONSOLE = "CONSOLE";
    
    public final static String COMMA = ",";
    public final static String DELIMITER = "|";
    public final static String UNDERSCORE = "_";
    public final static String TXTFILE = ".TXT";
    
    public final static String LOGGING_FILEHANDLER_FILEBYTESLIMIT = "LOGGING_FILEHANDLER_FILEBYTESLIMIT";

    //start
    /**MQTT RELATED KEY**/
    public final static String MQTT_IP = "MQTT_IP";
    public final static String MQTT_PORT="MQTT_PORT";
    public final static String MQTT_UPSTREAM_CLIENTID="MQTT_UPSTREAM_CLIENTID";
    public final static String MQTT_DOWNSTREAM_CLIENTID="MQTT_DOWNSTREAM_CLIENTID";
    public final static String MQTT_DOWNSTREAM_TOPIC="MQTT_DOWNSTREAM_TOPIC";
    public final static String MQTT_DOWNSTREAM_SMARTLIGHT_TOPIC="MQTT_DOWNSTREAM_SMARTLIGHT_TOPIC";
    public final static String MQTT_REGISTER_TOPIC="MQTT_REGISTER_TOPIC";
    public final static String MQTT_DISREGISTER_TOPIC="MQTT_DISREGISTER_TOPIC";
    public final static String MQTT_UPSTREAM_TOPIC="MQTT_UPSTREAM_TOPIC";
    public final static String MQTT_USERNAME="MQTT_USERNAME";
    public final static String MQTT_PASSWORD="MQTT_PASSWORD";
    public final static String IOT_ROOT_URL="IOT_ROOT_URL";
    public final static String MQTT_OTA_TOPIC="MQTT_OTA_TOPIC";
    public final static String MQTT_IS_AUTH_CONNECT="MQTT_IS_AUTH_CONNECT";
    /**KEY FOR MQTT CONNECT USE USER/PASSWORD OR NOT**/
    public final static String MQTT_AUTH_CONNECT_KEY="MQTT_AUTH_CONNECT_KEY";
    public final static String MQTT_NOT_AUTH_CONNECT_KEY="MQTT_NOT_AUTH_CONNECT_KEY";
    
    public final static String MQTT_CONTROL_QOS="MQTT_CONTROL_QOS";
    public final static String MQTT_REGISTER_QOS="MQTT_REGISTER_QOS";
    public final static String INSPECT_DATA_QOS="INSPECT_DATA_QOS";
    public final static String ConnectionTimeout="ConnectionTimeout";
    public final static String KEEPALIVE="KEEPALIVE";
    public final static String METHOD="METHOD";
    public final static String SERVICE_IP="SERVICE_IP";
    public final static String SERVICE_PORT="SERVICE_PORT";
    public final static String SERVICE_URI="SERVICE_URI";
    public final static String SERVICE_CONTROL_LIGHT="SERVICE_CONTROL_LIGHT";
    public final static String SERVICE_CONTROL_POWER="SERVICE_CONTROL_POWER";
    public final static String HTTP_METHOD_POST="HTTP_METHOD_POST";
    public final static String CONTENT_TYPE_JSON="CONTENT_TYPE_JSON";
    public final static String CallBACKTYPE_POWER="CallBACKTYPE_POWER";
    public final static String CallBACKTYPE_LIGHT="CallBACKTYPE_LIGHT";
    public final static String CONTROL_STATUS="CONTROL_STATUS";
    public final static String SENSOR_STATUS="SENSOR_STATUS";
    
    

    public final static String CONNECTION_TIME_OUT="ConnectionTimeout";

    public final static String JSON_ROOT_SWITCH="JSON_ROOT_SWITCH";
    public final static String JSON_SWITCH_STATUS="JSON_SWITCH_STATUS";
    public final static String JSON_SWITCH_MACID="JSON_SWITCH_MACID";
    public final static String JSON_SWITCH_STATUS_ON="JSON_SWITCH_STATUS_ON";
    public final static String JSON_SWITCH_STATUS_OFF="JSON_SWITCH_STATUS_OFF";
    public final static String JSON_SWITCH_STATUS_HALFON="JSON_SWITCH_STATUS_HALFON";
    public final static String NUMBER="NUMBER"; 
    public final static String TIMEMIN="TIMEMIN";
    public final static String TIMEMAX="TIMEMAX";

    public final static String POWER_STATUS = "POWERSTATUS";
    public final static String POWER_ON = "POWERON";
    public final static String POWER_OFF = "POWEROFF";
    public final static String LIGHT_OFF ="LIGHTOFF";
    public final static String LIGHT_HALF_ON ="LIGHTHALFON";
    public final static String LIGHT_ON ="LIGHTON";
    public final static String LIGHTSTATUS="LIGHTSTATUS";
    public final static String RUNMODELAUTO="RUNMODELAUTO";
    public final static String RUNMODELSLEEP="RUNMODELSLEEP";
    public final static String RUNMODELSTRONG="RUNMODELSTRONG";
    public final static String RUNMODELHANDONE="RUNMODELHANDONE";
    public final static String RUNMODELHANDTWO="RUNMODELHANDTWO";
    public final static String RUNMODELHANDTHREE="RUNMODELHANDTHREE";
    public final static String RUNMODELSTATUS="RUNMODELSTATUS";
    public final static String TIMENO="TIMENO";
    public final static String TIMEYES="TIMEYES";
    public final static String TIMESTATUS="TIMESTATUS";
    public final static String FILTER="FILTER";
    public final static String FILTERSTATUS="FILTERSTATUS";
    public final static String FILTERTWO="FILTERTWO";
    public final static String FILTERTWOSTATUS="FILTERTWOSTATUS";
    public final static String FILTERTHIRD="FILTERTHIRD";
    public final static String FILTERTHIRDSTATUS="FILTERTHIRDSTATUS";
    public final static String ANIONSWITCHON="ANIONON";
    public final static String ANIONOFF="ANIONOFF";
    public final static String ANIONSTATUS="ANIONSTATUS";
    public final static String CHILDLOCKSTATUS="CHILDLOCKSTATUS";
    public final static String CHILDON="CHILDLOCKON";
    public final static String CHILDOFF="CHILDLOCKOFF";
    public final static String HUMIDIFYCONTINUATION="HUMIDIFYCONTINUATION";
    public final static String HUMIDIFYONFF="HUMIDIFYOFF";
    public final static String HUMIDIFYSTATUS="HUMIDIFYSTATUS";
    public final static String HUMIDIFYAUTO="HUMIDIFYAUTO";
    public final static String WIFINETWORK= "WIFINETWORK";
    public final static String WIFIBROKENETWORK= "WIFIBROKENETWORK";
    public final static String CHECKHUMIDIFYSTATUS= "CHECKHUMIDIFYSTATUS";
    public final static String CHECKTEMPERASTATUS= "CHECKTEMPERASTATUS";
    public final static String CHECKVOCSTATUS= "CHECKVOCSTATUS";
    public final static String CHECKPMSTATUS= "CHECKPMSTATUS";
    public final static String CHECKWIFISTATUS= "CHECKWIFISTATUS";
    public final static String CHECKWATERSTATUS= "CHECKWATERSTATUS";
    public final static String CHECKFALLSTATUS= "CHECKFALLSTATUS";
    public final static String CHECKALLSTATUS= "CHECKALLSTATUS";
    public final static String CHECKAQISTATUS="CHECKAQISTATUS";
    public final static String QUIETMODEON="QUIETMODEON";
    public final static String QUIETMODEOFF="QUIETMODEOFF";
    public final static String QUIETMODESTATUS="QUIETMODESTATUS";
    public final static String MESSAGEPREFIX="0X";
    
    
    /**device registet and disregister relatedstatus**/
    public final static String DEVICE_REGISTER_SUCCESS="SUCCESS";
    public final static String DEVICE_REGISTER_FAIL="FAIL";
    //command to send device to dis-register
    public final static String DEVICE_DISREGISTER="DISREGISTER";
    public final static String DEVICE_DISREGISTER_SUCCESS="SUCCESS";
    public final static String DEVICE_DISREGISTER_FAIL="FAIL";
    public final static String DEVICE_DEFAULE_MANUFACTURER="0";
}

