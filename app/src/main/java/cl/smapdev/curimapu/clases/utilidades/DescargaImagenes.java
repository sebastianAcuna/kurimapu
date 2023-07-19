package cl.smapdev.curimapu.clases.utilidades;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DescargaImagenes {

    private static DescargaImagenes instance;
    private RequestQueue requestQueue;
    private static Context context;

    private static final String TAG = "ImageDownloader";

    private DescargaImagenes(Context context){
        DescargaImagenes.context = context;
        requestQueue = getRequestQueue();
    }



    public static synchronized  DescargaImagenes getInstance(Context context){
        if(instance == null){
            instance = new DescargaImagenes(context);
        }

        return instance;
    }

    public void cancelQueue(){
        if(requestQueue != null) {
            requestQueue.cancelAll(context.getApplicationContext());
        }
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }


    public void saveImageToDisk(Bitmap bitmap, String fileName) {
        File directory = new File(Environment.getExternalStorageDirectory(), "/DCIM");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            Log.i(TAG, "Image saved to disk: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Error saving image to disk: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing output stream: " + e.getMessage());
            }
        }
    }


}