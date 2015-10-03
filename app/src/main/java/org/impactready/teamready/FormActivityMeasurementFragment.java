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

import java.util.List;

public class FormActivityMeasurementFragment extends Fragment implements LocationListener {

    private static final String TAG = "Measurement creation";
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();

        View v =  inflater.inflate(R.layout.activity_form_fragment_measurement, container, false);
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
        Spinner typeSpinner = (Spinner) v.findViewById(R.id.input_measurement_type);
        Spinner groupSpinner = (Spinner) v.findViewById(R.id.input_measurement_group);

        JSONArray typesJson = FileServices.getSetup(R.string.types_filename, context);
        JSONArray groupsJson = FileServices.getSetup(R.string.groups_filename, context);

        List<SpinnerElement> typeList = FormComponents.loadTypeDropdown("Indicator", typesJson);
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
        EditText longitude = (EditText) v.findViewById(R.id.input_measurement_longitude);
        EditText latitude = (EditText) v.findViewById(R.id.input_measurement_latitude);

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
