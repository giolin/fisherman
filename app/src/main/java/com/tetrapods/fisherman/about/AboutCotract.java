package com.tetrapods.fisherman.about;

import com.tetrapods.fisherman.BasePresenter;
import com.tetrapods.fisherman.BaseView;

/**
 * Created by Ann on 04/02/2018.
 */

public interface AboutCotract {
  interface View extends BaseView<AboutCotract.Presenter>{

  }

  interface Presenter extends BasePresenter<AboutCotract.View>{}
}
