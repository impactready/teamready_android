package org.impactready.teamready;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class ListActivityFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = android.R.layout.simple_list_item_activated_1;

        String[] items = {"Item 1", "Item 2", "Item 3"};

        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, items));
    }

}
