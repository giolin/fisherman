package com.tetrapods.fisherman.mappage;

import com.tetrapods.fisherman.di.ActivityScoped;
import com.tetrapods.fisherman.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link MapPresenter}.
 */
@Module
public abstract class MapModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MapFragment mapFragment();

    @ActivityScoped
    @Binds
    abstract MapContract.Presenter mapPresenter(MapPresenter presenter);
}
