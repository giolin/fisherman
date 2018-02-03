package com.tetrapods.fisherman.mappage;

import android.content.Context;

import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.tetrapods.fisherman.mappage.MapFragment.GEO_JSON_ECONOMY_SEA_SOURCE_ID;
import static com.tetrapods.fisherman.mappage.MapFragment.GEO_JSON_FISH_CATCH_SOURCE_ID;
import static com.tetrapods.fisherman.mappage.MapFragment.GEO_JSON_MARINE_SANCTUARY_SOURCE_ID;
import static com.tetrapods.fisherman.mappage.MapFragment.GEO_JSON_MY_ROUTE_SOURCE_ID;
import static com.tetrapods.fisherman.mappage.MapFragment.GEO_JSON_PATH1_SOURCE_ID;
import static com.tetrapods.fisherman.mappage.MapFragment.GEO_JSON_PATH2_SOURCE_ID;
import static com.tetrapods.fisherman.mappage.MapFragment.GEO_JSON_PORT_SOURCE_ID;

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
        Timber.d("takeView");
        this.mapView = view;
    }

    @Override
    public void dropView() {
        mapView = null;
    }

    @Override
    public void loadGeoJsonFromFile(final Context context) {
        Observable.fromCallable(new Callable<List<GeoJsonSource>>() {
            @Override
            public List<GeoJsonSource> call() throws Exception {
                List<GeoJsonSource> geoJsonSources = new ArrayList<>();
                geoJsonSources.add(new GeoJsonSource(GEO_JSON_MARINE_SANCTUARY_SOURCE_ID,
                        loadJsonFileFromAssets("marine_sanctuary.geojson", context)));
                geoJsonSources.add(new GeoJsonSource(GEO_JSON_ECONOMY_SEA_SOURCE_ID,
                        loadJsonFileFromAssets("eez_eastasia.geojson", context)));
                geoJsonSources.add(new GeoJsonSource(GEO_JSON_MY_ROUTE_SOURCE_ID,
                        loadJsonFileFromAssets("ship_route1.geojson", context)));
                geoJsonSources.add(new GeoJsonSource(GEO_JSON_PATH1_SOURCE_ID,
                        loadJsonFileFromAssets("path1.geojson", context)));
                geoJsonSources.add(new GeoJsonSource(GEO_JSON_PATH2_SOURCE_ID,
                        loadJsonFileFromAssets("path2.geojson", context)));
                geoJsonSources.add(new GeoJsonSource(GEO_JSON_PORT_SOURCE_ID,
                        loadJsonFileFromAssets("ports_taiwan.geojson", context)));
                geoJsonSources.add(new GeoJsonSource(GEO_JSON_FISH_CATCH_SOURCE_ID,
                        loadJsonFileFromAssets("catch_data_fake.geojson", context)));
                return geoJsonSources;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GeoJsonSource>>() {
                    @Override
                    public void accept(List<GeoJsonSource> geoJsonSources) throws Exception {
                        mapView.addGeoJsonSources(geoJsonSources);
                    }
                });
    }

    private String loadJsonFileFromAssets(String filename, Context context) {
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
