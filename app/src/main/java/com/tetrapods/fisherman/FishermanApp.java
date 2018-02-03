package com.tetrapods.fisherman;

import android.app.Application;
import android.support.annotation.VisibleForTesting;
import com.tetrapods.fisherman.data.fishRecord.DaoMaster;
import com.tetrapods.fisherman.data.fishRecord.DaoSession;
import com.tetrapods.fisherman.data.fishRecord.FishRecordDao;
import com.tetrapods.fisherman.data.source.TasksRepository;
import com.tetrapods.fisherman.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

import javax.inject.Inject;

/**
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class FishermanApp extends DaggerApplication {

    private static DaoSession daoSession;
    @Inject
    TasksRepository tasksRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "fish.db");
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public static FishRecordDao FishRecordDao(){
        return daoSession.getFishRecordDao();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    /**
     * Our Espresso tests need to be able to get an instance of the {@link TasksRepository}
     * so that we can delete all tasks before running each test
     */
    @VisibleForTesting
    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }
}
