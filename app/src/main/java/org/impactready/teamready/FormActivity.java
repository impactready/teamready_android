package org.impactready.teamready;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class FormActivity extends AppCompatActivity {
    private static final String TAG = "Form Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        String text = getIntent().getStringExtra("type");
        Log.d(TAG, text);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (text.equals(MainActivity.EVENT)) {
            getSupportActionBar().setTitle("New " + MainActivity.EVENT);
            FormActivityEventFragment fragment = new FormActivityEventFragment();
            fragmentTransaction.add(R.id.activity_form_container, fragment);

        } else if (text.equals(MainActivity.STORY)) {
            getSupportActionBar().setTitle("New " + MainActivity.STORY);
            FormActivityStoryFragment fragment = new FormActivityStoryFragment();
            fragmentTransaction.add(R.id.activity_form_container, fragment);

        } else if (text.equals(MainActivity.MEASUREMENT)) {
            getSupportActionBar().setTitle("New indicator " + MainActivity.MEASUREMENT);
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
