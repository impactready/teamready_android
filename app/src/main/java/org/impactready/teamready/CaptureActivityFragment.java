package org.impactready.teamready;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class CaptureActivityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_capture, container, false);

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
                        CharSequence text = "Event!";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        buttonNewTask.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence text = "Task!";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        buttonNewStory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence text = "Story!";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        buttonNewMeasurement.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence text = "Measurement!";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}
