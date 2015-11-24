package org.impactready.teamready;

import android.util.Base64;
import android.util.Log;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class NetworkServices {
    private static final String TAG = "Network Services";

    public static StringBuilder getSetup(String params) {
        InputStream is = null;
        HttpsURLConnection conn = null;
        BufferedReader reader = null;
        String url = "https://impactready.herokuapp.com/api/v1/android/setup";
        String userCredentials = "api:" + params;
        String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));


        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1");

            sslcontext.init(null, null, null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(20000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", basicAuth);

            is = new BufferedInputStream(conn.getInputStream());

            InputStreamReader streamReader = new InputStreamReader(is);
            reader = new BufferedReader(streamReader);
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }

            Log.e(TAG, builder.toString());
            return builder;

        } catch (FileNotFoundException e){
            Log.e(TAG, "FileNotFoundException", e);
            return null;
        } catch (SocketTimeoutException e){
            Log.e(TAG, "SocketTimeoutException", e);
            return null;
        } catch (KeyManagementException e){
            Log.e(TAG, "KeyManagementException", e);
            return null;
        } catch (NoSuchAlgorithmException e){
            Log.e(TAG, "NoSuchAlgorithmException", e);
            return null;
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException", e);
            return null;
        }  catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException", e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            return null;
        } finally {
            if (is != null) {
                conn.disconnect();
            }
        }
    }

    public static StringBuilder sendObject(String params, JSONObject objectJSON) {
        InputStream is = null;
        HttpsURLConnection conn = null;
        BufferedReader reader = null;
        String postParameters = null;
        String url = "https://impactready.herokuapp.com/api/v1/android/create";
        String userCredentials = "api:" + params;
        String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));
        String boundary = "---+++---" + System.currentTimeMillis();

        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1");

            sslcontext.init(null, null, null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(20000 /* milliseconds */);
            conn.setConnectTimeout(20000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.setBoundary(boundary);
            if (objectJSON.getString("image").length() > 0) reqEntity.addPart("object[image]", new FileBody(new File(objectJSON.getString("image").substring(7))));
            reqEntity.addPart("object_category", new StringBody(objectJSON.getString("object_type"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("object[object_id]", new StringBody(objectJSON.getString("object_id"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("object[description]", new StringBody(objectJSON.getString("description"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("object[type]", new StringBody(objectJSON.getString("type"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("object[group]", new StringBody(objectJSON.getString("group"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("object[longitude]", new StringBody(objectJSON.getString("longitude"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("object[latitude]", new StringBody(objectJSON.getString("latitude"), ContentType.TEXT_PLAIN));

//            conn.setRequestProperty("Content-Length", String.valueOf(reqEntity.build().getContentLength()));

            OutputStream os = conn.getOutputStream();
            reqEntity.build().writeTo(os);
            os.close();

//            postParameters = "object_type=" + objectJSON.getString("object_type");
//            postParameters += "&object_id=" + objectJSON.getString("object_id");
//            postParameters += "&description=" + objectJSON.getString("description");
//            postParameters += "&type=" + objectJSON.getString("type");
//            postParameters += "&group=" + objectJSON.getString("group");
//            postParameters += "&longitude=" + objectJSON.getString("longitude");
//            postParameters += "&latitude=" + objectJSON.getString("latitude");
//
////            postParameters = URLEncoder.encode(postParameters, "UTF-8");
//            conn.setFixedLengthStreamingMode(
//                    postParameters.getBytes("UTF-8").length);
//            OutputStream os = conn.getOutputStream();
//            os.write(postParameters.getBytes("UTF-8"));
//            os.close();

//            int response = conn.getResponseCode();
            is = new BufferedInputStream(conn.getInputStream());

            InputStreamReader streamReader = new InputStreamReader(is);
            reader = new BufferedReader(streamReader);
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            streamReader.close();
            reader.close();

            return builder;

        } catch (FileNotFoundException e){
            Log.e(TAG, "FileNotFoundException", e);
            return null;
        } catch (KeyManagementException e){
            Log.e(TAG, "KeyManagementException", e);
            return null;
        } catch (NoSuchAlgorithmException e){
            Log.e(TAG, "NoSuchAlgorithmException", e);
            return null;
        } catch (SocketTimeoutException e){
            Log.e(TAG, "SocketTimeoutException", e);
            return null;
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException", e);
            return null;
        }  catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException", e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            return null;
        } finally {
            if (is != null) {
                conn.disconnect();
            }
        }

    }
}
