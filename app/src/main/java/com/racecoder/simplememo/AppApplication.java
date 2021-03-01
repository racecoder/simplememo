package com.racecoder.simplememo;

import android.app.Application;

import androidx.room.Room;

import com.racecoder.simplememo.dao.AppDatabase;
import com.racecoder.simplememo.dao.MemoInfoDao;
import com.racecoder.simplememo.util.Const;

import java.util.concurrent.locks.ReentrantLock;

public class AppApplication extends Application {
    private AppDatabase appDatabase;

    private volatile MemoInfoDao memoInfoDao;
    private static final ReentrantLock lock = new ReentrantLock();

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化数据库
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, Const.DATABASE_FILE_NAME)
                .allowMainThreadQueries() // 允许在主线程上访问数据库(可能会block UI)
                .build();
    }

    public MemoInfoDao getMemoInfoDao() {
        if (memoInfoDao == null) {
            lock.lock();
            if (memoInfoDao == null) {
                memoInfoDao = appDatabase.getMemoInfoDao();
            }
            lock.unlock();
        }
        return memoInfoDao;
    }

}