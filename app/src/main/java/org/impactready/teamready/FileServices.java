package org.impactready.teamready;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileServices {
    private static final String TAG = "File Services";

    public static JSONArray getFileJSON(Context context, Integer fileId) {
        try {

            return new JSONArray(readFileJson(context, fileId));

        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }

    public static void saveObjectToFile(Context context, JSONObject objectJson, Integer objectType) {
        JSONArray objectArray;

        try {
            switch (objectType) {
                case R.string.event_main_name:
                    objectArray = new JSONArray(readFileJson(context, R.string.events_filename));
                    objectArray.put(objectJson);
                    writeFileJson(context, R.string.events_filename, objectArray);
                    break;

                case R.string.story_main_name:
                    objectArray = new JSONArray(readFileJson(context, R.string.stories_filename));
                    objectArray.put(objectJson);
                    writeFileJson(context, R.string.stories_filename, objectArray);
                    break;

                case R.string.measurement_main_name:
                    objectArray = new JSONArray(readFileJson(context, R.string.measurements_filename));
                    objectArray.put(objectJson);
                    writeFileJson(context, R.string.measurements_filename, objectArray);
                    break;
            }

        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

    }

    protected static void writeFileJson(Context context, Integer fileId, JSONArray json) throws IOException {
        String filename = context.getString(fileId);

        FileOutputStream fos1 =  context.openFileOutput(filename, Context.MODE_PRIVATE);
        fos1.write(json.toString().getBytes());
        fos1.close();
    }

    protected static String readFileJson(Context context, Integer fileId) throws IOException {
        String filename = context.getString(fileId);

        File file = new File(context.getFilesDir() + "/" + filename);
        if(!file.exists()) {
            FileOutputStream fosIn =  context.openFileOutput(filename, Context.MODE_PRIVATE);
            fosIn.write("[]".getBytes());
            fosIn.close();
        }

        FileInputStream fos1 =  context.openFileInput(filename);
        byte[] bytesFromFile = new byte[fos1.available()];
        fos1.read(bytesFromFile);
        fos1.close();

        return new String(bytesFromFile, "UTF-8");
    }
}