package cl.smapdev.curimapu.clases.utilidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import cl.smapdev.curimapu.R;


public class InternetStateClass extends AsyncTask<Void, Boolean , Boolean> {

    private ProgressDialog progressDialog;
    private final WeakReference<Context> activity;
    private Boolean tipoConexion1 = false, tipoConexion2 = true;

    private final returnValuesFromAsyntask mListener;
    private final int lugar;

    public InternetStateClass(Context reference, returnValuesFromAsyntask mListener, int lugar) {
        this.activity = new WeakReference<>(reference);
        this.mListener = mListener;
        this.lugar = lugar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (lugar == 1) {
            progressDialog = new ProgressDialog(activity.get());
            progressDialog.setTitle("Espere un momento...");
            progressDialog.setMessage("comprobando conexion a internet ");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        ConnectivityManager cm = (ConnectivityManager) activity.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                ConnectivityManager connManager1 = (ConnectivityManager) activity.get().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mwifi = null;
                if (connManager1 != null) {
                    mwifi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                }

                ConnectivityManager connManager2 = (ConnectivityManager) activity.get().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobile = null;
                if (connManager2 != null) {
                    mMobile = connManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                }

                if (mwifi != null) {
                    if (mwifi.isConnected()) {
                        tipoConexion1 = true;
                    }
                }

                if (mMobile != null) {
                    if (mMobile.isConnected()) {
                        tipoConexion2 = true;
                    }
                }
            }
        }
        return tipoConexion1 || tipoConexion2;
    }

    @Override
    protected void onPostExecute(Boolean integer) {
        super.onPostExecute(integer);
        if (lugar == 1) {
            progressDialog.dismiss();
        }

        if (mListener != null) {
            mListener.myMethod(integer);
        }

    }
}
