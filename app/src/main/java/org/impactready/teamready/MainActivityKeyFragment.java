package org.impactready.teamready;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivityKeyFragment extends Fragment {
    private static final String TAG = "Account Setup Task";
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();

        View v = inflater.inflate(org.impactready.teamready.R.layout.activity_main_fragment_key, container, false);
        setApiKey(v);
        setUpButtons(context, v);
        setupLists(context, v);
        return v;
    }

    public void setApiKey(View v) {
        TextView apiKey = (TextView) v.findViewById(org.impactready.teamready.R.id.input_key_save);

        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        apiKey.setText(settings.getString("apiKey", ""));
    }

    public void setUpButtons(final Context context, View view) {
        Button buttonSaveKey = (Button) view.findViewById(org.impactready.teamready.R.id.button_key_save);
        Button buttonSetupKey = (Button) view.findViewById(org.impactready.teamready.R.id.button_account_setup);

        buttonSaveKey.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView apiKey = (TextView) getActivity().findViewById(org.impactready.teamready.R.id.input_key_save);

                        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("apiKey", apiKey.getText().toString());
                        editor.commit();

                        apiKey.setText(settings.getString("apiKey", ""));

                        Toast.makeText(context, "API Key saved", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        buttonSetupKey.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);

                        String apiKey = settings.getString("apiKey", "");

                        if (apiKey != "") {
                            progress = ProgressDialog.show(getActivity(), "Sync", "Syncing data, this may take a few minutes...", true);
                            new DataSyncTask().execute(apiKey);

                        } else {
                            Toast.makeText(context, "You do not have an API key set", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
        );
    }

    public void setupLists(Context context, View v) {
        LinearLayout typesList = (LinearLayout) v.findViewById(org.impactready.teamready.R.id.types_list);
        LinearLayout groupsList = (LinearLayout) v.findViewById(org.impactready.teamready.R.id.groups_list);
        typesList.removeAllViews();
        groupsList.removeAllViews();

        JSONArray typesJson = FileServices.getFileJSON(context, org.impactready.teamready.R.string.types_filename);
        JSONArray groupsJson = FileServices.getFileJSON(context, org.impactready.teamready.R.string.groups_filename);

        try {

            for (int i = 0; i < typesJson.length(); i++) {
                JSONObject type = typesJson.getJSONObject(i);
                TextView typeItem = new TextView(context);
                typeItem.setId(i + 200);
                typeItem.setText(type.getString("description") + "\n" + type.getString("usage"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(3,5,5,0);
                layoutParams.height = getResources().getDimensionPixelSize(org.impactready.teamready.R.dimen.setup_item_height);
                typeItem.setLayoutParams(layoutParams);
                typeItem.setBackgroundColor(Color.GRAY);
                typeItem.setTextColor(Color.WHITE);
                typeItem.setGravity(Gravity.CENTER);
                typesList.addView(typeItem);
            }

            for (int i = 0; i < groupsJson.length(); i++) {
                JSONObject group = groupsJson.getJSONObject(i);
                TextView groupItem = new TextView(context);
                groupItem.setId(i + 100);
                groupItem.setText(group.getString("name"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(3,5,5,0);
                layoutParams.height = getResources().getDimensionPixelSize(org.impactready.teamready.R.dimen.setup_item_height);
                groupItem.setLayoutParams(layoutParams);
                groupItem.setHeight(70);
                groupItem.setBackgroundColor(Color.GRAY);
                groupItem.setTextColor(Color.WHITE);
                groupItem.setGravity(Gravity.CENTER);
                groupsList.addView(groupItem);
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

    }

    protected void parseAndSaveJson(StringBuilder jsonString) throws IOException, JSONException {
        final Context context = getActivity().getApplicationContext();

        JSONObject allSetupData = new JSONObject(jsonString.toString());
        JSONArray typesJSON = allSetupData.getJSONArray("types");
        JSONArray groupsJSON = allSetupData.getJSONArray("groups");

        FileServices.writeFileJson(context, org.impactready.teamready.R.string.types_filename, typesJSON);
        FileServices.writeFileJson(context, org.impactready.teamready.R.string.groups_filename, groupsJSON);

    }

    class DataSyncTask extends AsyncTask<String, Void, Integer> {

        protected Integer doInBackground(String... params) {
            Context context = getActivity().getApplicationContext();
            StringBuilder response;
            JSONObject result;
            JSONArray objectsJSON;

            try {

                // The data upload part
                Integer files[] = {org.impactready.teamready.R.string.events_filename,
                    org.impactready.teamready.R.string.stories_filename,
                    org.impactready.teamready.R.string.measurements_filename};

                for (int j = 0; j < files.length; j++) {

                    objectsJSON = FileServices.getFileJSON(context, files[j]);
                    for (int i = 0; i < objectsJSON.length(); i++) {
                        JSONObject thisObject = objectsJSON.getJSONObject(i);
                        if (thisObject.getString("uploaded").equals("no")) {
                            response = NetworkServices.sendObject(params[0], thisObject);
                            if (response == null) return 0;
                            result = new JSONObject(response.toString());
                            if (result.get("result_ok") == true) {
                                JSONArray newObjectsJSON = JSONServices.remove(objectsJSON, thisObject.getString("object_id"));
                                thisObject.put("uploaded", "yes");
                                newObjectsJSON.put(thisObject);
                                FileServices.writeFileJson(context, files[j], newObjectsJSON);
                            } else {
                                return -1;
                            }
                        }
                    }

                }

                // The setup download part
                StringBuilder builder = NetworkServices.getSetup(params[0]);
                if (builder == null || builder.toString() == "") {
                   return 0;
                }
                parseAndSaveJson(builder);
                return 1;

            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
                return 0;
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
                return 0;
            }
        }


        protected void onPostExecute(Integer result) {
            Context context = getActivity().getApplicationContext();
            Log.d(TAG, "Result is: " + result.toString());

            Intent intent = new Intent(getActivity(), MainActivity.class);

            if (result == 1) {
                progress.dismiss();
                intent.putExtra("listview", true);
                Toast.makeText(context, "Sync complete.", Toast.LENGTH_SHORT).show();
            } else if (result == -1) {
                progress.dismiss();
                intent.putExtra("captureview", true);
                Toast.makeText(context, "Could not sync certain items, fields missing.", Toast.LENGTH_SHORT).show();
            } else {
                progress.dismiss();
                intent.putExtra("captureview", true);
                Toast.makeText(context, "Could not connect to the server.", Toast.LENGTH_SHORT).show();
            }

            getActivity().startActivity(intent);
        }

    }

}
