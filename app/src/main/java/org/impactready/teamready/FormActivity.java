package org.impactready.teamready;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class FormActivity extends AppCompatActivity {
    private static final String TAG = "Form Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.impactready.teamready.R.layout.activity_form);

        if (savedInstanceState == null) {

            String text = getIntent().getStringExtra("type");
            Log.d(TAG, text);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("type", text);

            if (text.equals(getString(org.impactready.teamready.R.string.event_main_name))) {
                getSupportActionBar().setTitle("protea.io : New " + getString(org.impactready.teamready.R.string.event_main_name));

            } else if (text.equals(getString(org.impactready.teamready.R.string.story_main_name))) {
                getSupportActionBar().setTitle("protea.io :  New " + getString(org.impactready.teamready.R.string.story_main_name));

            } else if (text.equals(getString(org.impactready.teamready.R.string.measurement_main_name))) {
                getSupportActionBar().setTitle("protea.io : New " + getString(org.impactready.teamready.R.string.measurement_main_name));
            }

            FormActivityFragment fragment = new FormActivityFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.add(org.impactready.teamready.R.id.activity_form_container, fragment);
            fragmentTransaction.commit();
        }
    }

}
