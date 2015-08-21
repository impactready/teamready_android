package org.impactready.teamready;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A placeholder fragment containing a simple view.
 */
public class CaptureActivityFragment extends Fragment implements View.OnClickListener {

    public CaptureActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capture, container, false);

        Button buttonNewEvent = (Button) getView().findViewById(R.id.button_event_new);
        Button buttonNewTask = (Button) getView().findViewById(R.id.button_task_new);
        Button buttonNewStory = (Button) getView().findViewById(R.id.button_story_new);
        Button buttonNewMeasurement = (Button) getView().findViewById(R.id.button_measurement_new_new);

        buttonNewEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
