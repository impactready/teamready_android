package org.impactready.teamready;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class FormActivityFragment extends Fragment implements LocationListener {
    private static final String TAG = "Object creation/edit";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int IMAGE_REQUEST_CODE = 100;
    private LocationManager locationManager;
    private String provider;
    private String fragmentType;
    private String action;
    private String objectId;
    private Uri imageLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();
        fragmentType = this.getArguments().getString("object_type");
        objectId = this.getArguments().getString("object_id");
        action = this.getArguments().getString("action");

        View v = null;
        JSONArray objectsJSON;
        JSONObject object = null;
        int filename = 0;

        if (fragmentType.equals(getString(org.impactready.teamready.R.string.event_main_name))) {
            v =  inflater.inflate(org.impactready.teamready.R.layout.activity_form_fragment_event, container, false);
            filename = org.impactready.teamready.R.string.events_filename;

        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.story_main_name))) {
            v =  inflater.inflate(org.impactready.teamready.R.layout.activity_form_fragment_story, container, false);
            filename = R.string.stories_filename;


        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.measurement_main_name))) {
            v =  inflater.inflate(org.impactready.teamready.R.layout.activity_form_fragment_measurement, container, false);
            filename = R.string.measurements_filename;

        }

        if (action.equals("edit") && filename != 0) {
            objectsJSON = FileServices.getFileJSON(context, filename);
            object = JSONServices.find(objectsJSON, objectId);
        }

        if (null == (locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE)))
            getActivity().finish();

        assignObjectDetails(context, v, action, object);
        setupScrolls(context, v, action, object);
        try {
            if (action.equals("new") || object.getString("longitude").equals("") || object.getString("latitude").equals("")) {
                findLocation(v);
                locationManager.requestLocationUpdates(provider, 400, 1, this);
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        setOnClickListeners(context, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        action = this.getArguments().getString("action");

        if (action.equals("new")) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    private void assignObjectDetails(Context context, View v, String action, JSONObject object) {
        EditText objectId = (EditText) v.findViewById(R.id.input_object_id);
        EditText objectType = (EditText) v.findViewById(R.id.input_object_type);

        if (action.equals("new")) {
            objectId.setText(UUID.randomUUID().toString());
            objectType.setText(fragmentType);
        } else {
            try {
                EditText longitude = (EditText) v.findViewById(org.impactready.teamready.R.id.input_longitude);
                EditText latitude = (EditText) v.findViewById(org.impactready.teamready.R.id.input_latitude);
                EditText description = (EditText) v.findViewById(org.impactready.teamready.R.id.input_description);
                ImageView imageView = (ImageView) v.findViewById(org.impactready.teamready.R.id.image_object);
                EditText imageText = (EditText) v.findViewById(org.impactready.teamready.R.id.input_image_location);

                objectId.setText(object.getString("object_id"));
                objectType.setText(object.getString("object_type"));
                description.setText(object.getString("description"));
                longitude.setText(object.getString("longitude"));
                latitude.setText(object.getString("latitude"));

                setViewImageAndLocation(context, imageView, imageText, object.getString("image"));

            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            }
        }

    }

    private void setupScrolls (Context context, View v, String action, JSONObject object) {
        Spinner typeSpinner = null;
        Spinner groupSpinner = null;
        List<String> typeList = null;

        JSONArray typesJson = FileServices.getFileJSON(context, org.impactready.teamready.R.string.types_filename);
        JSONArray groupsJson = FileServices.getFileJSON(context, org.impactready.teamready.R.string.groups_filename);

        if (fragmentType.equals("event")) {

            typeSpinner = (Spinner) v.findViewById(org.impactready.teamready.R.id.input_event_type);
            groupSpinner = (Spinner) v.findViewById(org.impactready.teamready.R.id.input_event_group);
            typeList = FormComponents.loadTypeDropdown("Event type", typesJson);

        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.story_main_name))) {

            typeSpinner = (Spinner) v.findViewById(org.impactready.teamready.R.id.input_story_type);
            groupSpinner = (Spinner) v.findViewById(org.impactready.teamready.R.id.input_story_group);
            typeList = FormComponents.loadTypeDropdown("Indicator", typesJson);

        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.measurement_main_name))) {

            typeSpinner = (Spinner) v.findViewById(org.impactready.teamready.R.id.input_measurement_type);
            groupSpinner = (Spinner) v.findViewById(org.impactready.teamready.R.id.input_measurement_group);
            typeList = FormComponents.loadTypeDropdown("Domain of change", typesJson);
        }

        List<String> groupList = FormComponents.loadGroupDropdown(groupsJson);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, typeList);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, groupList);
        groupSpinner.setAdapter(groupAdapter);

        if (action.equals("edit")) {
            try {
                typeSpinner.setSelection(typeAdapter.getPosition(object.getString("type")));
                groupSpinner.setSelection(groupAdapter.getPosition(object.getString("group")));
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            }
        }

    }

    private void findLocation(View v) {
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location finalLocation = FormComponents.getLocation(locationManager, provider);

        if (finalLocation != null) updateFieldsWithLocation(finalLocation, v);
    }

    private void updateFieldsWithLocation(Location location, View v) {
        EditText longitude;
        EditText latitude;

        longitude = (EditText) v.findViewById(org.impactready.teamready.R.id.input_longitude);
        latitude = (EditText) v.findViewById(org.impactready.teamready.R.id.input_latitude);

        longitude.setText(String.valueOf(location.getLongitude()));
        latitude.setText(String.valueOf(location.getLatitude()));
    }

    private void setOnClickListeners(final Context context, final View v) {
        Button submitButton = null;
        Button imageButton = null;


        if (fragmentType.equals(getString(org.impactready.teamready.R.string.event_main_name))) {
            submitButton = (Button) v.findViewById(org.impactready.teamready.R.id.input_event_submit);
            imageButton = (Button) v.findViewById(org.impactready.teamready.R.id.button_event_image);

        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.story_main_name))) {
            submitButton = (Button) v.findViewById(org.impactready.teamready.R.id.input_story_submit);
            imageButton = (Button) v.findViewById(org.impactready.teamready.R.id.button_story_image);

        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.measurement_main_name))) {
            submitButton = (Button) v.findViewById(org.impactready.teamready.R.id.input_measurement_submit);
            imageButton = (Button) v.findViewById(org.impactready.teamready.R.id.button_measurement_image);
        }

        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View b) {
                        EditText imageText;

                        imageText = (EditText) getView().findViewById(org.impactready.teamready.R.id.input_image_location);

                        if (imageText.getText().toString().equals("")) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            alertDialog.setView(inflater.inflate(org.impactready.teamready.R.layout.dialog_custom, null));

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    saveObject(v);
                                }
                            });

                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alertDialog.show();

                        } else {
                            saveObject(v);
                        }

                    }
                }
        );

        imageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View b) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        imageLocation = FileServices.getOutputMediaFile();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageLocation);

                        startActivityForResult(intent, IMAGE_REQUEST_CODE);
                    }
                }
        );
    }

    private void saveObject(View v) {
        String toaster = null;
        Integer rObjectFile = null;
        Context context = getActivity().getApplicationContext();

        if (fragmentType.equals(getString(org.impactready.teamready.R.string.event_main_name))) {
            toaster = "Event saved.";
            rObjectFile = org.impactready.teamready.R.string.event_main_name;
        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.story_main_name))) {
            toaster = "Story saved.";
            rObjectFile = org.impactready.teamready.R.string.story_main_name;
        } else if (fragmentType.equals(getString(org.impactready.teamready.R.string.measurement_main_name))) {
            toaster = "Measurement saved.";
            rObjectFile = org.impactready.teamready.R.string.measurement_main_name;
        }

        JSONObject objectJSON = FormComponents.getAllFormData(v, fragmentType);
        FileServices.saveObjectToFile(context, objectJSON, rObjectFile);

        Toast.makeText(context, toaster, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

    }

    private void setViewImageAndLocation(Context context, ImageView imageView, EditText imageText, String url) {
        Bitmap imageBitmap = PictureServices.setPicture(context, Uri.parse(url), 400, 6);
        imageView.setImageBitmap(imageBitmap);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = getResources().getDimensionPixelSize(org.impactready.teamready.R.dimen.image_view_height);
        imageView.setLayoutParams(params);
        imageText.setText(url);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView;
        EditText imageText;
        Context context = getActivity().getApplicationContext();

        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {

                imageView = (ImageView) getView().findViewById(org.impactready.teamready.R.id.image_object);
                imageText = (EditText) getView().findViewById(org.impactready.teamready.R.id.input_image_location);

                if (imageLocation != null) {
                    setViewImageAndLocation(context, imageView, imageText, imageLocation.toString());
                    Toast.makeText(getActivity(), "Image saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Image location lost");
                    Toast.makeText(getActivity(), "Image capture failed.", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Image cancelled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Image capture failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
