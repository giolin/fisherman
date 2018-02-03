package com.tetrapods.fisherman.record;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import com.tetrapods.fisherman.R;
import dagger.android.support.DaggerFragment;
import java.util.Calendar;
import javax.inject.Inject;

/**
 * Created by Ann on 03/02/2018.
 */

public class RecordFragment extends DaggerFragment implements RecordContract.View {

  @Inject
  RecordPresenter recordPresenter;
  @Inject
  public RecordFragment(){
    // Requires empty public constructor
  }

  @BindView(R.id.time)
  TextView currentTime;
  @BindView(R.id.et_fishCatch)
  EditText fishCatch;
  String weightSelected;
  @BindView(R.id.et_num)
  EditText taketime;
  String timeSeleced;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.layout_record,container,false);

    FloatingActionButton addFAB = rootView.findViewById(R.id.add_fab);
    addFAB.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_record);

        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance().getTime());

        //currentTime.setText(s);

        TextView location = dialog.findViewById(R.id.location);

        fishCatch = dialog.findViewById(R.id.et_fishCatch);

        AppCompatSpinner weightSpinner = dialog.findViewById(R.id.weight_spinner);
        final String[] weights = view.getResources().getStringArray(R.array.fishCatch);
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(view.getContext(),
            android.R.layout.simple_spinner_dropdown_item,
            weights);
        weightSpinner.setAdapter(weightAdapter);
        weightSpinner.setSelection(0);
        weightSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            weightSelected = weights[pos];
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
        });

        taketime = dialog.findViewById(R.id.et_num);

        AppCompatSpinner timeSpinner = dialog.findViewById(R.id.time_spinner);
        final String[] times = view.getResources().getStringArray(R.array.time);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(view.getContext(),
            android.R.layout.simple_spinner_dropdown_item,
            times);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setSelection(0);
        timeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            timeSeleced = times[pos];
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
        });

        Button save = dialog.findViewById(R.id.btn_save);
        save.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            //TODO: SAVE
            currentTime.getText();
            //location
            fishCatch.getText().toString();
            //weightSelected
            taketime.getText().toString();
            //timeSeleced

          }
        });

        dialog.show();
      }
    });

    return rootView;
  }
}
