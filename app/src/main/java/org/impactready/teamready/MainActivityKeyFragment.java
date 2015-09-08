package org.impactready.teamready;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    }

}
