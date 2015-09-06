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

        View v = inflater.inflate(R.layout.activity_main_fragment_capture, container, false);

        setUpButtons(v);

        return v;
    }

    public void setUpButtons(View v) {
        final Context context = getActivity().getApplicationContext();

        Button buttonNewEvent = (Button) v.findViewById(R.id.button_event_new);
        Button buttonNewTask = (Button)  v.findViewById(R.id.button_task_new);
        Button buttonNewStory = (Button)  v.findViewById(R.id.button_story_new);
        Button buttonNewMeasurement = (Button) v.findViewById(R.id.button_measurement_new);

        buttonNewEvent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra("type", "event");
                        startActivity(intent);
                    }
                }
        );

        buttonNewTask.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra("type", "task");
                        startActivity(intent);
                    }
                }
        );

        buttonNewStory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra("type", "story");
                        startActivity(intent);
                    }
                }
        );

        buttonNewMeasurement.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra("type", "measurement");
                        startActivity(intent);
                    }
                }
        );
    }

}
