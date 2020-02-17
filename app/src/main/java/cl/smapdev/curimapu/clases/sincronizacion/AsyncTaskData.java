package cl.smapdev.curimapu.clases.sincronizacion;

import android.os.AsyncTask;
import android.view.View;

import cl.smapdev.curimapu.MainActivity;

public class AsyncTaskData extends AsyncTask<Void, Integer, String> {

    private View view;
    private MainActivity context;

    public AsyncTaskData(View view, MainActivity context) {
        this.view = view;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {




        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
