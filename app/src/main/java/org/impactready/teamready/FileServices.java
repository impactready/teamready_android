package org.impactready.teamready;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileServices {
    private static final String TAG = "File Services";

    public static JSONArray getSetup(Integer fileId, Context context) {
        String typesFilename = context.getString(fileId);
        try {
            return new JSONArray(readFileJson(context, typesFilename));
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }

    protected static void writeFileJson(Context context, Integer fileId, JSONArray json) throws IOException {
        String filename = context.getString(fileId);

        FileOutputStream fos1 =  context.openFileOutput(filename, Context.MODE_PRIVATE);
        fos1.write(json.toString().getBytes());
        fos1.close();
    }

    protected static String readFileJson(Context context, String filename) throws IOException {
        FileInputStream fos1 =  context.openFileInput(filename);
        byte[] bytesFromFile = new byte[(int) fos1.available()];
        fos1.read(bytesFromFile);
        fos1.close();

        return new String(bytesFromFile, "UTF-8");
    }
}