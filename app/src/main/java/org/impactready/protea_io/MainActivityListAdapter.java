package org.impactready.protea_io;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivityListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[][] values;

    public MainActivityListAdapter(Context context, int layoutId, String[][] values, String[] keys) {
        super(context, layoutId, keys);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_main_fragment_list_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_image_thumbnail);
        TextView textView1 = (TextView) rowView.findViewById(R.id.item_first_line);
        TextView textView2 = (TextView) rowView.findViewById(R.id.item_second_line);

        Uri imageLocation = Uri.parse(values[position][2]);
        imageView.setImageURI(imageLocation);
        textView1.setText(values[position][0]);
        textView2.setText(values[position][1]);

        return rowView;
    }
}
