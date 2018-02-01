package com.tetrapods.fisherman.data.source;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.tetrapods.fisherman.data.source.local.TasksDao;
import com.tetrapods.fisherman.data.source.local.TasksLocalDataSource;
import com.tetrapods.fisherman.data.source.local.ToDoDatabase;
import com.tetrapods.fisherman.data.source.remote.TasksRemoteDataSource;
import com.tetrapods.fisherman.util.AppExecutors;
import com.tetrapods.fisherman.util.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This is used by Dagger to inject the required arguments into the {@link TasksRepository}.
 */
@Module
public class TasksRepositoryModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Provides
    @Local
    TasksDataSource provideTasksLocalDataSource(TasksDao dao, AppExecutors executors) {
        return new TasksLocalDataSource(executors, dao);
    }

    @Singleton
    @Provides
    @Remote
    TasksDataSource provideTasksRemoteDataSource() {
        return new TasksRemoteDataSource();
    }

    @Singleton
    @Provides
    ToDoDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), ToDoDatabase.class, "Tasks.db")
                .build();
    }

    @Singleton
    @Provides
    TasksDao provideTasksDao(ToDoDatabase db) {
        return db.taskDao();
    }

    @Singleton
    @Provides
    AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
