package com.racecoder.simplememo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatEditText;

public class LinedEditText extends AppCompatEditText {
    private Paint paint = new Paint();
    // 文本行高
    private int lineHeight;

    private final float startX = 0.0f;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(208, 208, 218));
        paint.setTextSize(getTextSize());

        lineHeight = getLineHeight();
        setGravity(Gravity.TOP); // 使文本框的显示方式为从上到下

        // 这是个巨坑: https://issuetracker.google.com/issues/131284662
        setFallbackLineSpacing(false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 下划线绘制在文字底部，需要设置 lineSpacingExtra 的行间距，才能有空余区域显示出来。
        final float lineSpacingExtra = getLineSpacingExtra() / 2 + 1;
        final float stopX = getWidth();

        // 超过一屏能显示的文本内容没有有下划线
        int actualLineCount = getLineCount();
        int screenCount = (int) Math.ceil(getHeight() / lineHeight);
        int linesToDraw = Math.max(actualLineCount, screenCount) + 1;

        for (int i = 0; i < linesToDraw; i++) {
            float baseline = lineHeight * i - lineSpacingExtra;
            canvas.drawLine(startX, baseline, stopX, baseline, paint);
        }

        super.onDraw(canvas); // 画出光标才能编辑文本
    }
}
