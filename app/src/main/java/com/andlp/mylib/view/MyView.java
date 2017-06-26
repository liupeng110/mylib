package com.andlp.mylib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 717219917@qq.com  2017/6/26 14:41
 */

public class MyView extends View{


    public MyView(Context context) {
        super(context );
        initPaint();
    }


    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }


    // 1.创建一个画笔
    private Paint mPaint = new Paint();

    // 2.初始化画笔
    private void initPaint() {
        mPaint.setColor(Color.BLACK);        //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);   //设置画笔模式为填充
//      mPaint.setStyle(Paint.Style.STROKE); //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);          //设置画笔宽度为10px
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoint(canvas);
    }

    private void drawPoint(Canvas canvas){
        canvas.drawPoint(200, 200, mPaint);     //在坐标(200,200)位置绘制一个点
        canvas.drawPoints(new float[]{          //绘制一组点，坐标位置由float数组指定
                500,500,
                500,600,
                500,700
        },mPaint);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas){
        canvas.drawLine(300,300,500,600,mPaint);    // 在坐标(300,300)(500,600)之间绘制一条直线
        canvas.drawLines(new float[]{               // 绘制一组线 每四数字(两个点的坐标)确定一条线
                100,200,200,200,
                100,300,200,300
        },mPaint);
        drawReact(canvas);
    }

    private void drawReact(Canvas canvas){      //矩形
        canvas.drawRect(100,100,800,400,mPaint);//第一种

//        Rect rect = new Rect(100,100,800,400);//第二种
//        canvas.drawRect(rect,mPaint);

//        RectF rectF = new RectF(100,100,800,400);//第三种
//        canvas.drawRect(rectF,mPaint);
        drawRoundRect(canvas);
    }

    private void drawRoundRect(Canvas canvas){
//        RectF rectF0 = new RectF(100,100,800,400);
//        canvas.drawRoundRect(rectF0,30,30,mPaint); // 第一种
//        canvas.drawRoundRect(100,100,800,400,30,30,mPaint);// 第二种  api 21   5.0以上
        RectF rectF = new RectF(100,100,800,400);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(rectF,mPaint); // 绘制背景矩形
        mPaint.setColor(Color.BLUE);   // 绘制圆角矩形
        canvas.drawRoundRect(rectF,700,400,mPaint);
        drawOval(canvas);
    }


    private void drawOval(Canvas canvas){
        RectF rectF = new RectF(100,100,800,400);
        canvas.drawOval(rectF,mPaint);           // 第一种
//      canvas.drawOval(100,100,800,400,mPaint); // 第二种
//      canvas.drawCircle(500,500,400,mPaint);  // 绘制一个圆心坐标在(500,500)，半径为400 的圆。

        RectF rectF_ = new RectF(100,100,600,600);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(rectF_,mPaint);          //绘制背景矩形

        mPaint.setColor(Color.BLUE);
        canvas.drawArc(rectF_,0,90,false,mPaint);//绘制圆弧

        RectF rectF2 = new RectF(100,700,600,1200);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(rectF2,mPaint);          // 绘制背景矩形

        mPaint.setColor(Color.BLUE);
        canvas.drawArc(rectF2,0,90,true,mPaint); // 绘制圆弧
        translate(canvas);
    }


    private void translate(Canvas canvas){

        // 在坐标原点绘制一个黑色圆形
        mPaint.setColor(Color.BLACK);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,mPaint);

        // 在坐标原点绘制一个蓝色圆形
        mPaint.setColor(Color.BLUE);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,mPaint);


        mPaint.setColor(Color.BLUE);
        canvas.translate(0,200);
        canvas.drawCircle(0,0,100,mPaint);

        // 将坐标系原点移动到画布正中心
        canvas.translate(1080 /4, 1920 /4);

        RectF rect = new RectF(0,-400,400,0);   // 矩形区域
        mPaint.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect,mPaint);
        canvas.scale(0.5f,0.5f,100,0);          // 画布缩放  加上 200，0
        mPaint.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect,mPaint);


        // 将坐标系原点移动到画布正中心
        canvas.translate(1080 /4, 1920 /4);
        RectF rect3 = new RectF(0,-400,400,0);   // 矩形区域
        mPaint.setColor(Color.BLACK);            // 绘制黑色矩形
        canvas.drawRect(rect3,mPaint);

        canvas.scale(-0.5f,-0.5f);              // 画布缩放
        mPaint.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect3,mPaint);

////------圆环
        mPaint.setStyle(Paint.Style.STROKE);      //设置画笔模式为 空心
        canvas.translate(1080 /4, 1920 /4);
        canvas.drawCircle(0,0,400,mPaint);          // 绘制两个圆形
        canvas.drawCircle(0,0,380,mPaint);

        for (int i=0; i<=360; i+=10){               // 绘制圆形之间的连接线
            canvas.drawLine(0,380,0,400,mPaint);
            canvas.rotate(10);
        }
//--------圆环


         //新图层
        mPaint.setColor(Color.RED);
        canvas.drawCircle(175, 175, 175, mPaint);
        canvas.saveLayerAlpha(0, 0, 200, 200, 0x30,0);//LAYER_FLAGS
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(125, 125, 175, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawCircle(177, 177, 177, mPaint);


        mPaint.setColor(Color.RED);
        canvas.drawCircle(188, 188, 188, mPaint);


        canvas.restore();




    }









}
