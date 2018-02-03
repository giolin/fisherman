package com.tetrapods.fisherman.law;

import javax.inject.Inject;

/**
 * Created by Ann on 03/02/2018.
 */

public class LawPresenter implements LawContract.Presenter {

  private LawContract.View mView;
  @Inject
  public LawPresenter(){
    // Requires empty public constructor
  }

  @Override
  public void takeView(LawContract.View view) {
    this.mView = view;
  }

  @Override
  public void dropView() {
    this.mView = null;
  }
}
