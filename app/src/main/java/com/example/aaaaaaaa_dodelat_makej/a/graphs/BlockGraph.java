package com.example.aaaaaaaa_dodelat_makej.a.graphs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.aaaaaaaa_dodelat_makej.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockGraph extends Graph {

    Paint fill;
    Rect bounds;

    float minBlockSize = 85;
    //TODO: Spacing
    float space = 0;
    float positionY = 0;
    float startY = 0;
    boolean scroll = false;

    ArrayList<DataModel> data;

    public BlockGraph(Context context) {
        super(context);
//        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,600));
        init();
    }

    public BlockGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setColor(getResources().getColor(R.color.gray_variant,getContext().getTheme()));
        fill.setStrokeWidth(2);
        bounds = new Rect();


        data = new ArrayList<>();
        data.add(new DataModel("Ja", 20.3, Color.CYAN + ""));
        data.add(new DataModel("Ty", 100, Color.GREEN + ""));
        data.add(new DataModel("On", 70, Color.BLUE + ""));
        data.add(new DataModel("Orel", 50, Color.MAGENTA + ""));
        data.add(new DataModel("host",20,Color.RED+""));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0,0,getWidth(),getHeight(), fill);

        int max = (int) max();

        int h = (getHeight()/(data.size()+2));
        int y = h;

//        if((y-space) < minBlockSize){
//            h = (int) minBlockSize;
//            y = h;
//            scroll = true;
//        }else{
//            scroll = false;
//        }

        background(canvas, max, true, false);

        float old = text.getTextSize();
        int oldFill = fill.getColor();
//        text.setTextSize(40);
        //BLOCKS
        canvas.save();
        canvas.translate(0, positionY);
        for(DataModel model:data){


            fill.setColor(Integer.parseInt(model.color));
            fill.setStyle(Paint.Style.STROKE);
            fill.setShadowLayer(4,0,0,Integer.parseInt(model.color));
            canvas.drawRect(0,y+h/2-30, (float) (model.value*(getWidth()*0.8/max)), y+h/2+30,fill);
            fill.setStyle(Paint.Style.FILL);
            fill.setAlpha(55);
            canvas.drawRect(0,y+h/2-30, (float) (model.value*(getWidth()*0.8/max)), y+h/2+30,fill);
            fill.setAlpha(255);

            text.getTextBounds(model.name,0,model.name.length(),bounds);
            text.setTextSize(30);
            canvas.drawText(model.name, 10, y+h/2+bounds.height()/2, text);
            float tmp = ((model.value*(getWidth()*0.8/max))+20 <= bounds.width()+40)?bounds.width()+40: (float) ((model.value * (getWidth() * 0.8 / max)) + 20);
            text.setTextSize(25);
            canvas.drawText(Math.round(model.value)+"", tmp,y+h/2+bounds.height()/2, text);

            y+=h+space;
        }
        canvas.restore();
        text.setTextSize(old);
        fill.setColor(oldFill);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(scroll) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                startY = event.getY();
//            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                positionY -= startY - event.getY();
//                startY = event.getY();
//                if (positionY > 0)
//                    positionY = 0;
//                if (positionY < -((minBlockSize+space) * (data.size() + 2) - (getHeight()+space*3)))
//                    positionY = -((minBlockSize+space) * (data.size() + 2) - (getHeight()+space*3));
//                invalidate();
//            }
//        }
//        return true;
//    }

    private double max(){
        double max = 0;
        for (DataModel d:data){
            if(max<d.value){
                max = d.value;
            }
        }
        return max;
    }

    public void add(ArrayList<DataModel> data){
        this.data.clear();
        this.data.addAll(data);
        invalidate();
    }

    public static class DataModel {

        public String name;
        public double value;
        public String color;

        public DataModel(){}

        public DataModel(String name, double value, String color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }
    }

}

