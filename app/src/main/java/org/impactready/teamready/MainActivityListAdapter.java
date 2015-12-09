package org.impactready.teamready;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;

public class MainActivityListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[][] values;
    private static final String TAG = "List adapter";

    public MainActivityListAdapter(Context context, int layoutId, String[][] values, String[] keys) {
        super(context, layoutId, keys);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(org.impactready.teamready.R.layout.activity_main_fragment_list_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(org.impactready.teamready.R.id.item_image_thumbnail);
        ImageView deleteView = (ImageView) rowView.findViewById(org.impactready.teamready.R.id.item_image_delete);
        TextView textView1 = (TextView) rowView.findViewById(org.impactready.teamready.R.id.item_first_line);
        TextView textView2 = (TextView) rowView.findViewById(org.impactready.teamready.R.id.item_second_line);
        TextView uploadView = (TextView) rowView.findViewById(org.impactready.teamready.R.id.item_upload_line);


        if (values[position][0].equals("Events") || values[position][0].equals("Stories") || values[position][0].equals("Measurements")) {

            switch (values[position][0]) {
                case "Events":
                    imageView.setImageResource(org.impactready.teamready.R.drawable.image_event);
                    break;
                case "Stories":
                    imageView.setImageResource(org.impactready.teamready.R.drawable.image_story);
                    break;
                case "Measurements":
                    imageView.setImageResource(org.impactready.teamready.R.drawable.image_measurement);
                    break;
            }

        } else {

            Uri imageLocation = Uri.parse(values[position][2]);
            Bitmap imageBitmap = PictureServices.setPicture(context, imageLocation, 100, 8);
            imageView.setImageBitmap(imageBitmap);

            deleteView.setImageResource(android.R.drawable.ic_delete);
            deleteView.setTag(values[position][3]);
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Integer files[] = {org.impactready.teamready.R.string.events_filename,
                                org.impactready.teamready.R.string.stories_filename,
                                org.impactready.teamready.R.string.measurements_filename};

                        for (int j = 0; j < files.length; j++) {

                            JSONArray objectsJSON = FileServices.getFileJSON(context, files[j]);

                            JSONArray newObjectsJSON = JSONServices.remove(objectsJSON, v.getTag().toString());
                            FileServices.writeFileJson(context, files[j], newObjectsJSON);
                            Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("listview", true);
                            context.startActivity(intent);
                        }


                    } catch (IOException e) {
                        Log.e(TAG, "IOException", e);
                    }
                }
            });
            rowView.setTag(values[position][3]);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (values[position][5].equals("")) {

                    } else {
                        Intent intent = new Intent(context, FormActivity.class);
                        intent.putExtra("object_type", values[position][5]);
                        intent.putExtra("object_id", v.getTag().toString());
                        intent.putExtra("action", "edit");
                        context.startActivity(intent);
                    }
                }
            });
        }

        if (values[position][0].length() > 25) {
            textView1.setText(values[position][0].substring(0, 22) + "...");
        } else {
            textView1.setText(values[position][0]);
        }

        if (values[position][1].length() > 25) {
            textView2.setText(values[position][1].substring(0, 22) + "...");
        } else {
            textView2.setText(values[position][1]);
        }

        uploadView.setText(values[position][4]);

        return rowView;
    }

}
