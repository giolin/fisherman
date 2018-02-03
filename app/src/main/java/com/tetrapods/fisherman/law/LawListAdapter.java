package com.tetrapods.fisherman.law;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.tetrapods.fisherman.R;
import com.tetrapods.fisherman.law.LawListAdapter.ViewHolder;

/**
 * Created by Ann on 03/02/2018.
 */

public class LawListAdapter extends RecyclerView.Adapter<ViewHolder> {
  String[] mLaws = {"流刺網法","底拖網法"};
  int[] resIcon = {R.drawable.flow_gill_net,R.drawable.bottom_trawl};

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.law_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.mCheckBox.setText(mLaws[position]);
    holder.mIcon.setImageResource(resIcon[position]);
  }

  @Override
  public int getItemCount() {
    return mLaws.length;
  }

  public class ViewHolder extends RecyclerView.ViewHolder{
    public ImageView mIcon;
    public CheckBox mCheckBox;

    public ViewHolder(View itemView) {
      super(itemView);
      mCheckBox = itemView.findViewById(R.id.checkbox_law);
      mIcon = itemView.findViewById(R.id.law_icon);
    }
  }


}
