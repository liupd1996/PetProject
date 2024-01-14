package com.example.petproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BatteryView extends View {

    // 其他代码...
    private int mWidth;
    private int mHeight;
    public static final int WRAP_WIDTH = 40;
    public static final int WRAP_HEIGHT = 20;
    private int lowBatteryThreshold = 20; // 设置低电量阈值

    private int highBatteryColor = Color.GREEN; // 高电量颜色
    private int lowBatteryColor = Color.RED; // 低电量颜色

    private double batteryLevel = 100; // 电量百分比，默认100%
    private Paint paint = new Paint();

    public BatteryView(Context context) {
        super(context);
        init();
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化操作，比如设置画笔颜色、背景等
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WRAP_WIDTH, WRAP_HEIGHT);
            mWidth = WRAP_WIDTH;
            mHeight = WRAP_HEIGHT;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WRAP_WIDTH, mHeight);
            mWidth = WRAP_WIDTH;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, WRAP_HEIGHT);
            mHeight = WRAP_HEIGHT;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        // 画电池外框
        RectF rect = new RectF((float) 2.5, (float) 2.5, mWidth-10, mHeight-(float) 2.5);
        canvas.drawRect(rect, paint);


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        if (batteryLevel <=0) {
            return;
        }
        // 计算电量所占的比例
        int batteryLevelWidth = (int) (((float) batteryLevel / 100) * (mWidth - 15));
        // 画电量部分
        canvas.drawRect(5, 5, 5 + batteryLevelWidth, mHeight - 5, paint);
        // 画电池头部分

        paint.setColor(Color.BLACK); // 设置画笔颜色为黑色
        paint.setStyle(Paint.Style.FILL); // 设置画笔样式为线条

        // 绘制电池头部（正极），可以绘制一个圆形或者矩形
        float headHeight = mHeight * 0.5f; // 电池头部高度
        RectF headRect = new RectF(mWidth - 10, (int)(mHeight/2 - headHeight/2), mWidth,(int)(mHeight/2 + headHeight/2));
        canvas.drawRect(headRect, paint);
    }

    // 设置电池电量并根据电量改变颜色
    public void setBatteryLevel(double level) {
        this.batteryLevel = level;
        changeBatteryColor(); // 根据电量改变颜色
        invalidate(); // 重新绘制 View
    }

    // 根据电量改变颜色
    private void changeBatteryColor() {
        if (batteryLevel <= lowBatteryThreshold) {
            // 低于等于阈值时使用低电量颜色
            paint.setColor(lowBatteryColor);
        } else {
            // 高于阈值时使用高电量颜色
            paint.setColor(highBatteryColor);
        }
    }

    // 设置低电量阈值
    public void setLowBatteryThreshold(int threshold) {
        this.lowBatteryThreshold = threshold;
    }

    // 设置高电量颜色
    public void setHighBatteryColor(int color) {
        this.highBatteryColor = color;
    }

    // 设置低电量颜色
    public void setLowBatteryColor(int color) {
        this.lowBatteryColor = color;
    }
}

