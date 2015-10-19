package org.impactready.protea_io;

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
import org.json.JSONException;
import org.json.JSONObject;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_main_fragment_list_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_image_thumbnail);
        ImageView deleteView = (ImageView) rowView.findViewById(R.id.item_image_delete);
        TextView textView1 = (TextView) rowView.findViewById(R.id.item_first_line);
        TextView textView2 = (TextView) rowView.findViewById(R.id.item_second_line);


        if (values[position][0] == "Events" || values[position][0] == "Stories" || values[position][0] == "Measurements") {

            switch (values[position][0]) {
                case "Events":
                    imageView.setImageResource(R.drawable.image_event);
                    break;
                case "Stories":
                    imageView.setImageResource(R.drawable.image_story);
                    break;
                case "Measurements":
                    imageView.setImageResource(R.drawable.image_measurement);
                    break;
            }

        } else {

            Uri imageLocation = Uri.parse(values[position][2]);
            Bitmap imageBitmap = PictureServices.setPicture(context, imageLocation, 100, 8);
            imageView.setImageBitmap(imageBitmap);

            deleteView.setImageResource(android.R.drawable.ic_delete);
            deleteView.setTag(values[position][3]);
            deleteView.setOnClickListener(new View.OnClickListener() {
                JSONArray objectsJSON = null;

                @Override
                public void onClick(View v) {

                    try {

                        Integer files[] = {R.string.events_filename,
                                R.string.stories_filename,
                                R.string.measurements_filename};

                        for (int j = 0; j < files.length; j++) {

                            objectsJSON = FileServices.getFileJSON(context, files[j]);

                            JSONArray newObjectsJSON = new JSONArray();
                            for (int i = 0; i < objectsJSON.length(); i++) {
                                JSONObject thisObject = objectsJSON.getJSONObject(i);
                                if (!thisObject.getString("id").equals(v.getTag().toString())) {
                                    newObjectsJSON.put(thisObject);
                                }
                            }
                            FileServices.writeFileJson(context, files[j], newObjectsJSON);
                            Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("listview", true);
                            context.startActivity(intent);
                        }


                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException", e);
                    } catch (IOException e) {
                        Log.e(TAG, "IOException", e);
                    }
                }
            });
        }

        textView1.setText(values[position][0]);
        textView2.setText(values[position][1]);

        return rowView;
    }

}
