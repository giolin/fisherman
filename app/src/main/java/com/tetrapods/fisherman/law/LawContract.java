package com.tetrapods.fisherman.law;

import com.tetrapods.fisherman.BasePresenter;
import com.tetrapods.fisherman.BaseView;

/**
 * Created by Ann on 03/02/2018.
 */

public interface LawContract {

  interface View extends BaseView<LawContract.Presenter> {

  }

  interface Presenter extends BasePresenter<LawContract.View> {

  }
}
