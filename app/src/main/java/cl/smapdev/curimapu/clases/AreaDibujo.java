package cl.smapdev.curimapu.clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class AreaDibujo extends View {

    private Path path;
    private final List<Path> paths;
    private final List<Paint> paints;
    private FileOutputStream fopt = null;

    public AreaDibujo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paths = new ArrayList<>();
        paints = new ArrayList<>();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(255, 240, 240, 240);
        int i = 0;
        for (Path trazo : this.paths) {
            canvas.drawPath(trazo, this.paints.get(i++));
        }
    }

    private void saveCanvas() {

        Bitmap toDisk = Bitmap.createBitmap(
                900,
                400,
                Bitmap.Config.ARGB_8888
        );
        Canvas canvasSave = new Canvas(toDisk);
        canvasSave.drawARGB(255, 255, 255, 255);
        int i = 0;
        for (Path trazo : this.paths) {
            canvasSave.drawPath(trazo, this.paints.get(i++));
        }
        toDisk.compress(Bitmap.CompressFormat.WEBP, 90, fopt);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float posx = event.getX();
        float posy = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Paint paint = new Paint();
                paint.setStrokeWidth(5);
                paint.setARGB(255, 0, 0, 0);
                paint.setStyle(Paint.Style.STROKE);

                paints.add(paint);

                path = new Path();
                path.moveTo(posx, posy);
                paths.add(path);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:

                int puntosHistoricos = event.getHistorySize();
                for (int i = 0; i < puntosHistoricos; i++) {
                    path.lineTo(event.getHistoricalX(i), event.getHistoricalY(i));
                }
                break;
        }

        invalidate();
        return true;
    }

    public void reset() {
        paths.clear();
        paints.clear();
        invalidate();
    }


    public String saveAsImage(String name) {

        if (paths.size() <= 0 || paints.size() <= 0) {
            return "";
        }
        File miFile = new File(getContext().getExternalFilesDir("DCIM"), Utilidades.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada) {
            isCreada = miFile.mkdirs();
        }

        String signPath = getContext().getExternalFilesDir("DCIM")
                + File.separator + Utilidades.DIRECTORIO_IMAGEN
                + File.separator + name;
        File file = new File(signPath);
        try {
            fopt = new FileOutputStream(file);
            saveCanvas();
            return signPath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
