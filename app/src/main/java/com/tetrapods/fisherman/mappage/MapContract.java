package com.tetrapods.fisherman.mappage;

import com.tetrapods.fisherman.BasePresenter;
import com.tetrapods.fisherman.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MapContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
