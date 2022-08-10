package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class DialogWebViewPDF  extends DialogFragment {


    private String clave_unica;

    public void setClave_unica(String clave_unica) {
        this.clave_unica = clave_unica;
    }

    public static DialogWebViewPDF newInstance(String clave_unica){
        DialogWebViewPDF dg = new DialogWebViewPDF();
        dg.setClave_unica( clave_unica );
        return dg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_webview_pdf, null);
        builder.setView(view);

        WebView webview_pdf = view.findViewById(R.id.webview_pdf);

        final WebSettings webSettings = webview_pdf.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);

//        webView.Settings.BuiltInZoomControls = true;
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        //webview.Settings.SetSupportZoom (true);
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.Settings.GetPluginState();
        webview_pdf.setWebChromeClient(new WebChromeClient());
        String googleDocs = "http://docs.google.com/gview?embedded=true&url=";
        String URLPDF = "http://" + Utilidades.IP_PRODUCCION + "/curimapu/docs/pdf/checklistSiembra.php?clave_unica=";
        webview_pdf.loadUrl(URLPDF +clave_unica);
        //after loading url call the webviewclient
//        webview_pdf.setWebViewClient(new MonkeyWebViewClient(imgViewBack, imgViewForward, imgRefresh));



        return builder.create();
    }
}
