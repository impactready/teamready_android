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
            if (objectsJSON != null) {
                for (int i = 0; i < len; i++) {
                    JSONObject thisObject = objectsJSON.getJSONObject(i);
                    if (!thisObject.getString("object_id").equals(id)) {
                        newObjectsJSON.put(thisObject);
                    }
                }
            }
            return newObjectsJSON;
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
            return null;
        }
    }
}
