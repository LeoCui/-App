package com.leo.playview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.leo.musicinfo.SingleLrc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 16/3/14.
 */
public class lrcView  extends android.widget.TextView{
        private float width;		//歌词视图宽度
        private float height;		//歌词视图高度
        private Paint currentPaint;	//当前画笔对象
        private Paint noLrcPaint;
        private Paint notCurrentPaint;	//非当前画笔对象
        private float textHeight = 100;	//文本高度
        private float textSize = 50;		//文本大小
        private int index = 0;		//list集合下标


        private static  List<SingleLrc> mLrcList = new ArrayList<SingleLrc>();

        public void setmLrcList(List<SingleLrc> mLrcList) {
            this.mLrcList = mLrcList;
        }

        public lrcView(Context context) {
            super(context);
            init();
        }
        public lrcView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public lrcView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setFocusable(true);		//设置可对焦
            //无歌词部分
            noLrcPaint=new Paint();
            noLrcPaint.setAntiAlias(true);
            noLrcPaint.setTextAlign(Paint.Align.CENTER);
            //高亮部分
            currentPaint = new Paint();
            currentPaint.setAntiAlias(true);	//设置抗锯齿，让文字美观饱满
            currentPaint.setTextAlign(Paint.Align.CENTER);//设置文本对齐方式

            //非高亮部分
            notCurrentPaint = new Paint();
            notCurrentPaint.setAntiAlias(true);
            notCurrentPaint.setTextAlign(Paint.Align.CENTER);
        }

        /**
         * 绘画歌词
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(canvas == null) {
                return;
            }

            noLrcPaint.setColor(0xffffffff);
            currentPaint.setColor(Color.argb(210, 251, 248, 29));
            notCurrentPaint.setColor(Color.argb(140, 255, 255, 255));

            noLrcPaint.setTextSize(80);
            noLrcPaint.setTypeface(Typeface.SERIF);

            currentPaint.setTextSize(60);
            currentPaint.setTypeface(Typeface.SERIF);

            notCurrentPaint.setTextSize(textSize);
            notCurrentPaint.setTypeface(Typeface.DEFAULT);

            try {
                setText("");
                canvas.drawText(mLrcList.get(index).getLrc(), width / 2, height / 2, currentPaint);

                float tempY = height / 2;
                //画出本句之前的句子
                for(int i = index - 1; i >= 0; i--) {
                    //向上推移
                    tempY = tempY - textHeight;
                    if(tempY<textHeight){
                        break;
                    }
                    canvas.drawText(mLrcList.get(i).getLrc(), width / 2, tempY, notCurrentPaint);
                }
                tempY = height / 2;
                //画出本句之后的句子
                for(int i = index + 1; i < mLrcList.size(); i++) {
                    //往下推移
                    tempY = tempY + textHeight;
                    canvas.drawText(mLrcList.get(i).getLrc(), width / 2, tempY, notCurrentPaint);
                }
            } catch (Exception e) {
                if(index==-1){
                    canvas.drawText("暂无歌词",width/2,height/2,noLrcPaint);
                }

            }
        }

        /**
         * 当view大小改变的时候调用的方法
         */
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.width = w;
            this.height = h;
        }

        public void setIndex(int index) {
            this.index = index;
        }
}
