package org.impactready.teamready;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class MainActivityKeyFragment extends Fragment {
    private static final String TAG = "Account Setup Task";
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity().getApplicationContext();

        View v = inflater.inflate(R.layout.activity_main_fragment_key, container, false);
        setApiKey(v);
        setUpButtons(context, v);
        setupLists(context, v);
        return v;
    }

    public void setApiKey(View v) {
        TextView apiKey = (TextView) v.findViewById(R.id.input_key_save);

        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        apiKey.setText(settings.getString("apiKey", ""));
    }

    public void setUpButtons(final Context context, View view) {


        Button buttonSaveKey = (Button) view.findViewById(R.id.button_key_save);
        Button buttonSetupKey = (Button) view.findViewById(R.id.button_account_setup);

        buttonSaveKey.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView apiKey = (TextView) getActivity().findViewById(R.id.input_key_save);

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
                            progress = ProgressDialog.show(getActivity(), "Setup", "Downloading setup..", true);
                            new AccountSetupTask().execute(apiKey);
                        } else {
                            Toast.makeText(context, "You do not have an API key set", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
        );
    }

    public void setupLists(Context context, View v) {
        LinearLayout typesList = (LinearLayout) v.findViewById(R.id.types_list);
        LinearLayout groupsList = (LinearLayout) v.findViewById(R.id.groups_list);
        typesList.removeAllViews();
        groupsList.removeAllViews();

        JSONArray typesJson = FileServices.getSetup(R.string.types_filename, context);
        JSONArray groupsJson = FileServices.getSetup(R.string.groups_filename, context);

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
                typeItem.setLayoutParams(layoutParams);
                typeItem.setHeight(70);
                typeItem.setBackgroundColor(Color.DKGRAY);
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
                groupItem.setLayoutParams(layoutParams);
                groupItem.setHeight(70);
                groupItem.setBackgroundColor(Color.DKGRAY);
                groupItem.setTextColor(Color.WHITE);
                groupItem.setGravity(Gravity.CENTER);
                groupsList.addView(groupItem);
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

    }

    class AccountSetupTask extends AsyncTask<String, Void, Integer> {

        protected Integer doInBackground(String... params) {
            int groupCount = 0;
            int typeCount = 0;
            InputStream is = null;
            HttpsURLConnection conn = null;
            BufferedReader reader = null;
            String url = "https://impactready.herokuapp.com/api/v1/android/setup";
            String userCredentials = "api:" + params[0];
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));
            String contentAsString = "";

            try {
                conn = (HttpsURLConnection) new URL(url).openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", basicAuth);

                is = new BufferedInputStream(conn.getInputStream());

                InputStreamReader streamReader = new InputStreamReader(is);
                reader = new BufferedReader(streamReader);
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }

                parseAndSaveJson(builder);


            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException", e);
            } finally {
                if (is != null) {
                    conn.disconnect();
                }
            }
            Log.e(TAG, contentAsString);

            return 1;
        }


        protected void onPostExecute(Integer result) {
            Context context = getActivity().getApplicationContext();
            Log.d(TAG, "Result is ok: " + result.toString());

            setupLists(context, getView());
            progress.dismiss();
            Toast.makeText(context, "API Key saved", Toast.LENGTH_SHORT).show();

        }

    }

    protected void parseAndSaveJson(StringBuilder jsonString) throws IOException, JSONException {
        final Context context = getActivity().getApplicationContext();

        JSONObject allSetupData = new JSONObject(jsonString.toString());
        JSONArray typesJSON = allSetupData.getJSONArray("types");
        JSONArray groupsJSON = allSetupData.getJSONArray("groups");

        FileServices.writeFileJson(context, R.string.types_filename, typesJSON);
        FileServices.writeFileJson(context, R.string.groups_filename, groupsJSON);

    }

    protected void writeFile(Context context, String filename, JSONArray json) throws IOException {
        FileOutputStream fos1 =  context.openFileOutput(filename, Context.MODE_PRIVATE);
        fos1.write(json.toString().getBytes());
        fos1.close();
    }

    protected String readFile(Context context, String filename) throws IOException {
        FileInputStream fos1 =  context.openFileInput(filename);
        byte[] bytesFromFile = new byte[(int) fos1.available()];
        fos1.read(bytesFromFile);
        fos1.close();

        return new String(bytesFromFile, "UTF-8");
    }

}
