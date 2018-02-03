package com.tetrapods.fisherman.mappage;

import android.support.annotation.IntDef;

import com.tetrapods.fisherman.BasePresenter;
import com.tetrapods.fisherman.BaseView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MapContract {

    int MY_ROUTE = 0;
    int ECONOMY_SEA = 1;
    int MARINE_SANCTUARY = 2;
    int PORT = 3;
    int FISH_DISTRIBUTION = 4;

    @IntDef({MY_ROUTE, ECONOMY_SEA, MARINE_SANCTUARY, PORT, FISH_DISTRIBUTION})
    @Retention(RetentionPolicy.SOURCE)
    @interface MapLayer {}

    interface View extends BaseView<Presenter> {

        void showMapLayer(@MapLayer int type, boolean show);

    }

    interface Presenter extends BasePresenter<View> {

    }
}
