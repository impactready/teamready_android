package org.impactready.protea_io;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
        String url = "https://impactready.herokuapp.com/api/v1/android/setup";
        String userCredentials = "api:" + params;
        String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));

        try {

            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(20000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            postParameters = "?description=" + objectJSON.getString("description");
            postParameters += "&type=" + objectJSON.getString("type");
            postParameters += "?group=" + objectJSON.getString("group");
            postParameters += "?longitude=" + objectJSON.getString("longitude");
            postParameters += "?latitude=" + objectJSON.getString("latitude");
            postParameters += "?object_id=" + objectJSON.getString("object_id");

            conn.setFixedLengthStreamingMode(
                    postParameters.getBytes().length);
            OutputStream os = conn.getOutputStream();
            os.write(postParameters.toString().getBytes("UTF-8"));
            os.close();


            is = new BufferedInputStream(conn.getInputStream());

            InputStreamReader streamReader = new InputStreamReader(is);
            reader = new BufferedReader(streamReader);
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }

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
