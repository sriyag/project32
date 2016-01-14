package com.example.sriyag.drawingapp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sriyag on 10/01/16.
 */
public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private float brushSize, lastBrushSize;

    private boolean erase = false;

    public DrawingView (Context context, AttributeSet attrs) {
        super (context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);

        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);

//        instantiate the drawing canvas and bitmap using the width and height values
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    //        To allow the class to function as a custom drawing View, we also need to override the onDraw method

    @Override
    protected void onDraw(Canvas canvas) {
        //draw view - draw the canvas and the drawing path
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

//      When the drawing View is on the app screen, we want user touches on it to register as drawing operations.
//      To do this we need to listen for touch events.

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch and retrieve coordinates of user touch
        float touchX = event.getX();
        float touchY = event.getY();

//        The MotionEvent parameter to the onTouchEvent method will let us respond to particular touch events.
//        The actions we are interested in to implement drawing are down, move and up

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

//        Calling invalidate will cause the onDraw method to execute.

        invalidate();
        return true;
    }

    public void setColor(String newColor){
        //set color
        invalidate();

        //now parse and set the colour for drawing
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);

    }

    public void setBrushSize(float newSize){
        //update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

/*    We will be passing the value from the dimensions file when we call this method,
    so we have to calculate its dimension value.
    We update the variable and the Paint object to use the new size.
    Now add methods to get and set the other size variable we created.*/

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    //called from MainActivity


    public void setErase(boolean isErase){
        //set erase true or false
        erase = isErase;

//        Now alter the Paint object to erase or switch back to drawing:
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);

    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}
