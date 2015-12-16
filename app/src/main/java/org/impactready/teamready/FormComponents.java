package org.impactready.teamready;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class FormComponents {
    private static final String TAG = "Form Components";

    public static ArrayList<String> loadTypeDropdown(String typeDescription, JSONArray typesJson) {
        ArrayList<String> typeList = new ArrayList<>();
        try {

            for (int i = 0; i < typesJson.length(); i++) {
                JSONObject type = typesJson.getJSONObject(i);
                if (type.getString("usage").equals(typeDescription)) {
                    typeList.add(type.getString("description"));
                }

            }

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return typeList;
    }

    public static ArrayList<String> loadGroupDropdown(JSONArray groupsJson) {
        ArrayList<String> groupList = new ArrayList<>();
        try {

            for (int i = 0; i < groupsJson.length(); i++) {
                JSONObject group = groupsJson.getJSONObject(i);
                groupList.add(group.getString("name"));
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return groupList;
    }

    public static Location getLocation(LocationManager locationManager, String provider) {
        Location finalLocation = null;
        float bestAccuracy = Float.MAX_VALUE;

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            float accuracy = location.getAccuracy();
            if (accuracy < bestAccuracy) {
                finalLocation = location;
            }
        }


        return finalLocation;
    }

    public static JSONObject getAllFormData(View v, String fragmentType) {
        JSONObject eventJson = new JSONObject();
        Integer description;
        Integer type = null;
        Integer group = null;
        Integer longitude;
        Integer latitude;
        Integer fileLocation;
        Integer object_id;
        String object_type;

        if (fragmentType.equals("event")) {
            type = org.impactready.teamready.R.id.input_event_type;
            group = org.impactready.teamready.R.id.input_event_group;
        } else if (fragmentType.equals("story")) {
            type = org.impactready.teamready.R.id.input_story_type;
            group = org.impactready.teamready.R.id.input_story_group;
        } else if (fragmentType.equals("measurement")) {
            type = org.impactready.teamready.R.id.input_measurement_type;
            group = org.impactready.teamready.R.id.input_measurement_group;
        }

        fileLocation = org.impactready.teamready.R.id.input_image_location;
        description = org.impactready.teamready.R.id.input_description;
        longitude = org.impactready.teamready.R.id.input_longitude;
        latitude = org.impactready.teamready.R.id.input_latitude;
        object_id = org.impactready.teamready.R.id.input_object_id;
        object_type = fragmentType;

        try {
            eventJson.put("description", ((EditText) v.findViewById(description)).getText());
            eventJson.put("type", ((Spinner) v.findViewById(type)).getSelectedItem());
            eventJson.put("group", ((Spinner) v.findViewById(group)).getSelectedItem());
            eventJson.put("longitude", ((TextView) v.findViewById(longitude)).getText());
            eventJson.put("latitude", ((TextView) v.findViewById(latitude)).getText());
            eventJson.put("image", ((EditText) v.findViewById(fileLocation)).getText());
            eventJson.put("object_id", ((EditText) v.findViewById(object_id)).getText());
            eventJson.put("object_type", object_type);
            eventJson.put("uploaded", "no");

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return eventJson;
    }
}
