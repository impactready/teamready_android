package org.impactready.teamready;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class MainListActivityFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = android.R.layout.simple_list_item_activated_1;

        String[] keys = {"Item 1", "Item 2", "Item 3"};
        String[][] items = {
                {"Item 1a", "Item 1b"},
                {"Item 2a", "Item 2b"},
                {"Item 3a", "Item 3b"}
        };

        setListAdapter(new MainListActivityAdapter(getActivity(), layout, items, keys));
    }

}
