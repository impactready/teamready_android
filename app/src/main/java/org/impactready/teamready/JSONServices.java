package org.impactready.teamready;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONServices {
    private static final String TAG = "JSON Services";

    public static JSONArray remove(JSONArray objectsJSON, String id) {

        try {
            JSONArray newObjectsJSON = new JSONArray();
            int len = objectsJSON.length();

            for (int i = 0; i < len; i++) {
                JSONObject thisObject = objectsJSON.getJSONObject(i);
                if (!thisObject.getString("object_id").equals(id)) {
                    newObjectsJSON.put(thisObject);
                }
            }
            return newObjectsJSON;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            return null;
        }
    }

    public static JSONObject find(JSONArray objectsJSON, String id) {
        try {

            if (objectsJSON != null) {
                int len = objectsJSON.length();
                for (int i = 0; i < len; i++) {
                    JSONObject thisObject = objectsJSON.getJSONObject(i);
                    if (thisObject.getString("object_id").equals(id)) {
                        return thisObject;
                    }
                }

            }
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            return null;
        }
    }
}
