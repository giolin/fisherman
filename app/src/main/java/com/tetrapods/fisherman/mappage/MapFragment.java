package com.tetrapods.fisherman.mappage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnLoadingGeoJsonListener;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Filter;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.tetrapods.fisherman.R;
import com.tetrapods.fisherman.util.SanctuaryDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public class MapFragment extends DaggerFragment implements MapContract.View, OnMapReadyCallback,
        LocationEngineListener, OnLoadingGeoJsonListener, MapboxMap.OnMapClickListener {

    private static final String ACCESS_TOKEN = "pk.eyJ1IjoiZ2VvcmdlbGluNDIyIiwiYSI6ImNqYzdqenNwbjJjMnEyd24yZHM5MnhucHMifQ.FqZz7XTRMrHl1A9VzwtxcQ";
    static final String GEO_JSON_MY_ROUTE_SOURCE_ID = "GEO_JSON_MY_ROUTE_SOURCE_ID";
    private static final String GEO_JSON_MY_ROUTE_LAYER_ID = "GEO_JSON_MY_ROUTE_LAYER_ID";
    static final String GEO_JSON_PATH1_SOURCE_ID = "GEO_JSON_PATH1_SOURCE_ID";
    private static final String GEO_JSON_PATH1_LAYER_ID = "GEO_JSON_PATH1_LAYER_ID";
    static final String GEO_JSON_PATH2_SOURCE_ID = "GEO_JSON_PATH2_SOURCE_ID";
    private static final String GEO_JSON_PATH2_LAYER_ID = "GEO_JSON_PATH2_LAYER_ID";
    static final String GEO_JSON_ECONOMY_SEA_SOURCE_ID = "GEO_JSON_ECONOMY_SEA_SOURCE_ID";
    private static final String GEO_JSON_ECONOMY_SEA_LAYER_ID = "GEO_JSON_ECONOMY_SEA_LAYER_ID";
    static final String GEO_JSON_MARINE_SANCTUARY_SOURCE_ID = "GEO_JSON_MARINE_SANCTUARY_SOURCE_ID";
    private static final String GEO_JSON_MARINE_SANCTUARY_LAYER_ID = "GEO_JSON_MARINE_SANCTUARY_LAYER_ID";
    static final String GEO_JSON_PORT_SOURCE_ID = "GEO_JSON_PORT_SOURCE_ID";
    private static final String GEO_JSON_PORT_LAYER_ID = "GEO_JSON_PORT_LAYER_ID";
    static final String GEO_JSON_FISH_CATCH_SOURCE_ID = "GEO_JSON_FISH_CATCH_SOURCE_ID";
    private static final String GEO_JSON_FISH_CATCH_LAYER_ID = "GEO_JSON_FISH_CATCH_LAYER_ID";
    private static final int ZOOM = 11;
    private static final float LINE_WIDTH = 3f;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.latlon_text)
    TextView latlonText;

    @Inject
    MapPresenter mapPresenter;

    private MapboxMap mapboxMap;
    private LocationLayerPlugin locationLayerPlugin;
    private LocationEngine locationEngine;
    private PermissionsManager permissionsManager;
    private Activity parentActivity;
    private LineLayer myRouteLayer;
    private LineLayer path1Layer;
    private LineLayer path2Layer;
    private FillLayer economySeaLayer;
    private FillLayer marineSanctuaryLayer;
    private SymbolLayer fishCatchLayer;
    private SymbolLayer portsLayer;

    @Inject
    public MapFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = getActivity();
        mapPresenter.takeView(this);
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
        mapPresenter.loadGeoJsonFromFile(parentActivity);
        mapboxMap.addOnMapClickListener(this);
        addImageSource();
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
            locationLayerPlugin.applyStyle(R.style.CustomLocationLayer);
            locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                    // do nothing intended
                }

                @Override
                public void onPermissionResult(boolean granted) {
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
            double lat = lastLocation.getLatitude();
            double lon = lastLocation.getLongitude();
            String str = getString(R.string.current_location, Math.abs(lat), lat > 0 ? "°N" : "°S",
                    Math.abs(lon), lon > 0 ? "°E" : "°W");
            latlonText.setText(str);
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
                new LatLng(location.getLatitude(), location.getLongitude()), ZOOM));
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
        mapPresenter.dropView();
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

    private void addImageSource() {
        mapboxMap.addImage("anchor-image",
                BitmapFactory.decodeResource(parentActivity.getResources(), R.drawable.anchor));
    }

    @Override
    public void addGeoJsonSources(List<GeoJsonSource> geoJsonSources) {
        for (GeoJsonSource geoJsonSource : geoJsonSources) {
            mapboxMap.addSource(geoJsonSource);
        }
        addPolygonLayer();
        addLineLayer();
        addSymbolLayer();
        ((MapActivity) parentActivity).showSwitchPanel();
    }

    private void addPolygonLayer() {
        economySeaLayer = new FillLayer(GEO_JSON_ECONOMY_SEA_LAYER_ID,
                GEO_JSON_ECONOMY_SEA_SOURCE_ID);
        economySeaLayer.setProperties(
                PropertyFactory.visibility(Property.VISIBLE),
                PropertyFactory.fillColor(Color.BLUE),
                PropertyFactory.fillOpacity(.4f));
        economySeaLayer.setFilter(Filter.eq("$type", "Polygon"));
        mapboxMap.addLayer(economySeaLayer);
        marineSanctuaryLayer = new FillLayer(GEO_JSON_MARINE_SANCTUARY_LAYER_ID,
                GEO_JSON_MARINE_SANCTUARY_SOURCE_ID);
        marineSanctuaryLayer.setProperties(
                PropertyFactory.visibility(Property.VISIBLE),
                PropertyFactory.fillColor(Color.RED),
                PropertyFactory.fillOpacity(.2f));
        marineSanctuaryLayer.setFilter(Filter.eq("$type", "Polygon"));
        mapboxMap.addLayer(marineSanctuaryLayer);
    }

    private void addLineLayer() {
        myRouteLayer = new LineLayer(GEO_JSON_MY_ROUTE_LAYER_ID, GEO_JSON_MY_ROUTE_SOURCE_ID);
        myRouteLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(LINE_WIDTH),
                PropertyFactory.lineColor(Color.GREEN)
        );
        mapboxMap.addLayer(myRouteLayer);
        path1Layer = new LineLayer(GEO_JSON_PATH1_LAYER_ID, GEO_JSON_PATH1_SOURCE_ID);
        path1Layer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(LINE_WIDTH),
                PropertyFactory.lineColor(Color.BLUE)
        );
        mapboxMap.addLayer(path1Layer);
        path2Layer = new LineLayer(GEO_JSON_PATH2_LAYER_ID, GEO_JSON_PATH2_SOURCE_ID);
        path2Layer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(LINE_WIDTH),
                PropertyFactory.lineColor(Color.RED)
        );
        mapboxMap.addLayer(path2Layer);
    }

    private void addSymbolLayer() {
        portsLayer = new SymbolLayer(GEO_JSON_PORT_LAYER_ID, GEO_JSON_PORT_SOURCE_ID)
                .withProperties(PropertyFactory.iconImage("anchor-image"));
        mapboxMap.addLayer(portsLayer);
        fishCatchLayer = new SymbolLayer(GEO_JSON_FISH_CATCH_LAYER_ID,
                GEO_JSON_FISH_CATCH_SOURCE_ID)
                .withProperties(PropertyFactory.iconImage("marker-15"));
        mapboxMap.addLayer(fishCatchLayer);
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

    @Override
    public void showMapLayer(int type, boolean show) {
        switch (type) {
            case MapContract.MY_ROUTE: {
                if (show) {
                    myRouteLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
                    path1Layer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
                    path2Layer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
                } else {
                    myRouteLayer.setProperties(PropertyFactory.visibility(Property.NONE));
                    path1Layer.setProperties(PropertyFactory.visibility(Property.NONE));
                    path2Layer.setProperties(PropertyFactory.visibility(Property.NONE));
                }
                break;
            }
            case MapContract.ECONOMY_SEA: {
                if (show) {
                    economySeaLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
                } else {
                    economySeaLayer.setProperties(PropertyFactory.visibility(Property.NONE));
                }
                break;
            }
            case MapContract.MARINE_SANCTUARY: {
                if (show) {
                    marineSanctuaryLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
                } else {
                    marineSanctuaryLayer.setProperties(PropertyFactory.visibility(Property.NONE));
                }
                break;
            }
            case MapContract.PORT: {
                if (show) {
                    portsLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
                } else {
                    portsLayer.setProperties(PropertyFactory.visibility(Property.NONE));
                }
                break;
            }
            case MapContract.FISH_CATCH: {
                if (show) {
                    fishCatchLayer.setProperties(
                            PropertyFactory.visibility(Property.VISIBLE));
                } else {
                    fishCatchLayer.setProperties(
                            PropertyFactory.visibility(Property.NONE));
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        Timber.d("onMapClick");
        PointF pointf = mapboxMap.getProjection().toScreenLocation(point);
        RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10,
                pointf.y + 10);
        for (com.mapbox.services.commons.geojson.Feature feature
                : mapboxMap.queryRenderedFeatures(rectF, GEO_JSON_MARINE_SANCTUARY_LAYER_ID)) {
            Dialog dialog = new SanctuaryDialog(parentActivity,
                    feature.getStringProperty("name"));
            dialog.show();
        }

        for (com.mapbox.services.commons.geojson.Feature feature
                : mapboxMap.queryRenderedFeatures(rectF, GEO_JSON_PORT_LAYER_ID)) {
            new AlertDialog.Builder(parentActivity)
                    .setMessage(feature.getStringProperty("LMName"))
                    .show();
        }

        for (com.mapbox.services.commons.geojson.Feature feature
                : mapboxMap.queryRenderedFeatures(rectF, GEO_JSON_FISH_CATCH_LAYER_ID)) {
            double catchKg = (double) feature.getNumberProperty("Catch_kg");
            double catchRate = (double) feature.getNumberProperty("Catch_rate");
            String str = getString(R.string.fish_catch_info,
                    feature.getStringProperty("Start_time"),
                    feature.getStringProperty("End_time"),
                    feature.getStringProperty("total_time"),
                    catchKg,
                    catchRate
            );
            new AlertDialog.Builder(parentActivity)
                    .setTitle(feature.getStringProperty("Date"))
                    .setMessage(str)
                    .show();
        }
    }
}
