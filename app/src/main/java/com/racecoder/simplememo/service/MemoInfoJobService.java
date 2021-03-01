package com.racecoder.simplememo.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.JobIntentService;

import com.racecoder.simplememo.AppApplication;
import com.racecoder.simplememo.R;
import com.racecoder.simplememo.dao.MemoInfoDao;
import com.racecoder.simplememo.entity.MemoInfo;

public class MemoInfoJobService extends JobIntentService {
    private static AppApplication application;
    private static MemoInfoDao memoInfoDao;

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MemoInfoJobService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (application == null) {
            application = (AppApplication) getApplication();
        }
        memoInfoDao = application.getMemoInfoDao();
    }

    @Override
    protected void onHandleWork(Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        long memoInfoId = intent.getLongExtra("memoInfoId", -1);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (memoInfoId < 0 || appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            return;

        memoInfoDao.delete(new MemoInfo(memoInfoId));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        // 触发RemoteViewsService的onDataSetChanged方法
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
