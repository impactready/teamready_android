package org.impactready.protea_io;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivityListFragment extends ListFragment {
    private static final String TAG = "Listing";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getActivity().getApplicationContext();
        int layout = android.R.layout.simple_list_item_activated_1;


        JSONArray eventsJSON = FileServices.getFileJSON(context, R.string.events_filename);
        JSONArray storiesJSON = FileServices.getFileJSON(context, R.string.stories_filename);
        JSONArray measurementsJSON = FileServices.getFileJSON(context, R.string.measurements_filename);
        int array_length = eventsJSON.length() + storiesJSON.length()+ measurementsJSON.length() + 3;
        int counter = 0;

        String[] keys = new String[array_length];
        String[][] items = new String[array_length][4];

        try {

            keys[counter] = "Events";
            items[counter][0] = "Events";
            items[counter][1] = "";
            items[counter][2] = "";
            items[counter][3] = "";


            if (eventsJSON.length() > 0) {

                for (int i = 0; i < eventsJSON.length(); i++) {
                    counter++;
                    JSONObject event = eventsJSON.getJSONObject(i);
                    keys[counter] = event.getString("description");

                    items[counter][0] = event.getString("description");
                    items[counter][1] = event.getString("type");
                    items[counter][2] = event.getString("image");
                    items[counter][3] = event.getString("id");

                }
            }

            counter++;

            keys[counter] = "Stories";
            items[counter][0] = "Stories";
            items[counter][1] = "";
            items[counter][2] = "";
            items[counter][3] = "";

            if (storiesJSON.length() > 0) {

                for (int i = 0; i < storiesJSON.length(); i++) {
                    counter++;
                    JSONObject story = storiesJSON.getJSONObject(i);
                    keys[counter] = story.getString("description");

                    items[counter][0] = story.getString("description");
                    items[counter][1] = story.getString("type");
                    items[counter][2] = story.getString("image");
                    items[counter][3] = story.getString("id");

                }
            }


            counter++;

            keys[counter] = "Measurements";
            items[counter][0] = "Measurements";
            items[counter][1] = "";
            items[counter][2] = "";
            items[counter][3] = "";

            if (measurementsJSON.length() > 0) {

                for (int i = 0; i < measurementsJSON.length(); i++) {
                    counter++;
                    JSONObject measurement = measurementsJSON.getJSONObject(i);
                    keys[counter] = measurement.getString("description");

                    items[counter][0] = measurement.getString("description");
                    items[counter][1] = measurement.getString("type");
                    items[counter][2] = measurement.getString("image");
                    items[counter][3] = measurement.getString("id");

                }
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

        setListAdapter(new MainActivityListAdapter(getActivity(), layout, items, keys));
    }

}
