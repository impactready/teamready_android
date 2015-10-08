package org.impactready.teamready;

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
import java.util.List;

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

    public static JSONObject getAllFormData(View v) {
        JSONObject eventJson = new JSONObject();
        try {
            EditText text = (EditText) v.findViewById(R.id.input_event_description);
            eventJson.put("description", ((EditText) v.findViewById(R.id.input_event_description)).getText());
            eventJson.put("type", ((Spinner) v.findViewById(R.id.input_event_type)).getSelectedItem());
            eventJson.put("group", ((Spinner) v.findViewById(R.id.input_event_group)).getSelectedItem());
            eventJson.put("longitude", ((EditText) v.findViewById(R.id.input_event_longitude)).getText());
            eventJson.put("latitude", ((EditText) v.findViewById(R.id.input_event_latitude)).getText());

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return eventJson;
    }
}
