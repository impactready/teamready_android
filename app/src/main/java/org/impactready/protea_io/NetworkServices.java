package org.impactready.protea_io;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
        String boundary = "---+++---";

        try {

            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(1000 /* milliseconds */);
            conn.setConnectTimeout(2000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", basicAuth);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" +  boundary);


            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.setBoundary(boundary);
            reqEntity.addPart("image", new FileBody(new File(objectJSON.getString("image").substring(7))));
            reqEntity.addPart("object_type", new StringBody(objectJSON.getString("object_type"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("object_id", new StringBody(objectJSON.getString("object_id"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("description", new StringBody(objectJSON.getString("description"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("type", new StringBody(objectJSON.getString("type"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("group", new StringBody(objectJSON.getString("group"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("longitude", new StringBody(objectJSON.getString("longitude"), ContentType.TEXT_PLAIN));
            reqEntity.addPart("latitude", new StringBody(objectJSON.getString("latitude"), ContentType.TEXT_PLAIN));

            conn.setChunkedStreamingMode(1024);
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

            int response = conn.getResponseCode();
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
