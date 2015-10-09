package org.impactready.teamready;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class FormActivityFragment extends Fragment implements LocationListener {
    private static final String TAG = "Event creation";
    private LocationManager locationManager;
    private String provider;
    private String fragmentType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();
        fragmentType = this.getArguments().getString("type");
        View v = null;

        if (fragmentType.equals(getString(R.string.event_main_name))) {
            v =  inflater.inflate(R.layout.activity_form_fragment_event, container, false);

        } else if (fragmentType.equals(getString(R.string.story_main_name))) {
            v =  inflater.inflate(R.layout.activity_form_fragment_story, container, false);

        } else if (fragmentType.equals(getString(R.string.measurement_main_name))) {
            v =  inflater.inflate(R.layout.activity_form_fragment_measurement, container, false);
        }

        if (null == (locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE)))
            getActivity().finish();

        setupScrolls(context, v);
        findLocation(v);
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        setOnClickListenerSave(context, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    public void setupScrolls (Context context, View v) {
        Spinner typeSpinner = null;
        Spinner groupSpinner = null;
        List<SpinnerElement> typeList = null;

        JSONArray typesJson = FileServices.getFileJSON(context, R.string.types_filename);
        JSONArray groupsJson = FileServices.getFileJSON(context, R.string.groups_filename);

        if (fragmentType.equals(getString(R.string.event_main_name))) {

            typeSpinner = (Spinner) v.findViewById(R.id.input_event_type);
            groupSpinner = (Spinner) v.findViewById(R.id.input_event_group);
            typeList = FormComponents.loadTypeDropdown("Event type", typesJson);

        } else if (fragmentType.equals(getString(R.string.story_main_name))) {

            typeSpinner = (Spinner) v.findViewById(R.id.input_story_type);
            groupSpinner = (Spinner) v.findViewById(R.id.input_story_group);
            typeList = FormComponents.loadTypeDropdown("Indicator", typesJson);

        } else if (fragmentType.equals(getString(R.string.measurement_main_name))) {

            typeSpinner = (Spinner) v.findViewById(R.id.input_measurement_type);
            groupSpinner = (Spinner) v.findViewById(R.id.input_measurement_group);
            typeList = FormComponents.loadTypeDropdown("Domain of change", typesJson);
        }

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
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location finalLocation = FormComponents.getLocation(locationManager, provider);

        if (finalLocation != null) updateFieldsWithLocation(finalLocation, v);
    }

    public void updateFieldsWithLocation(Location location, View v) {
        EditText longitude = null;
        EditText latitude = null;

        if (fragmentType.equals(getString(R.string.event_main_name))) {

            longitude = (EditText) v.findViewById(R.id.input_event_longitude);
            latitude = (EditText) v.findViewById(R.id.input_event_latitude);

        } else if (fragmentType.equals(getString(R.string.story_main_name))) {

            longitude = (EditText) v.findViewById(R.id.input_story_longitude);
            latitude = (EditText) v.findViewById(R.id.input_story_latitude);

        } else if (fragmentType.equals(getString(R.string.measurement_main_name))) {

            longitude = (EditText) v.findViewById(R.id.input_measurement_longitude);
            latitude = (EditText) v.findViewById(R.id.input_measurement_latitude);
        }

        longitude.setText(String.valueOf(location.getLongitude()));
        latitude.setText(String.valueOf(location.getLatitude()));
    }

    public void setOnClickListenerSave(final Context context, final View v) {
        Button submitButton = (Button) v.findViewById(R.id.input_event_submit);
        String toaster = null;
        Integer rObjectFile = null;

        if (fragmentType.equals(getString(R.string.event_main_name))) {
            submitButton = (Button) v.findViewById(R.id.input_event_submit);

        } else if (fragmentType.equals(getString(R.string.story_main_name))) {
            submitButton = (Button) v.findViewById(R.id.input_story_submit);

        } else if (fragmentType.equals(getString(R.string.measurement_main_name))) {
            submitButton = (Button) v.findViewById(R.id.input_measurement_submit);

        }

        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View b) {
                        String toaster = null;
                        Integer rObjectFile = null;

                        if (fragmentType.equals(getString(R.string.event_main_name))) {

                            toaster = "Event saved.";
                            rObjectFile = R.string.event_main_name;
                        } else if (fragmentType.equals(getString(R.string.story_main_name))) {

                            toaster = "Story saved.";
                            rObjectFile = R.string.story_main_name;
                        } else if (fragmentType.equals(getString(R.string.measurement_main_name))) {

                            toaster = "Measurement saved.";
                            rObjectFile = R.string.measurement_main_name;
                        }

                        JSONObject objectJSON = FormComponents.getAllFormData(v, fragmentType);
                        FileServices.saveObjectToFile(context, objectJSON, rObjectFile);

                        Toast.makeText(context, toaster, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

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
