package com.applozic.mobicomkit.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicommons.ApplozicService;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.encryption.EncryptionUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * Created by devashish on 28/11/14.
 */
public class HttpRequestUtils {

    private static final String TAG = "HttpRequestUtils";
    public static String APPLICATION_KEY_HEADER = "Application-Key";
    public static String USERID_HEADER = "UserId-Enabled";
    public static String USERID_HEADER_VALUE = "true";
    public static String DEVICE_KEY_HEADER = "Device-Key";
    public static String APP_MODULE_NAME_KEY_HEADER = "App-Module-Name";
    public static String ACCESS_TOKEN = "Access-Token";
    private static String SOURCE_HEADER = "Source";
    private static String SOURCE_HEADER_VALUE = "1";
    private static final String OF_USER_ID_HEADER = "Of-User-Id";
    private static final String APZ_PRODUCT_APP_HEADER = "Apz-Product-App";
    private static final String APZ_APP_ID_HEADER = "Apz-AppId";
    private static final String APZ_TOKEN_HEADER = "Apz-Token";
    private Context context;


    public HttpRequestUtils(Context context) {
        this.context = ApplozicService.getContext(context);
    }

    public String postData(String urlString, String contentType, String accept, String data) throws Exception {
        return postData(urlString, contentType, accept, data, null);
    }

    public String postData(String urlString, String contentType, String accept, String data, String userId) throws Exception {
        Utils.printLog(context, TAG, "Calling url: " + urlString);
        HttpURLConnection connection;
        URL url;
        try {
            if (!TextUtils.isEmpty(MobiComUserPreference.getInstance(context).getEncryptionKey())) {
                data = EncryptionUtils.encrypt(MobiComUserPreference.getInstance(context).getEncryptionKey(), data);
            }
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (!TextUtils.isEmpty(contentType)) {
                connection.setRequestProperty("Content-Type", contentType);
            }
            if (!TextUtils.isEmpty(accept)) {
                connection.setRequestProperty("Accept", accept);
            }
            addGlobalHeaders(connection, userId);
            connection.connect();

            if (connection == null) {
                return null;
            }
            if (data != null) {
                byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.write(dataBytes);
                os.flush();
                os.close();
            }
            BufferedReader br = null;
            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                InputStream inputStream = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            }
            StringBuilder sb = new StringBuilder();
            try {
                String line;
                if (br != null) {
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    br.close();
                }
            }
            Utils.printLog(context, TAG, "Response : " + sb.toString());
            if (!TextUtils.isEmpty(sb.toString())) {
                if (!TextUtils.isEmpty(MobiComUserPreference.getInstance(context).getEncryptionKey())) {
                    return EncryptionUtils.decrypt(MobiComUserPreference.getInstance(context).getEncryptionKey(), sb.toString());
                }
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.printLog(context, TAG, "Http call failed");
        return null;
    }

    public String postJsonToServer(String stringUrl, String data) throws Exception {
        return postJsonToServer(stringUrl, data, null);
    }

    public String postJsonToServer(String stringUrl, String data, String userId) throws Exception {
        HttpURLConnection connection;
        URL url = new URL(stringUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        if (!TextUtils.isEmpty(MobiComUserPreference.getInstance(context).getDeviceKeyString())) {
            connection.setRequestProperty(DEVICE_KEY_HEADER, MobiComUserPreference.getInstance(context).getDeviceKeyString());
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        addGlobalHeaders(connection, userId);
        connection.connect();

        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        DataOutputStream os = new DataOutputStream(connection.getOutputStream());
        os.write(dataBytes);
        os.flush();
        os.close();
        BufferedReader br = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } else {
            Utils.printLog(context, TAG, "Response code for post json is :" + connection.getResponseCode());
        }
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            if (br != null) {
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {

        } finally {
            if (br != null) {
                br.close();
            }
        }
        Utils.printLog(context, TAG, "Response: " + sb.toString());
        return sb.toString();
    }

    public String getResponse(String urlString, String contentType, String accept) {
        return getResponse(urlString, contentType, accept, false, null);
    }

    public String getResponse(String urlString, String contentType, String accept, boolean isFileUpload) {
        return getResponse(urlString, contentType, accept, isFileUpload, null);
    }

    public String getResponse(String urlString, String contentType, String accept, boolean isFileUpload, String userId) {
        Utils.printLog(context, TAG, "Calling url: " + urlString);

        HttpURLConnection connection = null;
        URL url;

        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoInput(true);

            if (!TextUtils.isEmpty(contentType)) {
                connection.setRequestProperty("Content-Type", contentType);
            }
            if (!TextUtils.isEmpty(accept)) {
                connection.setRequestProperty("Accept", accept);
            }
            addGlobalHeaders(connection, userId);
            connection.connect();

            if (connection == null) {
                return null;
            }
            BufferedReader br = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            } else {
                Utils.printLog(context, TAG, "Response code for getResponse is  :" + connection.getResponseCode());
            }

            StringBuilder sb = new StringBuilder();
            try {
                String line;
                if (br != null) {
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    br.close();
                }
            }

            Utils.printLog(context, TAG, "Response :" + sb.toString());

            if (!TextUtils.isEmpty(sb.toString())) {
                if (!TextUtils.isEmpty(MobiComUserPreference.getInstance(context).getEncryptionKey())) {
                    return isFileUpload ? sb.toString() : EncryptionUtils.decrypt(MobiComUserPreference.getInstance(context).getEncryptionKey(), sb.toString());
                }
            }
            return sb.toString();
        } catch (ConnectException e) {
            Utils.printLog(context, TAG, "failed to connect Internet is not working");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {

        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void addGlobalHeaders(HttpURLConnection connection, String userId) {
        try {
            connection.setRequestProperty(ACCESS_TOKEN, MobiComUserPreference.getInstance(context).getPassword());

            if (MobiComKitClientService.getAppModuleName(context) != null) {
                connection.setRequestProperty(APP_MODULE_NAME_KEY_HEADER, MobiComKitClientService.getAppModuleName(context));
            }

            if (!TextUtils.isEmpty(userId)) {
                connection.setRequestProperty(OF_USER_ID_HEADER, URLEncoder.encode(userId, "UTF-8"));
            }

            MobiComUserPreference userPreferences = MobiComUserPreference.getInstance(context);

            if (User.RoleType.AGENT.getValue().equals(userPreferences.getUserRoleType()) && !TextUtils.isEmpty(userId)) {
                connection.setRequestProperty(APZ_APP_ID_HEADER, MobiComKitClientService.getApplicationKey(context));
                connection.setRequestProperty(APZ_PRODUCT_APP_HEADER, "true");
                String userCredentials = getCredentialsWithPassword().getUserName() + ":" + String.valueOf(getCredentialsWithPassword().getPassword());
                String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
                connection.setRequestProperty(APZ_TOKEN_HEADER, basicAuth);
            } else {
                connection.setRequestProperty(APPLICATION_KEY_HEADER, MobiComKitClientService.getApplicationKey(context));
                connection.setRequestProperty(SOURCE_HEADER, SOURCE_HEADER_VALUE);
                connection.setRequestProperty(USERID_HEADER, USERID_HEADER_VALUE);
                connection.setRequestProperty(DEVICE_KEY_HEADER, MobiComUserPreference.getInstance(context).getDeviceKeyString());

                if (userPreferences.isRegistered()) {
                    String userCredentials = getCredentials().getUserName() + ":" + String.valueOf(getCredentials().getPassword());
                    String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
                    connection.setRequestProperty("Authorization", basicAuth);
                    connection.setRequestProperty("Application-User", basicAuth);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private PasswordAuthentication getCredentials() {
        MobiComUserPreference userPreferences = MobiComUserPreference.getInstance(context);
        if (!userPreferences.isRegistered()) {
            return null;
        }
        return new PasswordAuthentication(userPreferences.getUserId(), userPreferences.getDeviceKeyString().toCharArray());
    }

    private PasswordAuthentication getCredentialsWithPassword() {
        MobiComUserPreference userPreferences = MobiComUserPreference.getInstance(context);
        if (!userPreferences.isRegistered()) {
            return null;
        }
        return new PasswordAuthentication(userPreferences.getUserId(), userPreferences.getPassword().toCharArray());
    }

}
