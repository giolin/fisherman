package com.tetrapods.fisherman.law;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tetrapods.fisherman.R;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

/**
 * Created by Ann on 03/02/2018.
 */

public class LawFragment extends DaggerFragment implements LawContract.View{
  @Inject
  public LawFragment(){
    // Requires empty public constructor
  }

  @Inject
  LawPresenter mLawPresenter;
  @BindView(R.id.law_list)
  RecyclerView mLawListRV;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.layout_law,container,false);
    ButterKnife.bind(this,rootView);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mLawListRV.setLayoutManager(linearLayoutManager);
    mLawListRV.setAdapter(new LawListAdapter());

    return rootView;
  }
}
