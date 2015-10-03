package org.impactready.teamready;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class FormActivityEventFragment extends Fragment implements LocationListener {
    private static final String TAG = "Event creation";
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();

        View v =  inflater.inflate(R.layout.activity_form_fragment_event, container, false);
        setupScrolls(context, v);
        findLocation(v);
        setOnClickListenerSave(v);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    public void setupScrolls (Context context, View v) {
        Spinner typeSpinner = (Spinner) v.findViewById(R.id.input_event_type);
        Spinner groupSpinner = (Spinner) v.findViewById(R.id.input_event_group);

        JSONArray typesJson = FileServices.getSetup(R.string.types_filename, context);
        JSONArray groupsJson = FileServices.getSetup(R.string.groups_filename, context);

        List<SpinnerElement> typeList = FormComponents.loadTypeDropdown("Event type", typesJson);
        List<SpinnerElement> groupList = FormComponents.loadGroupDropdown(groupsJson);

        ArrayAdapter<SpinnerElement> typeAdapter = new ArrayAdapter<SpinnerElement>(this.getActivity(),
                android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<SpinnerElement> groupAdapter = new ArrayAdapter<SpinnerElement>(this.getActivity(),
                android.R.layout.simple_spinner_item, groupList);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);

    }

    public void findLocation(View v) {
        if (null == (locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE)))
            getActivity().finish();

        Location finalLocation = FormComponents.getLocation(locationManager);

        if (finalLocation != null) updateFieldsWithLocation(finalLocation, v);
    }

    public void updateFieldsWithLocation(Location location, View v) {
        EditText longitude = (EditText) v.findViewById(R.id.input_event_longitude);
        EditText latitude = (EditText) v.findViewById(R.id.input_event_latitude);

        longitude.setText(String.valueOf(location.getLongitude()));
        latitude.setText(String.valueOf(location.getLatitude()));
    }

    public void setOnClickListenerSave(View v) {
        Button submitButton = (Button) v.findViewById(R.id.input_submit);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        JSONObject eventJson = new JSONObject();
//                        eventJson.put("description", ((EditText) v.findViewById(R.id.input_event_description)).getText());
//                        eventJson.put("type", ((Spinner) v.findViewById(R.id.input_event_type)).getSelectedItem());
//                        eventJson.put("group", ((Spinner) v.findViewById(R.id.input_event_group)).getSelectedItem());
//                        eventJson.put("longitude", ((EditText) v.findViewById(R.id.input_event_longitude)).getText());
//                        eventJson.put("latitude", ((EditText) v.findViewById(R.id.input_event_latitude)).getText());

                    }
                }
        );
    }

    @Override
    public void onLocationChanged(Location location) {

        updateFieldsWithLocation(location, getView());
    }

    public void onStatusChanged(String provider, int status,
                                Bundle extras) {
        // NA
    }

    public void onProviderEnabled(String provider) {
        // NA
    }

    public void onProviderDisabled(String provider) {
        // NA
    }
}
