package org.impactready.teamready;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class FormActivity extends Activity {
    private static final String TAG = "Form Activity";

    private static final String EVENT = "event";
    private static final String TASK = "task";
    private static final String STORY = "story";
    private static final String MEASUREMENT = "measurement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        String text = getIntent().getStringExtra("type");
        Log.d(TAG, text);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (text.equals(EVENT)) {
            getActionBar().setTitle("New " + EVENT);
            FormActivityEventFragment fragment = new FormActivityEventFragment();
            fragmentTransaction.add(R.id.activity_form_container, fragment);

        } else if (text.equals(TASK)) {
            getActionBar().setTitle("New " + TASK);
            FormActivityTaskFragment fragment = new FormActivityTaskFragment();
            fragmentTransaction.add(R.id.activity_form_container, fragment);

        } else if (text.equals(STORY)) {
            getActionBar().setTitle("New " + STORY);
            FormActivityStoryFragment fragment = new FormActivityStoryFragment();
            fragmentTransaction.add(R.id.activity_form_container, fragment);

        } else if (text.equals(MEASUREMENT)) {
            getActionBar().setTitle("New indicator " + MEASUREMENT);
            FormActivityMeasurementFragment fragment = new FormActivityMeasurementFragment();
            fragmentTransaction.add(R.id.activity_form_container, fragment);
        }

        fragmentTransaction.commit();

//        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "Menu inflated..");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
