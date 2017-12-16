package com.yhkj.yymall.view.EZUIkit;

import android.net.Uri;
import android.text.TextUtils;
import com.ezvizuikit.open.ParamException;
import com.videogo.openapi.EzvizAPI;
import com.videogo.util.AESCipher;
import com.yhkj.yymall.view.EZUIkit.EZUIPlayer.EZUIKitPlayMode;
import com.videogo.util.LogUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EZUIKitUilts {
    private static final String TAG = "EZUIKitUilts";
    private static final String PARAM_PROTOCAL = "protocol";
    private static final String PARAM_HOST = "host";
    private static final String PARAM_DEVICE_SERIAL = "deviceSerial";
    private static final String PARAM_CAMERA_NO = "cameraNo";
    private static final String PARAM_PLAYTPE = "playTpe";
    private static final String PARAM_LOCAL = "local";
    private static final String PARAM_CLOUD = "cloud";
    private static final String PARAM_LIVE = "live";
    private static final String PARAM_REC = "rec";
    private static final String PARAM_HD = "hd";
    private static final String PARAM_MUTE = "mute";
    private static final String PARAM_MIX = "mix";
    private static final String PARAM_BEGIN = "begin";
    private static final String PARAM_END = "end";
    private static final String PARAM_ALARMID = "alarmId";
    private static final String PARAM = "param";
    protected static Map<String, String> mParamMap;

    public EZUIKitUilts() {
    }

    private static Map<String, String> getParamMap() {
        mParamMap = new HashMap();
        mParamMap.put("protocol", "(^ezopen://)");
        mParamMap.put("host", "(open.ys7.com)");
        mParamMap.put("deviceSerial", "^[0-9]{1,}");
        mParamMap.put("cameraNo", "^[0-9]{1,}");
        mParamMap.put("playTpe", "(live|rec)");
        mParamMap.put("alarmId", "[\\s\\S]+");
        mParamMap.put("begin", "^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))(([-/.]?)|([01][0-9]|2[0-3])|([01][0-9]|2[0-3])[0-5][0-9]|([01][0-9]|2[0-3])[0-5][0-9][0-5][0-9])$");
        mParamMap.put("end", "^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))(([-/.]?)|([01][0-9]|2[0-3])|([01][0-9]|2[0-3])[0-5][0-9]|([01][0-9]|2[0-3])[0-5][0-9][0-5][0-9])$");
        mParamMap.put("mute", "(true|false)");
        mParamMap.put("param", "(alarmId|begin|end|mute)");
        return mParamMap;
    }

    private static boolean checkValue(String key, String value, String errorStr) throws ParamException {
        if(Pattern.matches((String)mParamMap.get(key), value)) {
            return true;
        } else {
            throwValueExcption(key, value, errorStr);
            return false;
        }
    }

    private static String getUrlRemoveParam(String url) {
        Pattern patternUrl = Pattern.compile("(.+?)\\?");
        Matcher matcherUrl = patternUrl.matcher(url);
        return matcherUrl.find()?matcherUrl.group(1):url;
    }

    protected static boolean isEZOpenProtocol(String url) {
        String check = "^ezopen://";
        Pattern pattern = Pattern.compile(check);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private static String getVerifyCode(String string) {
        if(TextUtils.isEmpty(string)) {
            return null;
        } else {
            String[] keys = string.split(":");
            if(keys.length > 1 && "AES".equals(keys[0])) {
                try {
                    return AESCipher.decrypt(EzvizAPI.getInstance().getAppKey(), keys[1]);
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return keys[1];
                }
            } else {
                return string;
            }
        }
    }

    private static EZPlayURLParams getParamMap(Matcher matcher, String url) throws ParamException {
        EZPlayURLParams urlparams = new EZPlayURLParams();
        int count1 = matcher.groupCount();
        LogUtil.d("EZUIKitUilts", "getParamMap  pattern1 " + matcher.group(1));
        String camerNo_str;
        if(count1 > 4) {
            camerNo_str = matcher.group(count1 - 4);
            String strings = getVerifyCode(camerNo_str);
            if(!TextUtils.isEmpty(strings)) {
                urlparams.verifyCode = strings;
            }
        }

        urlparams.host = matcher.group(count1 - 3);
        checkValue("host", urlparams.host, "UE007");
        urlparams.deviceSerial = matcher.group(count1 - 2);
        checkValue("deviceSerial", urlparams.deviceSerial, "UE007");
        camerNo_str = matcher.group(count1 - 1);
        checkValue("cameraNo", camerNo_str, "UE007");
        urlparams.cameraNo = Integer.parseInt(camerNo_str);
        String[] strings1 = matcher.group(count1).split("\\.");
        if(strings1.length == 1) {
            if(strings1[0].equalsIgnoreCase("live")) {
                urlparams.type = 1;
                urlparams.videoLevel = 1;
            } else {
                if(!strings1[0].equalsIgnoreCase("rec")) {
                    throw new ParamException("UE006", "\"" + strings1[0] + "\" is error");
                }

                urlparams.type = 2;
            }
        } else if(strings1.length == 2) {
            if(strings1[1].equalsIgnoreCase("live")) {
                urlparams.type = 1;
                urlparams.videoLevel = strings1[0].equalsIgnoreCase("hd")?2:1;
            } else {
                if(!strings1[1].equalsIgnoreCase("rec")) {
                    throw new ParamException("UE006", "\"" + strings1[0] + "\" is error");
                }

                urlparams.type = 2;
                if(strings1[0].equalsIgnoreCase("cloud")) {
                    urlparams.recodeType = 1;
                } else if(strings1[0].equalsIgnoreCase("local")) {
                    urlparams.recodeType = 2;
                } else {
                    if(!strings1[0].equalsIgnoreCase("mix")) {
                        throw new ParamException("UE006", "\"" + strings1[0] + "\" is error");
                    }

                    urlparams.recodeType = 0;
                }
            }
        }

        return urlparams;
    }

    protected static EZPlayURLParams analyzeParam(String url) throws ParamException {
        if(!isEZOpenProtocol(url)) {
            throw new ParamException("UE007", "protocal is error");
        } else {
            String mainUrl = getUrlRemoveParam(url);
            Pattern pattern1 = Pattern.compile("^ezopen://(.+?)@(.+?)/(.+?)/(.+?)\\.(.+?)$");
            Pattern pattern2 = Pattern.compile("^ezopen://(.+?)/(.+?)/(.+?)\\.(.+?)$");
            Matcher matcher1 = pattern1.matcher(mainUrl);
            EZPlayURLParams urlparams;
            if(matcher1.find()) {
                urlparams = getParamMap(matcher1, url);
            } else {
                Matcher matcher2 = pattern2.matcher(mainUrl);
                if(!matcher2.find()) {
                    throw new ParamException("UE007", "\"" + url + "\" is error");
                }

                urlparams = getParamMap(matcher2, url);
            }

            HashMap paramsMap = new HashMap();
            Uri uri = Uri.parse(url);
            Set keys = uri.getQueryParameterNames();
            Iterator it = keys.iterator();

            while(it.hasNext()) {
                String str = (String)it.next();
                LogUtil.d("EZUIKitUilts", "analyzeParam " + str);
                paramsMap.put(str, uri.getQueryParameter(str));
                if(Pattern.matches((String)mParamMap.get("param"), str)) {
                    LogUtil.d("EZUIKitUilts", "key= " + str + " and value= " + uri.getQueryParameter(str));
                    if(Pattern.matches((String)mParamMap.get(str), uri.getQueryParameter(str))) {
                        String result = uri.getQueryParameter(str);
                        LogUtil.d("EZUIKitUilts", "param = \"" + str + "\"" + " value = " + result);
                        if("mute".equalsIgnoreCase(str)) {
                            urlparams.mute = Boolean.parseBoolean(result);
                        }

                        int length;
                        if("begin".equalsIgnoreCase(str)) {
                            checkValue("begin", result, "UE006");
                            length = result.length();
                            checkValue("begin", result, "UE006");
                            result = "00000000000000".replace("00000000000000".substring(0, length), result);

                            try {
                                urlparams.startTime = getTimeCalendar(result);
                            } catch (ParseException var15) {
                                var15.printStackTrace();
                                throwValueExcption("begin", result, "UE006");
                            }
                        }

                        if("end".equalsIgnoreCase(str)) {
                            length = result.length();
                            checkValue("end", result, "UE006");
                            result = "00000000000000".replace("00000000000000".substring(0, length), result);

                            try {
                                urlparams.endTime = getTimeCalendar(result);
                            } catch (ParseException var16) {
                                var16.printStackTrace();
                                throwValueExcption("end", result, "UE006");
                            }
                        }

                        if("alarmId".equalsIgnoreCase(str)) {
                            urlparams.alarmId = result;
                        }
                    } else {
                        throwValueExcption(str, uri.getQueryParameter(str), "UE006");
                    }
                } else {
                    throwKeyExcption(str, "UE006");
                }
            }

            if(urlparams != null) {
                if(urlparams.startTime == null && urlparams.endTime != null) {
                    throw new ParamException("UE006", "you must have begin time");
                }

                if(urlparams.startTime != null && urlparams.endTime != null && !urlparams.startTime.before(urlparams.endTime)) {
                    throw new ParamException("UE006", "The end time cannot be earlier than start time");
                }
            }

            return urlparams;
        }
    }

    private static void throwValueExcption(String key, String value, String errorStr) throws ParamException {
        throw new ParamException(errorStr, "value \"" + value + "\"" + " of param " + "\"" + key + "\"" + " is error");
    }

    private static void throwKeyExcption(String key, String errorStr) throws ParamException {
        throw new ParamException(errorStr, "param key \"" + key + "\"" + " is error");
    }

    private static Calendar getTimeCalendar(String time) throws ParseException {
        new Date();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date mDate = mDateFormat.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        return calendar;
    }

    protected static EZUIKitPlayMode getUrlPlayType(String url) {
        String mainUrl = getUrlRemoveParam(url);
        return TextUtils.isEmpty(mainUrl)?EZUIKitPlayMode.EZUIKIT_PLAYMODE_UNKOWN:(mainUrl.endsWith(".rec")?EZUIKitPlayMode.EZUIKIT_PLAYMODE_REC:(mainUrl.endsWith(".live")?EZUIKitPlayMode.EZUIKIT_PLAYMODE_LIVE:EZUIKitPlayMode.EZUIKIT_PLAYMODE_UNKOWN));
    }

    static {
        getParamMap();
    }
}