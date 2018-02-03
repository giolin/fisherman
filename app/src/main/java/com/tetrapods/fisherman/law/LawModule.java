package com.tetrapods.fisherman.law;

import com.tetrapods.fisherman.di.ActivityScoped;
import com.tetrapods.fisherman.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Ann on 03/02/2018.
 */
@Module
public abstract class LawModule {

  @FragmentScoped
  @ContributesAndroidInjector
  abstract LawFragment lawFragment();

  @ActivityScoped
  @Binds
  abstract LawContract.Presenter lawPresenter(LawPresenter presenter);
}
