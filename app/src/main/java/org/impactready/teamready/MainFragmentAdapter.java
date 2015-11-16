package org.impactready.teamready;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private final int NUM_ITEMS = 2;
    private Context context;

    public MainFragmentAdapter(FragmentManager fragManager) {
        super(fragManager);
    }

    @Override
    public int getCount() {
        return MainActivity.TAB_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MainActivityCaptureFragment captureFragment = new MainActivityCaptureFragment();
                return captureFragment;
            case 1:
                MainActivityListFragment listFragment = new MainActivityListFragment();
                return listFragment;
            case 2:
                MainActivityKeyFragment keyFragment= new MainActivityKeyFragment();
                return keyFragment;
            default:
                return null;
        }


    }
}
