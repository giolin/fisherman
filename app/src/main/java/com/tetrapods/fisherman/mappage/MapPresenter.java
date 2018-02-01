package com.tetrapods.fisherman.mappage;

import javax.inject.Inject;

/**
 * Created by georgelin on 23/01/2018.
 */

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mapView;

    @Inject
    public MapPresenter() {
        // Requires empty public constructor
    }

    @Override
    public void takeView(MapContract.View view) {
        this.mapView = view;
    }

    @Override
    public void dropView() {
        mapView = null;
    }
}
