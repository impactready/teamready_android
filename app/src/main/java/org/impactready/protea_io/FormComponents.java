package org.impactready.protea_io;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class FormComponents {
    private static final String TAG = "Form Components";

    public static ArrayList<SpinnerElement> loadTypeDropdown(String typeDescription, JSONArray typesJson) {
        ArrayList<SpinnerElement> typeList = new ArrayList<SpinnerElement>();
        try {

            for (int i = 0; i < typesJson.length(); i++) {
                JSONObject type = typesJson.getJSONObject(i);
                if (type.getString("usage").equals(typeDescription)) {
                    typeList.add(new SpinnerElement(type.getString("description"),type.getString("id")));
                }

            }

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return typeList;
    }

    public static ArrayList<SpinnerElement> loadGroupDropdown(JSONArray groupsJson) {
        ArrayList<SpinnerElement> groupList = new ArrayList<SpinnerElement>();
        try {

            for (int i = 0; i < groupsJson.length(); i++) {
                JSONObject group = groupsJson.getJSONObject(i);
                groupList.add(new SpinnerElement(group.getString("name"), group.getString("id")));
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
                bestAccuracy = accuracy;

            }
        }


        return finalLocation;
    }

    public static JSONObject getAllFormData(View v, String fragmentType) {
        JSONObject eventJson = new JSONObject();
        Integer description = null;
        Integer type = null;
        Integer group = null;
        Integer longitude = null;
        Integer latitude = null;
        Integer fileLocation = null;

        if (fragmentType.equals("event")) {
            description = R.id.input_event_description;
            type = R.id.input_event_type;
            group = R.id.input_event_group;
            longitude = R.id.input_event_longitude;
            latitude = R.id.input_event_latitude;
            fileLocation = R.id.input_event_image_location;
        } else if (fragmentType.equals("story")) {
            description = R.id.input_story_description;
            type = R.id.input_story_type;
            group = R.id.input_story_group;
            longitude = R.id.input_story_longitude;
            latitude = R.id.input_story_latitude;
            fileLocation = R.id.input_story_image_location;
        } else if (fragmentType.equals("measurement")) {
            description = R.id.input_measurement_description;
            type = R.id.input_measurement_type;
            group = R.id.input_measurement_group;
            longitude = R.id.input_measurement_longitude;
            latitude = R.id.input_measurement_latitude;
            fileLocation = R.id.input_measurement_image_location;
        }

        try {
            eventJson.put("description", ((EditText) v.findViewById(description)).getText());
            eventJson.put("type", ((Spinner) v.findViewById(type)).getSelectedItem());
            eventJson.put("group", ((Spinner) v.findViewById(group)).getSelectedItem());
            eventJson.put("longitude", ((EditText) v.findViewById(longitude)).getText());
            eventJson.put("latitude", ((EditText) v.findViewById(latitude)).getText());
            eventJson.put("image", ((EditText) v.findViewById(fileLocation)).getText());
            eventJson.put("object_id", UUID.randomUUID().toString());
            eventJson.put("uploaded", "no");

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return eventJson;
    }
}
