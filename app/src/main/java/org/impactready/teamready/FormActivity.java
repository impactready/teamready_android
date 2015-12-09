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

            String object_type = getIntent().getStringExtra("object_type");
            String object_id = getIntent().getStringExtra("object_id");
            String action = getIntent().getStringExtra("action");
            Log.d(TAG, object_type);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("object_type", object_type);
            bundle.putString("object_id", object_id);
            bundle.putString("action", action);

            if (object_type.equals(getString(org.impactready.teamready.R.string.event_main_name))) {
                getSupportActionBar().setTitle("TeamReady: New " + getString(org.impactready.teamready.R.string.event_main_name));

            } else if (object_type.equals(getString(org.impactready.teamready.R.string.story_main_name))) {
                getSupportActionBar().setTitle("TeamReady:  New " + getString(org.impactready.teamready.R.string.story_main_name));

            } else if (object_type.equals(getString(org.impactready.teamready.R.string.measurement_main_name))) {
                getSupportActionBar().setTitle("TeamReady: New " + getString(org.impactready.teamready.R.string.measurement_main_name));
            }

            FormActivityFragment fragment = new FormActivityFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.add(org.impactready.teamready.R.id.activity_form_container, fragment);
            fragmentTransaction.commit();
        }
    }

}
