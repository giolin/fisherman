package com.tetrapods.fisherman.mappage;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.geojson.GeoJsonPlugin;
import com.mapbox.mapboxsdk.plugins.geojson.GeoJsonPluginBuilder;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnLoadingGeoJsonListener;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Filter;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.tetrapods.fisherman.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public class MapFragment extends DaggerFragment implements MapContract.View, OnMapReadyCallback,
        LocationEngineListener, OnLoadingGeoJsonListener {

    private static final String ACCESS_TOKEN = "pk.eyJ1IjoiZ2VvcmdlbGluNDIyIiwiYSI6ImNqYzdqenNwbjJjMnEyd24yZHM5MnhucHMifQ.FqZz7XTRMrHl1A9VzwtxcQ";
    private static final String GEOJSON_SOURCE_ID = "GEOJSONFILE";

    @BindView(R.id.mapView)
    MapView mapView;

    @Inject
    MapPresenter mapPresenter;

    private MapboxMap mapboxMap;
    private LocationLayerPlugin locationLayerPlugin;
    private LocationEngine locationEngine;
    private PermissionsManager permissionsManager;
    private Activity parentActivity;
    private GeoJsonPlugin geoJsonPlugin;

    @Inject
    public MapFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(parentActivity, ACCESS_TOKEN);
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        ButterKnife.bind(this, rootView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
//        createGeoJsonSource();
//        addPolygonLayer();
        Timber.d("onMapReady");
        geoJsonPlugin = new GeoJsonPluginBuilder()
                .withContext(parentActivity)
                .withMap(this.mapboxMap)
                .withOnLoadingFileAssets(this)
                .build();
        geoJsonPlugin.setAssetsName("data.geojson");
        enableLocationPlugin();
        getLifecycle().addObserver(locationLayerPlugin);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(parentActivity)) {
            // Create an instance of LOST location engine
            initLocationEngine();
            locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
            locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            Timber.d("PermissionsManager");
            permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                    // do nothing intended
                }

                @Override
                public void onPermissionResult(boolean granted) {
                    Timber.d("onPermissionResult");
                    if (granted) {
                        enableLocationPlugin();
                    } else {
                        Toast.makeText(parentActivity, "You didn't grant location permissions.",
                                Toast.LENGTH_LONG).show();
                        parentActivity.finish();
                    }
                }
            });
            permissionsManager.requestLocationPermissions(parentActivity);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void initLocationEngine() {
        locationEngine = new LostLocationEngine(parentActivity);
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private void setCameraPosition(Location location) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        parentActivity = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void createGeoJsonSource() {
        // Load data from GeoJSON file in the assets folder
        GeoJsonSource geoJsonSource = new GeoJsonSource(GEOJSON_SOURCE_ID,
                loadJsonFileFromAssets("data.geojson"));
        mapboxMap.addSource(geoJsonSource);
    }

    private void addPolygonLayer() {
        // Create and style a FillLayer that uses the Polygon Feature's coordinates in the GeoJSON data
        FillLayer borderOutlineLayer = new FillLayer("polygon", GEOJSON_SOURCE_ID);
        borderOutlineLayer.setProperties(
                PropertyFactory.fillColor(Color.RED),
                PropertyFactory.fillOpacity(.4f));
        borderOutlineLayer.setFilter(Filter.eq("$type", "Polygon"));
        mapboxMap.addLayer(borderOutlineLayer);
    }

    private String loadJsonFileFromAssets(String filename) {
        try {
            InputStream is = parentActivity.getAssets().open(filename);
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

    @Override
    public void onPreLoading() {
        Timber.d("onPreLoading");
    }

    @Override
    public void onLoaded() {
        Timber.d("onLoaded");
    }

    @Override
    public void onLoadFailed(Exception e) {
        Timber.e(e, "onLoadFailed");

    }
}
