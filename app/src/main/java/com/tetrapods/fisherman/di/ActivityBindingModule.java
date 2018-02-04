package com.tetrapods.fisherman.di;

import com.tetrapods.fisherman.about.AboutModule;
import com.tetrapods.fisherman.addedittask.AddEditTaskModule;
import com.tetrapods.fisherman.addedittask.AddEditTaskActivity;
import com.tetrapods.fisherman.law.LawModule;
import com.tetrapods.fisherman.mappage.MapActivity;
import com.tetrapods.fisherman.mappage.MapModule;
import com.tetrapods.fisherman.record.RecordModule;
import com.tetrapods.fisherman.statistics.StatisticsActivity;
import com.tetrapods.fisherman.statistics.StatisticsModule;
import com.tetrapods.fisherman.taskdetail.TaskDetailActivity;
import com.tetrapods.fisherman.taskdetail.TaskDetailPresenterModule;
import com.tetrapods.fisherman.tasks.TasksActivity;
import com.tetrapods.fisherman.tasks.TasksModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = {MapModule.class, LawModule.class, RecordModule.class,
        AboutModule.class})
    abstract MapActivity mapActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TasksModule.class)
    abstract TasksActivity tasksActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddEditTaskModule.class)
    abstract AddEditTaskActivity addEditTaskActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = StatisticsModule.class)
    abstract StatisticsActivity statisticsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TaskDetailPresenterModule.class)
    abstract TaskDetailActivity taskDetailActivity();
}
