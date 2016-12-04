package anh2772.slenderman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jason on 11/6/2016.
 */
public class Fog extends View {

    private Paint paint;
    private Integer width;
    private Integer height;
    private Canvas canvas;
    private Integer orientation;
    private Integer[] dimensions;
    private Point[] triangleDimension;
    private Integer alpha;
    private Paint flashlightPaint;
    private Handler fHandler;
    private Boolean started;

    private Runnable fR = new Runnable() {
        @Override public void run() {
            updateFlashLight(1);
            if(fHandler != null) {
                fHandler.postDelayed(this, 2000);
            }
        }
    };

    public Fog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
        this.width = 0;
        this.height = 0;
        this.dimensions = new Integer[8];
        this.paint.setAlpha(225);
        this.triangleDimension = new Point[6];
        this.alpha = 0;
        this.flashlightPaint = new Paint();
        this.flashlightPaint.setColor(Color.BLACK);
        this.flashlightPaint.setStyle(Paint.Style.FILL);
        this.flashlightPaint.setAlpha(this.alpha);
        this.fHandler = new Handler();
        this.fHandler.postDelayed(this.fR, 2000);
    }

    @Override
    public void onDraw(Canvas canvas) {

        System.out.println(dimensions[0] + " " + dimensions[1] + " " + dimensions[2] + " " + dimensions[3]);
        canvas.drawRect(dimensions[0], dimensions[1], dimensions[2], dimensions[3], paint);

        Path triangle1 = new Path();
        triangle1.setFillType(Path.FillType.EVEN_ODD);
        triangle1.moveTo(triangleDimension[0].x, triangleDimension[0].y);
        triangle1.lineTo(triangleDimension[1].x, triangleDimension[1].y);
        triangle1.lineTo(triangleDimension[2].x, triangleDimension[2].y);
        triangle1.lineTo(triangleDimension[0].x, triangleDimension[0].y);
        triangle1.close();

        System.out.println(triangleDimension[3].x + " " + triangleDimension[3].y);
        System.out.println(triangleDimension[4].x + " " + triangleDimension[4].y);
        System.out.println(triangleDimension[5].x + " " + triangleDimension[5].y);

        Path triangle2 = new Path();
        triangle2.setFillType(Path.FillType.EVEN_ODD);
        triangle2.moveTo(triangleDimension[3].x, triangleDimension[3].y);
        triangle2.lineTo(triangleDimension[4].x, triangleDimension[4].y);
        triangle2.lineTo(triangleDimension[5].x, triangleDimension[5].y);
        triangle2.lineTo(triangleDimension[3].x, triangleDimension[3].y);
        triangle2.close();

        Path flashlight = new Path();
        flashlight.setFillType(Path.FillType.EVEN_ODD);
        flashlight.moveTo(triangleDimension[0].x, triangleDimension[0].y);
        flashlight.lineTo(triangleDimension[2].x, triangleDimension[2].y);
        flashlight.lineTo(triangleDimension[5].x, triangleDimension[5].y);
        flashlight.lineTo(triangleDimension[3].x, triangleDimension[3].y);
        flashlight.lineTo(triangleDimension[0].x, triangleDimension[0].y);

        canvas.drawPath(triangle1, paint);
        canvas.drawPath(triangle2, paint);
        canvas.drawPath(flashlight, flashlightPaint);

        super.onDraw(canvas);

    }

    private void drawFlashlight(){

    }

    @Override
    public void onSizeChanged(int w, int h, int w0, int h0 ){
        super.onSizeChanged(w, h, w0, h0);
        updateFogPosition(0);
    }

    public void updateFogPosition(Integer orientation){
        this.orientation = orientation;

        if(this.orientation == 0){
            // open left
            updateDimensions((getWidth()/2) + 20,0,getWidth(), getHeight(),
                    new Point(0,0), new Point((getWidth()/2) + 20, 0), new Point((getWidth()/2) + 20, (getHeight()/2) - 100),
                    new Point(0,getHeight()), new Point((getWidth()/2) + 20, getHeight()), new Point((getWidth()/2) + 20, getHeight()/2));
        }else if (this.orientation == 1){
            // open top
            updateDimensions(0,(getHeight()/2),getWidth(), getHeight(),
                    new Point(getWidth(),0), new Point(getWidth(), getHeight()/2), new Point((getWidth()/2) + 20, getHeight()/2),
                    new Point(0,0), new Point(0, getHeight()/2), new Point((getWidth()/2) - 20, getHeight()/2));
        }else if (this.orientation == 2){
            // open right
            updateDimensions(0,0,(getWidth()/2) - 20, getHeight(),
                    new Point(getWidth(),getHeight()), new Point((getWidth()/2) - 20, getHeight()), new Point((getWidth()/2) - 20, getHeight()/2),
                    new Point(getWidth(),0), new Point((getWidth()/2) - 20, 0), new Point((getWidth()/2) - 20, (getHeight()/2) - 100));
        }else if (this.orientation == 3) {
            // open bottom
            updateDimensions(0, 0, getWidth(), (getHeight() / 2) - 100,
                    new Point(0, getHeight()), new Point(0, getHeight() / 2 - 100), new Point((getWidth() / 2) - 20, (getHeight() / 2) - 100),
                    new Point(getWidth(), getHeight()), new Point(getWidth(), (getHeight() / 2) - 100), new Point((getWidth() / 2) + 20, (getHeight() / 2) - 100));
        }
    }

    public void updateFogSize(Integer width, Integer height){
        this.width = width;
        this.height = height;
    }

    public void updateDimensions(Integer left, Integer top, Integer right, Integer bottom,
                                 Point t1_a, Point t1_b, Point t1_c, Point t2_a, Point t2_b, Point t2_c){
        this.dimensions[0] = left;
        this.dimensions[1] = top;
        this.dimensions[2] = right;
        this.dimensions[3] = bottom;

        this.triangleDimension[0] = t1_a;
        this.triangleDimension[1] = t1_b;
        this.triangleDimension[2] = t1_c;
        this.triangleDimension[3] = t2_a;
        this.triangleDimension[4] = t2_b;
        this.triangleDimension[5] = t2_c;

    }

    private void updateFlashLight(Integer increase){
        if(this.alpha <= 225) {
            this.alpha += increase;
            this.flashlightPaint.setAlpha(this.alpha);
        }
    }

}
