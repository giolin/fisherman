package com.tetrapods.fisherman.tasks;

import com.tetrapods.fisherman.di.ActivityScoped;
import com.tetrapods.fisherman.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link TasksPresenter}.
 */
@Module
public abstract class TasksModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract TasksFragment tasksFragment();

    @ActivityScoped
    @Binds abstract TasksContract.Presenter taskPresenter(TasksPresenter presenter);
}
