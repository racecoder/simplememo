package com.racecoder.simplememo.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.racecoder.simplememo.AppApplication;
import com.racecoder.simplememo.R;
import com.racecoder.simplememo.dao.MemoInfoDao;
import com.racecoder.simplememo.entity.MemoInfo;
import com.racecoder.simplememo.util.Const;
import com.racecoder.simplememo.util.ViewActionType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoteWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        final AppApplication application = (AppApplication) getApplication();
        MemoInfoDao memoInfoDao = application.getMemoInfoDao();
        return new ListRemoteViewsFactory(getApplicationContext(), intent, memoInfoDao);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        public List<MemoInfo> memoInfoList = new ArrayList();
        private int appWidgetId;
        private Context context;
        private MemoInfoDao memoInfoDao;
        private boolean firstStart = true;

        public ListRemoteViewsFactory(Context context, Intent intent, MemoInfoDao memoInfoDao) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            this.memoInfoDao = memoInfoDao;
        }

        @Override
        public void onCreate() {
            SharedPreferences sharedPreferences = getSharedPreferences(Const.PREFS_NAME, Context.MODE_PRIVATE);
            firstStart = sharedPreferences.getBoolean(Const.PREFS_KEY_FIRST_START, true);
            if (firstStart) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Const.PREFS_KEY_FIRST_START, false);
                editor.apply();

                if (memoInfoList.isEmpty()) {
                    String lineSeparator = System.lineSeparator();
                    String guideInfo = "欢迎使用极简便签(修改自“Show桌面便签)" + lineSeparator +
                            "1.添加便签请点击插件右上角的“铅笔”。" + lineSeparator +
                            "2.删除任意便签请点击该行左边的“正方形”。" + lineSeparator +
                            "3.编辑任意便签请点击该行。" + lineSeparator +
                            "3.任何问题请发送邮件到raceocder@163.com";

                    MemoInfo guideMemo = new MemoInfo(guideInfo, ZonedDateTime.now());
                    long insertId = memoInfoDao.insert(guideMemo);

                    if (insertId > 0) {
                        memoInfoList.add(guideMemo);
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return memoInfoList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item);

            MemoInfo memoInfo = null;
            try {
                memoInfo = memoInfoList.get(position);
            } catch (Exception ignored) {}
            if (memoInfo == null)
                return remoteViews;

            remoteViews.setTextViewText(R.id.memo_brief, memoInfo.text);

            Intent deleteIntent = new Intent();
            deleteIntent.putExtra("id", memoInfo.id);
            deleteIntent.putExtra("actionType", ViewActionType.DELETE.name());
            remoteViews.setOnClickFillInIntent(R.id.button_delete, deleteIntent);

            Intent editIntent = new Intent();
            editIntent.putExtra("id", memoInfo.id);
            editIntent.putExtra("actionType", ViewActionType.EDIT.name());
            remoteViews.setOnClickFillInIntent(R.id.memo_brief, editIntent);

            return remoteViews;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public void onDataSetChanged() {
            initItems();
        }

        private void initItems() {
            memoInfoList = memoInfoDao.getAll();
        }
    }
}