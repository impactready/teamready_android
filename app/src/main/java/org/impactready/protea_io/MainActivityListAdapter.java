package org.impactready.protea_io;

import android.content.Context;
import android.content.Intent;
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
    private String listItemId = null;
    private String listItemType = null;
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
            imageView.setImageURI(imageLocation);
            deleteView.setImageResource(R.drawable.image_delete);
        }

        textView1.setText(values[position][0]);
        textView2.setText(values[position][1]);
        listItemId = values[position][3];
        listItemType = values[position][4];

        deleteView.setOnClickListener(new View.OnClickListener() {
            JSONArray objectsJSON = null;
            Integer fileId = null;

            @Override
            public void onClick(View v) {
                switch (listItemType) {
                    case "event":
                        fileId = R.string.events_filename;
                        break;
                    case "story":
                        fileId = R.string.stories_filename;
                        break;
                    case "measurement":
                        fileId = R.string.measurements_filename;
                        break;
                    default:
                        fileId = null;
                        break;
                }

                try {
                    if (fileId != null) {
                        objectsJSON = FileServices.getFileJSON(context, fileId);

                        JSONArray newObjectsJSON = new JSONArray();
                        for (int i = 0; i < objectsJSON.length(); i++) {
                            JSONObject thisObject = objectsJSON.getJSONObject(i);
                            if (thisObject.getString("id") != listItemId) {
                                newObjectsJSON.put(thisObject);
                            }
                        }
                        FileServices.writeFileJson(context, fileId, newObjectsJSON);
                        Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }





                } catch (JSONException e) {
                    Log.e(TAG, "JSONException", e);
                } catch (IOException e) {
                    Log.e(TAG, "IOException", e);
                }
            }
        });

        return rowView;
    }

}
