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
public class IOTPayLoadConstants {

	// For logging properties
	public final static String COMMAND_ON = "COMMAND_ON";
	public final static String RETURN_VALUE = "RETURN_VALUE";
	public final static String FILECOUNT_FOR_LOGGING = "LOGGING_FILEHANDLER_FILECOUNT";
	public final static String APPENDMODE_FOR_LOGGING = "LOGGING_FILEHANDLER_APPEND";
	public final static String FORMATTERNAME_FOR_LOGGING = "LOGGING_FILEHANDLER_FORMATTERNAME";
	public final static String LOGLEVEL_OF_LOGGING = "LOGGING_FILEHANDLER_LOGLEVEL";

	// Some commonly used string constants
	public final static String SUCCESS = "SUCCESS";
	public final static String ERROR = "ERROR";
	public final static String TRUE = "TRUE";
	public final static String FALSE = "FALSE";
	public final static String FAILURE = "FAILURE";
	public final static String ON = "ON";
	public final static String OFF = "OFF";
	public final static String UNKNOWN = "UNKNOWN";

	public final static String COMMA = ",";
	public final static String DELIMITER = "|";
	public final static String UNDERSCORE = "_";
	public final static String TXTFILE = ".TXT";

	public final static String LOGGING_FILEHANDLER_FILEBYTESLIMIT = "LOGGING_FILEHANDLER_FILEBYTESLIMIT";

	public final static String POWER_ON = "poweron";
	public final static String POWER_OFF = "poweroff";
	public final static String POWER_STATUS = "powerstatus";
	public final static String LIGHTOFF = "lightoff";
	public final static String LIGHT_HALF_OFF = "light_half_on";
	public final static String LIGHTON = "lighton";
	public final static String LIGHTSTATUS = "lightstatus";
	public final static String MODELSTATUS = "modelstatus";
	public final static String CHECKHUMIDITYSTATUS = "ckhumidity";
	public final static String INTERNETSTATUS = "internet";
	public final static String CHECKTEMPORATURESTATUS = "cktempor";
	public final static String CHECKVOCSTATUS = "ckvoc";
	public final static String CHECKPMSTATUS = "ckpm";
	public final static String CHECKWATERSTATUS = "ckwater";
	public final static String HUMIDITYSTATUS = "humidity";
	public final static String ANION_STATUS = "anionstatus";
	public final static String CHILDLOCK_STATUS = "childlockstatus";
	public final static String TIME_STATUS = "timestatus";
	public final static String WIFI_STATUS = "wifistatus";
	public final static String FALL_STATUS = "fallstatus";

	public final static String IP = "IP";
	public final static String PORT = "PORT";
	public final static String CLIENTID = "CLIENTID";
	public final static String TOPIC = "TOPIC";
	public final static String CONTROL_QOS = "CONTROL_QOS";
	public final static String REGISTER_QOS = "REGISTER_QOS";
	public final static String CONNECTION_TIME_OUT = "ConnectionTimeout";
	public final static String KEEPALIVE = "KEEPALIVE";
	public final static String INSPECT_DATA_QOS = "INSPECT_DATA_QOS";
	public final static String JSON_ROOT_SWITCH = "JSON_ROOT_SWITCH";
	public final static String JSON_SWITCH_STATUS = "JSON_SWITCH_STATUS";
	public final static String JSON_SWITCH_MACID = "JSON_SWITCH_MACID";
	public final static String JSON_SWITCH_STATUS_ON = "JSON_SWITCH_STATUS_ON";
	public final static String JSON_SWITCH_STATUS_OFF = "JSON_SWITCH_STATUS_OFF";
	public final static String JSON_SWITCH_STATUS_HALFON = "JSON_SWITCH_STATUS_HALFON";
	
	
	/**command no value **/
	public final static String HEX_PREFIX = "0x";
	public final static String COMMAND_FRAME_HEAD = "55";
	public final static String COMMAND_FRAME_END = "aa";
	public final static String POWER_CONTROL_KEY = "1";
	public final static String ANION_CONTROL_KEY = "2";
	public final static String CHILDLOCK_CONTROL_KEY = "3";
	public final static String HUMIDITY_CONTROL_KEY = "4";
	public final static String MODELCONTROL_KEY = "5";
	public final static String LIGHTCONTROL_KEY = "6";
	public final static String TIMECONTROL_KEY = "7";
	public final static String FILTERTYPEONECONTROL_KEY = "8";
	public final static String HUMIDITYCONTROL_KEY = "9";
	public final static String TEMPORATURECONTROL_KEY = "10";
	public final static String VOCCONTROL_KEY = "11";
	public final static String PMCONTROL_KEY = "12";
	public final static String WIFICONTROL_KEY = "13";
	public final static String WATERCONTROL_KEY = "14";
	public final static String FALLRCONTROL_KEY = "15";
	public final static String FILTERTYPETWOCONTROL_KEY = "16";
	public final static String FILTERTYPETHREECONTROL_KEY = "17";
	public final static String AQI_CONTROL_KEY ="19";
	public final static String QUIETMODECONTROL_KEY="20";
	public final static String DEVICE_ZERO = "0";
	public final static String DEVICE_ONE = "1";
	public final static String DEVICE_TWO = "2";
	public final static String DEVICE_THREE = "3";
	public final static String DEVICE_FOUR = "4";
	public final static String DEVICE_FIVE = "5";
	public final static String DEVICE_SIX = "6";
	public final static String DEVICE_FE = "65535";
	public final static String DEVICE_FD = "65534";

	public final static String DEVICE_ERROR = "DEVICEERROR";
	public final static String DEVICE_NOTREADY = "DEVICENOTREADY";
	
	/**key to get the control value **/
	public final static String CONTROL_POWERON="POWERON";
	public final static String CONTROL_POWEROFF="POWEROFF";
	public final static String CONTROL_LIGHTOFF="LIGHTOFF";
	public final static String CONTROL_LIGHTHALFON="LIGHTHALFON";
	public final static String CONTROL_LIGHTON="LIGHTON";
	public final static String CONTROL_RUNMODELSTOP="RUNMODELSTOP";
	public final static String CONTROL_RUNMODELAUTO="RUNMODELAUTO";
	public final static String CONTROL_RUNMODELSLEEP="RUNMODELSLEEP";
	public final static String CONTROL_RUNMODELSTRONG="RUNMODELSTRONG";
	public final static String CONTROL_RUNMODELHANDONE="RUNMODELHANDONE";
	public final static String CONTROL_RUNMODELHANDTWO="RUNMODELHANDTWO";
	public final static String CONTROL_RUNMODELHANDTHREE="RUNMODELHANDTHREE";
	public final static String CONTROL_TIMENO="TIMENO";
	public final static String CONTROL_INTERNET="INTERNET";
	public final static String CONTROL_ANIONON="ANIONON";
	public final static String CONTROL_ANIONOFF="ANIONOFF";
	public final static String CONTROL_CHILDLOCKON="CHILDLOCKON";
	public final static String CONTROL_CHILDLOCKOFF="CHILDLOCKOFF";
	public final static String CONTROL_HUMIDIFYAUTO_ON="HUMIDIFYAUTOON";
	public final static String CONTROL_HUMIDIFYAUTO_OFF="HUMIDIFYAUTOOFF";
	public final static String CONTROL_HUMIDIFY_CONTINUATION_ON="HUMIDIFYCONTINUATIONON";
	public final static String CONTROL_HUMIDIFYOFF="HUMIDIFYOFF";
	public final static String CONTROL_WATRELEVELON="WATRELEVELON";
	public final static String CONTROL_WATRELEVELOFF="WATRELEVELOFF";
	public final static String CONTROL_HUMIDIFYAUTO="HUMIDIFYAUTO";
	public final static String CONTROL_WIFINETWORK="WIFINETWORK";
	public final static String CONTROL_WIFIBROKENETWORK="WIFIBROKENETWORK";
	public final static String CONTROL_DEVICE_FALL="DEVICEFALL";
	public final static String CONTROL_DEVICE_NOT_FALL="DEVICENOTFALL";
	public final static String CONTROL_QUIETMODEON="QUIETMODEON";
	public final static String CONTROL_QUIETMODEOFF="QUIETMODEOFF";
	
	/**bean value to platform **/
	public final static String RESTFUL_ANION_ON="anionon";
	public final static String RESTFUL_ANION_OFF="anionoff";
	
	public final static String RESTFUL_CHILELOCK_ON="childlockon";
	public final static String RESTFUL_CHILELOCK_OFF="childlockoff";
	
	public final static String RESTFUL_FILTER_ONE="filterone";
	public final static String RESTFUL_FILTER_TWO="filtertwo";
	public final static String RESTFUL_FILTER_THREE="filterthree";
	
	public final static String RESTFUL_HUMIDIFY_CONTINUATION_ON="humidifycontinuationon";
	public final static String RESTFUL_HUMIDIFY_OFF="humidifyoff";
	public final static String RESTFUL_HUMIDIFY_AUTO_ON="humidifyautoon";
	public final static String RESTFUL_HUMIDIFY_AUTO_OFF="humidifyautooff";
	
	public final static String RESTFUL_LIGHT_ON="lighton";
	public final static String RESTFUL_LIGHT_OFF="lightoff";
	public final static String RESTFUL_LIGHT_HALF_ON="lighthalfon";
	
	public final static String RESTFUL_MODE_STOP="stop";
	public final static String RESTFUL_MODE_AUTO="auto";
	public final static String RESTFUL_MODE_SLEEP="sleep";
	public final static String RESTFUL_MODE_STRONG="strong";
	public final static String RESTFUL_MODE_ONE="one";
	public final static String RESTFUL_MODE_TWO="two";
	public final static String RESTFUL_MODE_THREE="three";
	public final static String RESTFUL_MODE_ERROR="motoerror";
	
	public final static String RESTFUL_POWER_ON="poweron";
	public final static String RESTFUL_POWER_OFF="poweroff";
	
	public final static String RESTFUL_QUIET_MODE_ON="quietmodeon";
	public final static String RESTFUL_QUIET_MODE_OFF="quietmodeoff";
	
}
