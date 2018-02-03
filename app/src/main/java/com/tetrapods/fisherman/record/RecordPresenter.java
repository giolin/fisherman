package com.tetrapods.fisherman.record;

import com.tetrapods.fisherman.record.RecordContract.View;
import javax.inject.Inject;

/**
 * Created by Ann on 03/02/2018.
 */

public class RecordPresenter implements RecordContract.Presenter {

  private RecordContract.View recordView;
  @Inject
  public RecordPresenter(){
    // Requires empty public constructor
  }

  @Override
  public void takeView(View view) {
    recordView = view;
  }

  @Override
  public void dropView() {
    recordView = null;
  }
}
