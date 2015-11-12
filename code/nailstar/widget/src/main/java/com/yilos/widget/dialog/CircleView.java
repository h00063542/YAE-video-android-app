package com.yilos.widget.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * Created by yangdan on 15/11/11.
 */
public class CircleView extends FrameLayout {
    private static final int MSG_RUN = 1;

    private Paint mArcPaint; // 绘制扇形
    private RectF mRectF;

    private int mSweep;
    private int startAngle = 0;
    private int step = 2;
    private boolean startAngleMove = false;

    private boolean stoped = false;

    private int paintColor = Color.BLUE;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RUN) {
                if(stoped) {
                    return;
                }

                if(startAngleMove) {
                    step = startAngle / 36 + 1;
                    startAngle += step;
                    mSweep = 360 - startAngle;
                    if(startAngle >= 360) {
                        step = 2;
                        startAngleMove = false;
                        startAngle = 0;
                        mSweep = startAngle + step;
                    }
                } else {
                    step = (360 - mSweep) / 36 + 1;
                    mSweep += step;

                    if(mSweep >= 360) {
                        step = 2;
                        mSweep = 360;
                        startAngleMove = true;
                    }
                }

                postInvalidate();
                sendEmptyMessage(MSG_RUN);
            }
        }
    };

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.FILL);
        if(getBackground() instanceof ColorDrawable) {
            paintColor = ((ColorDrawable)getBackground()).getColor();
        }

        mRectF = new RectF();

        setBackgroundColor(Color.TRANSPARENT);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void start() {
        startAngle = 0;
        mSweep = 0;
        startAngleMove = false;
        step = 0;
        mHandler.sendEmptyMessage(MSG_RUN);
    }

    public void stop() {
        stoped = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = getMeasuredWidth();
        setMeasuredDimension(size, size);
        mRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());

        mArcPaint.setShader(new SweepGradient(size / 2, size / 2, paintColor, paintColor));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;

        canvas.save();
        if(startAngleMove) {
            canvas.rotate(startAngle, centerX, centerY);
        } else {
            canvas.rotate(mSweep, centerX, centerY);
        }

        canvas.drawArc(mRectF, startAngle, mSweep, true, mArcPaint);
        canvas.restore();
    }
}
