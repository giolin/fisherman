package com.tetrapods.fisherman.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tetrapods.fisherman.R;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

/**
 * Created by Ann on 04/02/2018.
 */

public class AboutFragment extends DaggerFragment implements AboutCotract.View {

  @Inject
  public AboutFragment(){}
  @Inject
  public AboutPresenter aboutPresenter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.layout_about,container,false);

    return rootView;
  }
}
