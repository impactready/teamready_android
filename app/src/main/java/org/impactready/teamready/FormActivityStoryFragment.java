package org.impactready.teamready;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormActivityStoryFragment extends Fragment implements LocationListener {

    private static final String TAG = "Story creation";
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();

        View v =  inflater.inflate(R.layout.activity_form_fragment_story, container, false);
        setupScrolls(context, v);
        findLocation(v);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    public void setupScrolls (Context context, View v) {
        Spinner typeSpinner = (Spinner) v.findViewById(R.id.input_story_type);
        List<String> typeList = new ArrayList<String>();
        Spinner groupSpinner = (Spinner) v.findViewById(R.id.input_story_group);
        List<String> groupList = new ArrayList<String>();

        JSONArray typesJson = FileServices.getSetup(R.string.types_filename, context);
        JSONArray groupsJson = FileServices.getSetup(R.string.groups_filename, context);

        try {

            for (int i = 0; i < typesJson.length(); i++) {
                JSONObject type = typesJson.getJSONObject(i);
                String test = type.getString("usage");
                String test2 = type.getString("description");
                if (type.getString("usage").equals("Domain of change")) {
                    typeList.add(type.getString("description"));
                }

            }

            for (int i = 0; i < groupsJson.length(); i++) {
                JSONObject group = groupsJson.getJSONObject(i);
                groupList.add(group.getString("name"));
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, groupList);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);

    }

    public void findLocation(View v) {
        if (null == (locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE)))
            getActivity().finish();

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

        if (finalLocation != null) updateFieldsWithLocation(finalLocation, v);
    }

    public void updateFieldsWithLocation(Location location, View v) {
        EditText longitude = (EditText) v.findViewById(R.id.input_story_longitude);
        EditText latitude = (EditText) v.findViewById(R.id.input_story_latitude);

        longitude.setText(String.valueOf(location.getLongitude()));
        latitude.setText(String.valueOf(location.getLatitude()));
    }

    @Override
    public void onLocationChanged(Location location) {

        updateFieldsWithLocation(location, getView());
    }

    @Override
    public void onStatusChanged(String provider, int status,
                                Bundle extras) {
        // NA
    }

    @Override
    public void onProviderEnabled(String provider) {
        // NA
    }

    @Override
    public void onProviderDisabled(String provider) {
        // NA
    }
}
