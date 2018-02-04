package com.tetrapods.fisherman.about;

import com.tetrapods.fisherman.di.ActivityScoped;
import com.tetrapods.fisherman.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Ann on 04/02/2018.
 */
@Module
public abstract class AboutModule {
  @FragmentScoped
  @ContributesAndroidInjector
  abstract AboutFragment aboutFragment();

  @ActivityScoped
  @Binds
  abstract AboutCotract.Presenter aboutPresenter(AboutPresenter aboutPresenter);
}
