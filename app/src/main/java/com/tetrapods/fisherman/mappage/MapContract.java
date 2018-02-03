package com.tetrapods.fisherman.mappage;

import android.content.Context;
import android.support.annotation.IntDef;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tetrapods.fisherman.BasePresenter;
import com.tetrapods.fisherman.BaseView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MapContract {

    int MY_ROUTE = 0;
    int ECONOMY_SEA = 1;
    int MARINE_SANCTUARY = 2;
    int PORT = 3;
    int FISH_CATCH = 4;

    @IntDef({MY_ROUTE, ECONOMY_SEA, MARINE_SANCTUARY, PORT, FISH_CATCH})
    @Retention(RetentionPolicy.SOURCE)
    @interface MapLayer {}

    interface View extends BaseView<Presenter> {

        void addGeoJsonSources(List<GeoJsonSource> geoJsonSources);

        void showMapLayer(@MapLayer int type, boolean show);

    }

    interface Presenter extends BasePresenter<View> {

        void loadGeoJsonFromFile(Context context);

    }
}
