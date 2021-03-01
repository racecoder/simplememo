package com.racecoder.simplememo.view;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.racecoder.simplememo.AppApplication;
import com.racecoder.simplememo.R;
import com.racecoder.simplememo.dao.MemoInfoDao;
import com.racecoder.simplememo.entity.MemoInfo;
import com.racecoder.simplememo.util.TimeUtil;

import java.time.ZonedDateTime;

public class EditActivity extends Activity {
    private AppApplication application;
    private MemoInfoDao memoInfoDao;
    private int appWidgetId;
    private long memoInfoId;
    private boolean cancelSave; // 用户主动cancel时不保存数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        application = (AppApplication) getApplication();
        memoInfoDao = application.getMemoInfoDao();

        Intent intent = getIntent();
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        memoInfoId = intent.getLongExtra("memoInfoId", 0);
        MemoInfo memoInfo = memoInfoDao.findById(memoInfoId);

        // 回显数据
        ((EditText) findViewById(R.id.edit_content)).setText(memoInfo.getText());
        ((TextView) findViewById(R.id.edit_timebar)).setText(TimeUtil.formatPattern(memoInfo.getUpdateTime()));

        findViewById(R.id.button_save_edit).setOnClickListener(view -> saveMemo());
        findViewById(R.id.button_cancel_edit).setOnClickListener(view -> {
            cancelSave = true;
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cancelSave)
            return;
        saveMemo();
    }

    private void saveMemo() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(EditActivity.this);
        String text = ((EditText) EditActivity.this.findViewById(R.id.edit_content)).getText().toString();
        MemoInfo updateInfo = new MemoInfo(memoInfoId, text, ZonedDateTime.now());
        memoInfoDao.updateMemoInfo(updateInfo);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);
        finish();
    }
}
