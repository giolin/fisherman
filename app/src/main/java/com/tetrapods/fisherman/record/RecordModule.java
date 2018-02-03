package com.tetrapods.fisherman.record;

import com.tetrapods.fisherman.di.ActivityScoped;
import com.tetrapods.fisherman.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Ann on 03/02/2018.
 */
@Module
public abstract class RecordModule {

  @FragmentScoped
  @ContributesAndroidInjector
  abstract RecordFragment recordFragment();

  @ActivityScoped
  @Binds
  abstract RecordContract.Presenter recordPresenter(RecordPresenter presenter);
}
