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

        Paint myPaint = new Paint();
        myPaint.setColor(activity.getResources().getColor(R.color.transparentBlack));
        myPaint.setStrokeWidth(10);
        cs.drawRect(0, dest.getHeight() - 90, 700, dest.getHeight() - 10, myPaint);

        Paint tPaint = new Paint();
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setColor(activity.getResources().getColor(android.R.color.white));
        tPaint.setTextSize(80);
        tPaint.setStrokeWidth(10);

        // text shadow
        tPaint.setShadowLayer(1f, 0f, 1f, activity.getResources().getColor(android.R.color.black));

        Rect bounds = new Rect();
        tPaint.getTextBounds(fecha, 0, fecha.length(), bounds);

        cs.drawText(fecha, 90, dest.getHeight() - 25 , tPaint);

        return dest;
    }

}
