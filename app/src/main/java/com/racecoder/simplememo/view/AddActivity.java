package com.racecoder.simplememo.view;

import android.appwidget.AppWidgetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.racecoder.simplememo.AppApplication;
import com.racecoder.simplememo.R;
import com.racecoder.simplememo.dao.MemoInfoDao;
import com.racecoder.simplememo.entity.MemoInfo;
import com.racecoder.simplememo.util.TimeUtil;

import java.time.ZonedDateTime;

public class AddActivity extends AppCompatActivity {
    private final String currentTime = TimeUtil.getCurrentTime();
    private AppApplication application;
    private MemoInfoDao memoInfoDao;
    private AppWidgetManager appWidgetManager;
    private int appWidgetId;
    private boolean cancelSave; // 用户主动cancel时不保存数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        application = (AppApplication) getApplication();
        memoInfoDao = application.getMemoInfoDao();
        appWidgetManager = AppWidgetManager.getInstance(this);

        appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        ((TextView) findViewById(R.id.add_timebar)).setText(currentTime);

        findViewById(R.id.button_save_add).setOnClickListener(view -> saveMemo());
        findViewById(R.id.button_cancel_add).setOnClickListener(view -> {
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
        String memoContent = ((EditText) AddActivity.this.findViewById(R.id.add_content)).getText().toString();
        if (!TextUtils.isEmpty(memoContent)) {
            MemoInfo newMemoInfo = new MemoInfo(memoContent, ZonedDateTime.now());
            memoInfoDao.insert(newMemoInfo);
            // 更新Widget
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);
        }
        finish();
    }
}
