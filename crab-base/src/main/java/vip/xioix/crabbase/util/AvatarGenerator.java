package vip.xioix.crabbase.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by terge on 16-11-25.
 */

public class AvatarGenerator {
    private Bitmap[] mSource;

    //line/column
    private int lc;
    // width/height
    private int wh;

    public AvatarGenerator(Bitmap[] source){
        mSource = source;
        lc = (int) Math.sqrt(source.length);
    }

    private int mMaxWidth = 100;
    public AvatarGenerator setMaxWidth(int width){
        mMaxWidth = width;
        return this;
    }

    private int mGap = 10;
    public AvatarGenerator setGap(int gap){
        mGap = gap;
        return this;
    }

    public  Bitmap generate(){
        wh = (mMaxWidth - ((lc -1)*mGap))/lc;
        Bitmap bigBm = Bitmap.createBitmap(mMaxWidth,mMaxWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bigBm);
        canvas.drawColor(Color.WHITE);
        int x = 0,y = 0;
        Paint paint = new Paint();
        paint.setAntiAlias(true);                       //设置画笔为无锯齿
        paint.setColor(Color.BLACK);                    //设置画笔颜色
                      //白色背景
        paint.setStrokeWidth((float) 3.0);              //线宽
        paint.setStyle(Paint.Style.STROKE);


        int colum ;
        for (int i = 0;i<mSource.length -1;i++) {
            colum = i%lc;
            //最边上一列，需要重新计算坐标
            if(colum == 0){
                y = colum * (wh+mGap);
                if(i == 0) {
                    //缺失的icon个数* 宽度 = 缺失的总宽度
                    int lost = (lc * lc - mSource.length)*(wh+mGap);
                    //那么左边距就是缺失总宽度/2
                    lost = lost /2;
                    x = lost;
                }else {
                    //左边距是上一个起始位置+宽度+间距
                    x = x + wh+mGap;
                }
            }

            canvas.drawBitmap(mSource[i],x,y,paint);
        }
        return bigBm;
    }


}
