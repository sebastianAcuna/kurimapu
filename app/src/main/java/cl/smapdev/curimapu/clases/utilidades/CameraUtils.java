package cl.smapdev.curimapu.clases.utilidades;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import cl.smapdev.curimapu.R;

public class CameraUtils {


    public static Bitmap escribirFechaImg(Bitmap bm, Activity activity){

        Bitmap dest = Bitmap.createBitmap(bm,0, 0,bm.getWidth(), bm.getHeight()).copy(Bitmap.Config.ARGB_8888, true);

        String fecha = Utilidades.fechaActualInvSinHora();

        Canvas cs = new Canvas(dest);

//        Paint myPaint = new Paint();
//        myPaint.setColor(activity.getResources().getColor(R.color.transparentBlack));
//        myPaint.setStrokeWidth(10);
//        cs.drawRect(5, dest.getHeight() - 90, 700, dest.getHeight() - 10, myPaint);

        Paint tPaint = new Paint();
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setColor(activity.getResources().getColor(R.color.colorGold));

        tPaint.setTextSize(calculateTextSize(bm.getWidth()));
        tPaint.setStrokeWidth(10);

        // text shadow
        tPaint.setShadowLayer(1f, 0f, 1f, activity.getResources().getColor(android.R.color.black));

        Rect bounds = new Rect();
        tPaint.getTextBounds(fecha, 0, fecha.length(), bounds);

        cs.drawText(fecha,7, dest.getHeight() - 25 , tPaint);



        return dest;
    }

    private static int calculateTextSize(int width){


        if(width <= 600){
            return 15;
        }

        if(width <= 1400){
            return 30;
        }

        return 50;
    }
}
