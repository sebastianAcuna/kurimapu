package cl.smapdev.curimapu.clases.utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;


public class InternetStateClass {

    private final ExecutorService executor;
    private final Handler handler;

    // 2. Referencia débil al contexto para evitar fugas de memoria
    private final WeakReference<Context> contextRef;
    private final returnValuesFromAsyntask mListener;

    // Interfaz para devolver el resultado a la Activity/Fragment
    public interface returnValuesFromAsyntask {
        void onInternetCheckResult(boolean isConnected);
    }

    public InternetStateClass(Context context, returnValuesFromAsyntask listener,
                              ExecutorService executor, Handler handler) {
        this.contextRef = new WeakReference<>(context);
        this.mListener = listener;
        this.executor = executor;
        this.handler = handler;
    }

    /**
     * Inicia la comprobación de la conexión a Internet.
     * El resultado se devolverá a través del listener en el hilo principal.
     */
    public void execute() {
        executor.execute(() -> {
            // --- Esto se ejecuta en el hilo secundario ---
            boolean isConnected = isNetworkAvailable();

            // Publica el resultado de vuelta al hilo principal
            handler.post(() -> {
                // --- Esto se ejecuta en el hilo principal ---
                if (mListener != null) {
                    mListener.onInternetCheckResult(isConnected);
                }
            });
        });
    }

    /**
     * Verifica la disponibilidad de la red utilizando la API moderna NetworkCapabilities.
     *
     * @return true si hay una conexión a Internet activa, false en caso contrario.
     */
    private boolean isNetworkAvailable() {
        Context context = contextRef.get();
        if (context == null) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        // Para Android 6 (API 23) y superior
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        if (capabilities == null) {
            return false;
        }

        // Comprueba si la red tiene capacidad para acceder a Internet
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    /**
     * Es buena práctica ofrecer un método para apagar el executor cuando ya no se necesite,
     * por ejemplo, en el onDestroy de la Activity/Fragment.
     */
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}