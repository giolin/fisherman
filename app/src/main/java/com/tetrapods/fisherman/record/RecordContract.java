package com.tetrapods.fisherman.record;


import com.tetrapods.fisherman.BasePresenter;
import com.tetrapods.fisherman.BaseView;
import com.tetrapods.fisherman.mappage.MapContract;


/**
 * Created by Ann on 03/02/2018.
 */

public interface RecordContract {
  interface View extends BaseView<Presenter> {

  }

  interface Presenter extends BasePresenter<View> {

  }
}
