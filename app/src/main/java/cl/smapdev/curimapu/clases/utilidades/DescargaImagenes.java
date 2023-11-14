package cl.smapdev.curimapu.clases.utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class DescargaImagenes {

    private Context context;
    private String folderName;


    private int position = 1;
    private int total = 1;

    public DescargaImagenesI callback;
    private RequestQueue requestQueue;

    private static final String TAG = "ImageDownloader";

    public DescargaImagenes(Context context, String folderName, DescargaImagenesI callback) {
        this.context = context;
        this.folderName = folderName;
        this.callback = callback;
    }

    public void descargarEGuardarImagenes(List<String> urls) {
        this.total = urls.size();
        for (String url : urls) {
            descargarYGuardarImagen(url);
        }
    }

    private void descargarYGuardarImagen(final String url) {
       try {

           if(requestQueue == null){
               requestQueue = Volley.newRequestQueue(context);
           }
           ImageRequest request = new ImageRequest(url,
                   bitmap -> {
                       guardarImagenInterna(bitmap, obtenerNombreArchivo(url));
                   },
                   0,
                   0,
                   null,
                   error -> {
               callback.erroredImage("URL :" + url + " No se pudo descargar");
                       Log.e("ImageDownloader", "Error al descargar la imagen: " + error.getMessage());
                   });

           requestQueue.add(request);
       }catch (OutOfMemoryError error){
           Log.e(TAG, "SIN MEMORIA "+error.getMessage());
       }

    }

    private void guardarImagenInterna(Bitmap bitmap, String nombreArchivo) {
        File directorioInterno = context.getFilesDir();
        File carpetaImagenes = new File(directorioInterno, folderName);

        if (!carpetaImagenes.exists()) {
            carpetaImagenes.mkdirs();
        }
        File archivoImagen = new File(carpetaImagenes, nombreArchivo);
        try (FileOutputStream out = new FileOutputStream(archivoImagen)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            position++;
            callback.imageCompleted((position * 100) /  this.total);
        }
    }

    private String obtenerNombreArchivo(String url) {
        // Puedes ajustar la lógica para obtener un nombre de archivo único
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
