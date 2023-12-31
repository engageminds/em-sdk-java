package ai.engageminds.sdk.dto;

import ai.engageminds.sdk.EmSdk;
import lombok.Data;

import java.util.Map;

@Data
public class CommonRequest {
    private final int sdk = 3;          // 当前SDK类型, 0:iOS,1:Android,2:JS,3:ServerJava,4:ServerGo,5:ServerPython
    private final String sdkv = EmSdk.VERSION;

    private long ts;                    // Client time in milliseconds
    private String rid;                 // request id
    private long fit;                   // Client first install App time, in seconds
    private long flt;                   // The first time the client opens the App, in seconds (uid file creation time)
    private int zo;                     // Time zone offset in minutes. For example, Beijing time zone Zo = 480
    private String tz;                  // Time zone name
    private String session;             // Session ID, UUID generated when the app is initialized
    private String uid;                 // User ID, the unique user ID generated by the SDK
    private String ssid;                // Standardized Serviceld, generated by em-server
    private String gaid;                // Andriod GAID
    private String idfa;                // iOS IDFA
    private String idfv;                // iOS IDFV
    private String oaid;                // OAID
    private int dtype;                  // device_type, 0:unknown,1:phone,2:tablet,3:tv
    private String lang;                // language code
    private int jb;                     // jailbreak status, 0: normal, 1: jailbreak, no transmission during normal
    private String bundle;              // The current app package name
    private String make;                // device maker
    private String brand;               // device brand
    private String model;               // device model
    private int os;                     // 操作系统, 0:iOS,1:Android,2:HarmonyOS,3:Mac,4:Windows,5:Linux
    private String osv;                 // Os version
    private String appk;                // pubApp key
    private String appv;                // app version
    private int width;                  // screen or placement Width
    private int height;                 // screen or placement Height
    private int contype;                // ConnectionType
    private String carrier;             // 运营商名称, NetworkOperatorName
    private String mccmnc;              // 运营商mcc+mnc, NetworkOperator
    private String gcy;                 // telephony network country iso
    private int sco;                    // screen_orientation, 0:unknown,1:portrait,2:landscape
    private int adtk;                   // ad_tracking_enable, 0:No,1:Yes
    private int ntf;                    // notifications_enabled, 0:No,1:Yes
    private int gp;                     // google_play_services, 0:No,1:Yes
    private Map<String, Object> bps;    // Custom basic values
    private String ip;                  // client IP
    private String ua;                  // client UserAgent

}
