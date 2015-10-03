package org.impactready.teamready;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

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

    public static Location getLocation(LocationManager locationManager) {
        Location finalLocation = null;
        float bestAccuracy = Float.MAX_VALUE;
        List<String> matchingProviders = locationManager.getAllProviders();

        for (String provider : matchingProviders) {

            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                if (accuracy < bestAccuracy) {
                    finalLocation = location;
                    bestAccuracy = accuracy;

                }
            }
        }
        return finalLocation;
    }
}
