package org.impactready.teamready;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "protea.io";
    private static final String TAG = "Main Activity";

    public static final Integer TAB_COUNT = 3;
    public static final Integer CAPTURE_VIEW = 0;
    public static final Integer LIST_VIEW = 1;
    public static final Integer API_KEY_VIEW = 2;

    private MainFragmentAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(org.impactready.teamready.R.layout.activity_main);

        ViewPager vpPager = (ViewPager) findViewById(org.impactready.teamready.R.id.activity_main_container);
        adapterViewPager = new MainFragmentAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        setTabActionBar(vpPager);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("apiKey", "").equals("") || getIntent().getBooleanExtra("captureview", false) == true) {
            vpPager.setCurrentItem(API_KEY_VIEW);
        } else if (getIntent().getBooleanExtra("listview", false) == true) {
            vpPager.setCurrentItem(LIST_VIEW);
        } else {
            vpPager.setCurrentItem(CAPTURE_VIEW);
        }


    }

    public void setTabActionBar(final ViewPager vpPager) {
        final TabLayout tabLayout = (TabLayout) findViewById(org.impactready.teamready.R.id.activity_main_tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Capture"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));

        vpPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        );

        tabLayout.setOnTabSelectedListener(
            new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    vpPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    vpPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
            }
        );



    }


}
