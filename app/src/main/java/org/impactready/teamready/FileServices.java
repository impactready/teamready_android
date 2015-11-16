package org.impactready.teamready;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileServices {
    private static final String TAG = "File Services";
    public static final int MEDIA_TYPE_IMAGE = 1;

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
                case org.impactready.teamready.R.string.event_main_name:
                    objectArray = new JSONArray(readFileJson(context, org.impactready.teamready.R.string.events_filename));
                    objectArray.put(objectJson);
                    writeFileJson(context, org.impactready.teamready.R.string.events_filename, objectArray);
                    break;

                case org.impactready.teamready.R.string.story_main_name:
                    objectArray = new JSONArray(readFileJson(context, org.impactready.teamready.R.string.stories_filename));
                    objectArray.put(objectJson);
                    writeFileJson(context, org.impactready.teamready.R.string.stories_filename, objectArray);
                    break;

                case org.impactready.teamready.R.string.measurement_main_name:
                    objectArray = new JSONArray(readFileJson(context, org.impactready.teamready.R.string.measurements_filename));
                    objectArray.put(objectJson);
                    writeFileJson(context, org.impactready.teamready.R.string.measurements_filename, objectArray);
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


    public static Uri getOutputMediaFile() {
        File mediaFile = null;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "teamready");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");

        return Uri.fromFile(mediaFile);

    }

    public static JSONArray removeJSONObject( JSONArray array, int pos) {

        JSONArray newArray = new JSONArray();
        try{
            for(int i=0;i<array.length();i++){
                if(i!=pos) newArray.put(array.get(i));
            }
        } catch (Exception e){e.printStackTrace();}
        return newArray;

    }
}