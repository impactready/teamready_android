package org.impactready.teamready;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivityKeyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main_fragment_key, container, false);
        setApiKey(v);
        setUpButtons(v);
        return v;
    }

    public void setApiKey(View v) {
        TextView apiKey = (TextView) v.findViewById(R.id.input_key_save);

        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        apiKey.setText(settings.getString("apiKey", ""));
    }

    public void setUpButtons(View view) {
        final Context context = getActivity().getApplicationContext();

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
                            new AccountSetupTask().execute("apiKey");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setMessage("You do not have an API key set.")
                                    .setTitle("API Key not set");

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    }

                }
        );
    }

    class AccountSetupTask extends AsyncTask<String, Void, Void> {
        private static final String TAG = "Account Setup Task";

        protected Void doInBackground(String... params) {
            int groupCount = 0;
            int typeCount = 0;
            InputStream is = null;
            HttpURLConnection conn = null;
            String url = "http://impactready.herokuapp.com/api/v1/android/setup";
            String userCredentials = "api:" + params;
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
            String contentAsString = "";

            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setRequestProperty ("Authorization", basicAuth);

                is = new BufferedInputStream(conn.getInputStream());
                
                contentAsString = is.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                Log.e(TAG, "IOException");
            } finally {
                if (is != null) {
                    conn.disconnect();
                }
            }
            Log.e(TAG, contentAsString);
//            return contentAsString;
            return null;
        }


        protected void onPostExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Your account setup is complete.")
                    .setTitle("Setup complete");

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }


}
