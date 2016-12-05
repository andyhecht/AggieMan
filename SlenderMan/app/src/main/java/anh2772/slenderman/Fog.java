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
 *
 * Creates a fog around the user's view.
 * Practically makes it seem like user has a flashlight
 */
public class Fog extends View {

    // declare variables for canvas drawing
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

    // makes the flashlight "run out of batteries" eventually
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

        // initialize variables
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

        // draws the fog to hide everything behind the user
        System.out.println(dimensions[0] + " " + dimensions[1] + " " + dimensions[2] + " " + dimensions[3]);
        canvas.drawRect(dimensions[0], dimensions[1], dimensions[2], dimensions[3], paint);

        // draws a triangle to hide one side of the user
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

        // draws a triangle to hide the other side of the user
        Path triangle2 = new Path();
        triangle2.setFillType(Path.FillType.EVEN_ODD);
        triangle2.moveTo(triangleDimension[3].x, triangleDimension[3].y);
        triangle2.lineTo(triangleDimension[4].x, triangleDimension[4].y);
        triangle2.lineTo(triangleDimension[5].x, triangleDimension[5].y);
        triangle2.lineTo(triangleDimension[3].x, triangleDimension[3].y);
        triangle2.close();

        // draws transparent shape that is the flashlight
        Path flashlight = new Path();
        flashlight.setFillType(Path.FillType.EVEN_ODD);
        flashlight.moveTo(triangleDimension[0].x, triangleDimension[0].y);
        flashlight.lineTo(triangleDimension[2].x, triangleDimension[2].y);
        flashlight.lineTo(triangleDimension[5].x, triangleDimension[5].y);
        flashlight.lineTo(triangleDimension[3].x, triangleDimension[3].y);
        flashlight.lineTo(triangleDimension[0].x, triangleDimension[0].y);

        // draw the fogged sides and the flashlight
        canvas.drawPath(triangle1, paint);
        canvas.drawPath(triangle2, paint);
        canvas.drawPath(flashlight, flashlightPaint);

        super.onDraw(canvas);

    }

    @Override
    public void onSizeChanged(int w, int h, int w0, int h0 ){
        super.onSizeChanged(w, h, w0, h0);

        // update the dimensions of the flashlight
        updateFogPosition(0);
    }

    // update the dimensions of the flashlight to reflect the user looking at a new direciton
    public void updateFogPosition(Integer orientation){

        // direction the user is facing
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

    // update the width and height variables
    public void updateFogSize(Integer width, Integer height){
        this.width = width;
        this.height = height;
    }

    // update the dimension variables to values
    public void updateDimensions(Integer left, Integer top, Integer right, Integer bottom,
                                 Point t1_a, Point t1_b, Point t1_c, Point t2_a, Point t2_b, Point t2_c){

        // dimensions for fog behind
        this.dimensions[0] = left;
        this.dimensions[1] = top;
        this.dimensions[2] = right;
        this.dimensions[3] = bottom;

        // dimensions for fog on sides
        this.triangleDimension[0] = t1_a;
        this.triangleDimension[1] = t1_b;
        this.triangleDimension[2] = t1_c;
        this.triangleDimension[3] = t2_a;
        this.triangleDimension[4] = t2_b;
        this.triangleDimension[5] = t2_c;

    }

    // update the transparency of the flashlight
    private void updateFlashLight(Integer increase){
        if(this.alpha <= 225) {
            this.alpha += increase;
            this.flashlightPaint.setAlpha(this.alpha);
        }
    }

}
