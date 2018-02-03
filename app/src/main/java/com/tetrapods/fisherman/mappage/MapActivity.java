package com.tetrapods.fisherman.mappage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.tetrapods.fisherman.R;
import com.tetrapods.fisherman.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import dagger.android.support.DaggerAppCompatActivity;

public class MapActivity extends DaggerAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.switch_panel)
    LinearLayout switchPanel;

    @Inject
    MapFragment mapFragment;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
        MapFragment fragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = mapFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.contentFrame);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.setNavigationItemSelectedListener(null);
        drawerLayout.removeDrawerListener(drawerToggle);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @OnCheckedChanged({R.id.switch_my_route, R.id.switch_economy_sea, R.id.switch_marine_sanctuary,
    R.id.switch_port, R.id.switch_fish_distribution})
    public void onRadioButtonClicked(Switch s) {
        boolean checked = s.isChecked();

        switch (s.getId()) {
            case R.id.switch_my_route:
                if (checked) {
                    // TODO switch
                } else {
                    // TODO switch
                }
                break;
            case R.id.switch_economy_sea:
                if (checked) {
                    // TODO switch
                } else {
                    // TODO switch
                }
                break;
            case R.id.switch_marine_sanctuary:
                if (checked) {
                    // TODO switch
                } else {
                    // TODO switch
                }
                break;
            case R.id.switch_port:
                if (checked) {
                    // TODO switch
                } else {
                    // TODO switch
                }
                break;
            case R.id.switch_fish_distribution:
                if (checked) {
                    // TODO switch
                } else {
                    // TODO switch
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_layer:
                if (switchPanel.getVisibility() == View.VISIBLE) {
                    switchPanel.animate()
                            .alpha(0)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    switchPanel.setVisibility(View.INVISIBLE);
                                }
                            }).start();
                } else {
                    switchPanel.animate()
                            .alpha(1)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    switchPanel.setVisibility(View.VISIBLE);
                                }
                            }).start();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                break;
            case R.id.law:
                break;
            case R.id.record:
                break;
            case R.id.track:
                break;
            case R.id.setting:
                break;
            case R.id.about:
                break;
            default:
                break;
        }
        // Close the navigation drawer when an item is selected.
        item.setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }
}
