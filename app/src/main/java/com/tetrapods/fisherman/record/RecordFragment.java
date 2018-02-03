package com.tetrapods.fisherman.record;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.location.Location;
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
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;
import com.tetrapods.fisherman.FishermanApp;
import com.tetrapods.fisherman.R;
import com.tetrapods.fisherman.data.fishRecord.FishRecord;
import dagger.android.support.DaggerFragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

  TextView currentTime;
  EditText fishCatch;
  String weightSelected;
  EditText taketime;
  String timeSeleced;

  LineChart fishingLineChart;
  LineChart catchesLineChart;
  String[] weights;
  String[] times;
  TextView location;

  List<FishRecord> recordList;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.layout_record,container,false);
    ButterKnife.bind(this,rootView);

    fishingLineChart = rootView.findViewById(R.id.fishing_lineChart);
    catchesLineChart = rootView.findViewById(R.id.catches_lineChart);

    fishingLineChart.setTouchEnabled(false);
    fishingLineChart.setDrawGridBackground(false);
    fishingLineChart.setScaleEnabled(false);
    fishingLineChart.setScaleXEnabled(false);
    fishingLineChart.setScaleYEnabled(false);
    fishingLineChart.setPinchZoom(true);

    catchesLineChart.setTouchEnabled(false);
    catchesLineChart.setDrawGridBackground(false);
    catchesLineChart.setScaleEnabled(false);
    catchesLineChart.setScaleXEnabled(false);
    catchesLineChart.setScaleYEnabled(false);
    catchesLineChart.setPinchZoom(true);

    recordList = FishermanApp.FishRecordDao().loadAll();
    if (recordList.size()>0){
      setFishingData(fishingLineChart,45,100);
    }
    if (recordList.size()>0){
      setCatchesData(catchesLineChart);
    }

    weights = getResources().getStringArray(R.array.fishCatch);
    times = getResources().getStringArray(R.array.time);
    FloatingActionButton addFAB = rootView.findViewById(R.id.add_fab);
    addFAB.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.dialog_record);

        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance().getTime());
        currentTime = dialog.findViewById(R.id.time);
        currentTime.setText(s);

        location = dialog.findViewById(R.id.location);
        LostLocationEngine locationEngine = new LostLocationEngine(view.getContext());
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();
        @SuppressLint("MissingPermission")
        Location lastLocation = locationEngine.getLastLocation();
        final double lat = lastLocation.getLatitude();
        final double lon = lastLocation.getLongitude();
        String str = getString(R.string.location, Math.abs(lat), lat > 0 ? "°N" : "°S",
            Math.abs(lon), lon > 0 ? "°E" : "°W");
        location.setText(str);

        fishCatch = dialog.findViewById(R.id.et_fishCatch);
        fishCatch = dialog.findViewById(R.id.et_fishCatch);

        AppCompatSpinner weightSpinner = dialog.findViewById(R.id.weight_spinner);
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
            FishRecord fishRecord = new FishRecord();
            fishRecord.setStartTime(currentTime.getText().toString());
            fishRecord.setLatitude(String.valueOf(lat));
            fishRecord.setLongitude(String.valueOf(lon));
            fishRecord.setCatches(fishCatch.getText().toString());
            fishRecord.setWeightUnit(weightSelected);
            fishRecord.setTotalTime(taketime.getText().toString());
            fishRecord.setTimeUnit(timeSeleced);
            FishermanApp.FishRecordDao().insert(fishRecord);

            recordList = FishermanApp.FishRecordDao().loadAll();
            setFishingData(fishingLineChart,45,100);
            setCatchesData(catchesLineChart);
            
            dialog.dismiss();
          }
        });

        Button cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            dialog.dismiss();
          }
        });

        dialog.show();
      }
    });

    return rootView;
  }

  private void setFishingData(LineChart lineChart,int count, float range) {

    lineChart.getXAxis().setEnabled(false);
    ArrayList<Entry> values = new ArrayList<Entry>();

    for (int i = 0; i < recordList.size() && recordList.size()>0; i++) {
      FishRecord record = recordList.get(i);
      int catches = Integer.parseInt(record.getCatches());
      int time = Integer.parseInt(record.getTotalTime());

      if (record.getWeightUnit().equals("t")){
        catches = TonneToKg(catches);
      }

      if (record.getTimeUnit().equals("hr")){
        time = hrToMinutes(time);
      }

      float val = catches/time;
      values.add(new Entry(i, val, null));
    }

    LineDataSet set1;

    if (lineChart.getData() != null &&
        lineChart.getData().getDataSetCount() > 0) {
      set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
      set1.setValues(values);
      lineChart.getData().notifyDataChanged();
      lineChart.notifyDataSetChanged();
    } else {
      // create a dataset and give it a type
      set1 = new LineDataSet(values, "釣獲率");

      set1.setDrawIcons(false);

      // set the line to be drawn like this "- - - - - -"
      set1.enableDashedLine(10f, 5f, 0f);
      set1.enableDashedHighlightLine(10f, 5f, 0f);
      set1.setColor(Color.BLACK);
      set1.setCircleColor(Color.BLACK);
      set1.setLineWidth(1f);
      set1.setCircleRadius(3f);
      set1.setDrawCircleHole(false);
      set1.setValueTextSize(9f);
      set1.setDrawFilled(true);
      set1.setFormLineWidth(1f);
      set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
      set1.setFormSize(15.f);

      ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
      dataSets.add(set1); // add the datasets

      // create a data object with the datasets
      LineData data = new LineData(dataSets);

      // set data
      lineChart.setData(data);
    }
  }

  private void setCatchesData(LineChart lineChart) {

    lineChart.getXAxis().setEnabled(false);

    ArrayList<Entry> values = new ArrayList<Entry>();

    for (int i = 0; i < recordList.size() && recordList.size()>0; i++) {
      if (recordList.get(i).getWeightUnit().equals("t")){
        int tonne = Integer.parseInt(recordList.get(i).getCatches());
        values.add(new Entry(i, TonneToKg(tonne), null));
      }else {
        values.add(new Entry(i, Float.parseFloat(recordList.get(i).getCatches()), null));
      }
    }

    LineDataSet set1;

    if (lineChart.getData() != null &&
        lineChart.getData().getDataSetCount() > 0) {
      set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
      set1.setValues(values);
      lineChart.getData().notifyDataChanged();
      lineChart.notifyDataSetChanged();
    } else {
      // create a dataset and give it a type
      set1 = new LineDataSet(values, "漁獲量");

      set1.setDrawIcons(false);

      // set the line to be drawn like this "- - - - - -"
      set1.enableDashedLine(10f, 5f, 0f);
      set1.enableDashedHighlightLine(10f, 5f, 0f);
      set1.setColor(Color.BLACK);
      set1.setCircleColor(Color.BLACK);
      set1.setLineWidth(1f);
      set1.setCircleRadius(3f);
      set1.setDrawCircleHole(false);
      set1.setValueTextSize(9f);
      set1.setDrawFilled(true);
      set1.setFormLineWidth(1f);
      set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
      set1.setFormSize(15.f);

      ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
      dataSets.add(set1); // add the datasets

      // create a data object with the datasets
      LineData data = new LineData(dataSets);

      // set data
      lineChart.setData(data);
    }
  }

  private int TonneToKg(int tonne){
    return tonne*1000;
  }

  private int hrToMinutes(int hr){
    return hr*60;
  }

}
