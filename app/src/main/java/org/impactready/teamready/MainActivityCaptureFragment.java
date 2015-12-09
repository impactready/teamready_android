package org.impactready.teamready;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivityCaptureFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(org.impactready.teamready.R.layout.activity_main_fragment_capture, container, false);

        setUpButtons(v);

        return v;
    }

    public void setUpButtons(View view) {
        final Context context = getActivity().getApplicationContext();

        Button buttonNewEvent = (Button) view.findViewById(org.impactready.teamready.R.id.button_event_new);
        Button buttonNewStory = (Button)  view.findViewById(org.impactready.teamready.R.id.button_story_new);
        Button buttonNewMeasurement = (Button) view.findViewById(org.impactready.teamready.R.id.button_measurement_new);

        buttonNewEvent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra("object_type", getString(org.impactready.teamready.R.string.event_main_name));
                        intent.putExtra("action", "new");
                        startActivity(intent);
                    }
                }
        );

        buttonNewStory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra("object_type", getString(org.impactready.teamready.R.string.story_main_name));
                        intent.putExtra("action", "new");
                        startActivity(intent);
                    }
                }
        );

        buttonNewMeasurement.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra("object_type", getString(org.impactready.teamready.R.string.measurement_main_name));
                        intent.putExtra("action", "new");
                        startActivity(intent);
                    }
                }
        );
    }

}
