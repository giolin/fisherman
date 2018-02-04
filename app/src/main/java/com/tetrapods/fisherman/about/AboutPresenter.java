package com.tetrapods.fisherman.about;

import com.tetrapods.fisherman.about.AboutCotract.View;
import javax.inject.Inject;

/**
 * Created by Ann on 04/02/2018.
 */

public class AboutPresenter implements AboutCotract.Presenter {

  private AboutCotract.View view;
  @Inject
  public AboutPresenter(){}
  @Override
  public void takeView(View view) {
    this.view = view;
  }

  @Override
  public void dropView() {
    this.view = null;
  }
}
