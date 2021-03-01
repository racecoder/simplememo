package com.racecoder.simplememo.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.racecoder.simplememo.service.NoteWidgetService;
import com.racecoder.simplememo.R;
import com.racecoder.simplememo.service.MemoInfoJobService;
import com.racecoder.simplememo.util.ViewActionType;

public class MemoWidget extends AppWidgetProvider {
    public static final String COLLECTION_VIEW_ACTION = "com.racecoder.simplememo.COLLECTION_VIEW_ACTION";
    public static final String COLLECTION_VIEW_ADD = "com.racecoder.simplememo.TEXT_VIEW_ADD";

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        String action = intent.getAction();

        if (COLLECTION_VIEW_ACTION.equals(action)) {
            long memoInfoId = intent.getLongExtra("id", 0);

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            String actionType = intent.getStringExtra("actionType");

            if (ViewActionType.DELETE.name().equals(actionType)) {
                Intent deleteJobServiceIntent = new Intent(context, MemoInfoJobService.class);
                deleteJobServiceIntent.putExtra("memoInfoId", memoInfoId);
                deleteJobServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                MemoInfoJobService.enqueueWork(context, deleteJobServiceIntent);
            } else if (ViewActionType.EDIT.name().equals(actionType)) {
                Intent editIntent = new Intent(context, EditActivity.class);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                editIntent.putExtra("memoInfoId", memoInfoId);
                editIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                context.startActivity(editIntent);
            }

            // 触发RemoteViewsService的onDataSetChanged方法
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent serviceIntent = new Intent(context, NoteWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.memo_widget);
        remoteViews.setRemoteAdapter(R.id.listView, serviceIntent); // 设置ListView的Adapter

        // 添加便签Intent
        Intent addMemoIntent = new Intent(context, AddActivity.class);
        addMemoIntent.setAction(COLLECTION_VIEW_ADD);
        addMemoIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setOnClickPendingIntent(R.id.add, PendingIntent.getActivity(context, appWidgetId, addMemoIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        // 点击Widget Intent
        Intent widgetIntent = new Intent(context, MemoWidget.class);
        widgetIntent.setAction(COLLECTION_VIEW_ACTION);
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setPendingIntentTemplate(R.id.listView, PendingIntent.getBroadcast(context, 0, widgetIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);
    }
}