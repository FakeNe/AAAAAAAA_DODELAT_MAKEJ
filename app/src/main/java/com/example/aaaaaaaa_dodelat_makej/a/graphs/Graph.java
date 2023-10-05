package com.example.aaaaaaaa_dodelat_makej.a.graphs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.aaaaaaaa_dodelat_makej.R;


public class Graph extends View {

    Paint text;
    Paint line;

    int n = 6;

    public Graph(Context context) {
        super(context);
        init();
    }

    public Graph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        text = new Paint(Paint.ANTI_ALIAS_FLAG);
        line = new Paint(Paint.ANTI_ALIAS_FLAG);
        text.setColor(getResources().getColor(R.color.white,getContext().getTheme()));
        text.setTextSize(20);
        line.setColor(getResources().getColor(R.color.gray, getContext().getTheme()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        background(canvas, 100, true, true);
    }

    public void background(Canvas canvas, double max, boolean top, boolean left){
        for(int i = 1; i < getWidth(); i++){
            int x = (int) (i * (getWidth()*0.8/n));
            canvas.drawRect(x,0,x+1,getHeight(),line);
            if (top)
                canvas.drawText((int)((max/n)*i) +"", x+30, 30, text);
        }
        for(int i = 1; i < getHeight(); i++){
            int x = (int) (i * (getWidth()*0.8/n));
            canvas.drawRect(0,x,getWidth(),x+1,line);
            if(left)
                canvas.drawText((int)((max/n)*i)+"", 10, x+getWidth()/(n*2), text);
        }
    }
}
