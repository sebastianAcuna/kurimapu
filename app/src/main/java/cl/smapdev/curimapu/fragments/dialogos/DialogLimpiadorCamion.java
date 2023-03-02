package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogLimpiadorCamion extends DialogFragment {


    private EditText et_nombre_chofer;
    private EditText et_patente_camion;
    private EditText et_patente_carro;
    private EditText et_estado_general;
    private EditText et_equipos_utilizados_limpieza;
    private RadioButton btn_puertas_laterales_si;
    private RadioButton btn_puertas_laterales_no;
    private RadioButton btn_puertas_traseras_si;
    private RadioButton btn_puertas_traseras_no;
    private RadioButton btn_limpieza_piso_si;
    private RadioButton btn_limpieza_piso_no;
    private RadioButton btn_inspeccion_rejillas_mallas_realizada_si;
    private RadioButton btn_inspeccion_rejillas_mallas_realizada_no;
    private RadioButton btn_pisos_costados_bateas_sin_orificio_si;
    private RadioButton btn_pisos_costados_bateas_sin_orificio_no;
    private RadioButton btn_camion_carro_limpio_revisado_si;
    private RadioButton btn_camion_carro_limpio_revisado_no;
    private RadioButton btn_carpa_limpia_revisada_si;
    private RadioButton btn_carpa_limpia_revisada_no;
    private RadioButton btn_sistema_cerrado_puertas_si;
    private RadioButton btn_sistema_cerrado_puertas_no;
    private EditText et_nivel_llenado_carga;

    private EditText et_carga_anterior;
    private RadioButton btn_sello_color_indica_condicion_si;
    private RadioButton btn_sello_color_indica_condicion_no;
    private RadioButton btn_etiqueta_cosecha_adherida_camion_si;
    private RadioButton btn_etiqueta_cosecha_adherida_camion_no;
    private EditText et_sello_verde_curimapu_cierre_camion;
    private Button btn_firma;
    private ImageView iv_firma;
    private Button btn_guardar_anexo_fecha;
    private Button btn_posponer_anexo_fecha;

    private int tipoAsistene;
    private IOnSave IOnSave;

    public interface IOnSave {
        void onSave( boolean saved );
    }


    public void setTipoAsistene(int tipoAsistene) {
        this.tipoAsistene = tipoAsistene;
    }

    public static DialogLimpiadorCamion newInstance(IOnSave onSave, int tipoAsistene){
        DialogLimpiadorCamion dg = new DialogLimpiadorCamion();
        dg.setIOnSave( onSave );
        dg.setTipoAsistene(tipoAsistene);
        return dg;
    }

    public void setIOnSave(IOnSave onSave){ this.IOnSave = onSave; }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_agregar_limpiador_camiones, null);
        builder.setView(view);

        bind(view);

        builder.setTitle("Nuevo Detalle");


        btn_guardar_anexo_fecha.setOnClickListener(view1 -> onSave());
        btn_posponer_anexo_fecha.setOnClickListener(view1 -> cerrar());

        btn_firma.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_LIMPIEZA_CAMIONES);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRa = UUID.randomUUID().toString()+".png";


            DialogFirma dialogo = DialogFirma.newInstance(
                    tipoAsistene,
                    etRa,
                    Utilidades.DIALOG_TAG_FIRMA_LIMPIEZA_CAMIONES,
                    (isSaved, path) -> {
                        if(isSaved){
                            iv_firma.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_LIMPIEZA_CAMIONES);
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if( dialog != null ){
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);

        }
    }

    public void cerrar(){
        Dialog dialog = getDialog();
        if( dialog != null ){
            dialog.dismiss();
        }
    }


    public void onSave(){


        if(
                et_nombre_chofer.getText().toString().isEmpty() ||
                et_patente_camion.getText().toString().isEmpty() ||
                et_patente_carro.getText().toString().isEmpty() ||
                et_estado_general.getText().toString().isEmpty() ||
                et_equipos_utilizados_limpieza.getText().toString().isEmpty() ||
                iv_firma.getVisibility() == View.GONE
        ){
            Toasty.error(requireContext(),
                    "Debes completar campos con (*) y la firma." ,
                    Toast.LENGTH_LONG, true).show();
            return;
        }


        String nombreChofer = et_nombre_chofer.getText().toString();
        String patenteCamion = et_patente_camion.getText().toString();
        String patenteCarro = et_patente_carro.getText().toString();
        String estadoGenerar = et_estado_general.getText().toString();
        String equipoUtilizadosLimpieza = et_equipos_utilizados_limpieza.getText().toString();
        String nivelLlenadoCarga = et_nivel_llenado_carga.getText().toString();
        String cargaAnterior = et_carga_anterior.getText().toString();
        String selloVerdeCurimapuCierreCamion =  et_sello_verde_curimapu_cierre_camion.getText().toString();

        int puertaLateral = 0;
        if(btn_puertas_laterales_si.isChecked() || btn_puertas_laterales_no.isChecked()){
            puertaLateral = (btn_puertas_laterales_si.isChecked()) ? 1 : 2;
        }

        int puertaTrasera = 0;
        if(btn_puertas_traseras_si.isChecked() || btn_puertas_traseras_no.isChecked()){
            puertaTrasera = (btn_puertas_traseras_si.isChecked()) ? 1 : 2;
        }

        int limpiezaPiso = 0;
        if(btn_limpieza_piso_si.isChecked() || btn_limpieza_piso_no.isChecked()){
            limpiezaPiso = (btn_limpieza_piso_si.isChecked()) ? 1 : 2;
        }

        int inspeccionRejillaMallaRealizada = 0;
        if(btn_inspeccion_rejillas_mallas_realizada_si.isChecked() || btn_inspeccion_rejillas_mallas_realizada_no.isChecked()){
            inspeccionRejillaMallaRealizada = (btn_inspeccion_rejillas_mallas_realizada_si.isChecked()) ? 1 : 2;
        }

        int pisoCostadoBateasSinOrificio = 0;
        if(btn_pisos_costados_bateas_sin_orificio_si.isChecked() || btn_pisos_costados_bateas_sin_orificio_no.isChecked()){
            pisoCostadoBateasSinOrificio = (btn_pisos_costados_bateas_sin_orificio_si.isChecked()) ? 1 : 2;
        }

        int camionCarroLimpioRevisado = 0;
        if(btn_camion_carro_limpio_revisado_si.isChecked() || btn_camion_carro_limpio_revisado_no.isChecked()){
            camionCarroLimpioRevisado = (btn_camion_carro_limpio_revisado_si.isChecked()) ? 1 : 2;
        }

        int carpaLimpiaRevisada = 0;
        if(btn_carpa_limpia_revisada_si.isChecked() || btn_carpa_limpia_revisada_no.isChecked()){
            carpaLimpiaRevisada = (btn_carpa_limpia_revisada_si.isChecked()) ? 1 : 2;
        }

        int sistemaCerradoPuertas = 0;
        if(btn_sistema_cerrado_puertas_si.isChecked() || btn_sistema_cerrado_puertas_no.isChecked()){
            sistemaCerradoPuertas = (btn_sistema_cerrado_puertas_si.isChecked()) ? 1 : 2;
        }

        int selloColorIndicaCondicion = 0;
        if(btn_sello_color_indica_condicion_si.isChecked() || btn_sello_color_indica_condicion_no.isChecked()){
            selloColorIndicaCondicion = (btn_sello_color_indica_condicion_si.isChecked()) ? 1 : 2;
        }

        int etiquetaCosechaAdheridaCamion = 0;
        if(btn_etiqueta_cosecha_adherida_camion_si.isChecked() || btn_etiqueta_cosecha_adherida_camion_no.isChecked()){
            etiquetaCosechaAdheridaCamion = (btn_etiqueta_cosecha_adherida_camion_si.isChecked()) ? 1 : 2;
        }


        ChecklistLimpiezaCamionesDetalle detalle = new ChecklistLimpiezaCamionesDetalle();


        detalle.setNombre_chofer_limpieza_camiones(nombreChofer);
        detalle.setPatente_camion_limpieza_camiones(patenteCamion);
        detalle.setPatente_carro_limpieza_camiones(patenteCarro);
        detalle.setEstado_general_recepcion_camion_campo_limpieza_camiones(estadoGenerar);
        detalle.setEquipo_utilizado_limpieza_camiones(equipoUtilizadosLimpieza);
        detalle.setNivel_llenado_carga_limpieza_camiones(nivelLlenadoCarga);
        detalle.setLimpieza_anterior_limpieza_camiones(cargaAnterior);
        detalle.setSello_verde_curimapu_cierre_camion_limpieza_camiones(selloVerdeCurimapuCierreCamion);
        detalle.setLimpieza_puertas_laterales_limpieza_camiones(puertaLateral);
        detalle.setLimpieza_puertas_traseras_limpieza_camiones(puertaTrasera);
        detalle.setLimpieza_piso_limpieza_camiones(limpiezaPiso);
        detalle.setInspeccion_rejillas_mallas_limpieza_camiones(inspeccionRejillaMallaRealizada);
        detalle.setPisos_costados_batea_sin_orificios_limpieza_camiones(pisoCostadoBateasSinOrificio);
        detalle.setCamion_carro_limpio_limpieza_camiones(camionCarroLimpioRevisado);
        detalle.setCarpa_limpia_limpieza_camiones(carpaLimpiaRevisada);
        detalle.setSistema_cerrado_puertas_limpieza_camiones(sistemaCerradoPuertas);
        detalle.setSello_color_indica_condicion_limpieza_camiones(selloColorIndicaCondicion);
        detalle.setEtiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones(etiquetaCosechaAdheridaCamion);
        detalle.setClave_unica_cl_limpieza_camiones("0");
        detalle.setEstado_sincronizacion_detalle(0);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            List<TempFirmas> firmasFuture = executor.submit(() -> MainActivity.myAppDB.DaoFirmas()
                    .getFirmasByDocum(tipoAsistene)).get();

            for (TempFirmas ff : firmasFuture){
                detalle.setFirma_cl_limpieza_camiones_detalle(ff.getPath());
            }

            executor.submit(() -> MainActivity.myAppDB.DaoFirmas()
                    .deleteFirmasByDoc(tipoAsistene));

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        detalle.setClave_unica_cl_limpieza_camiones_detalle(UUID.randomUUID().toString());

        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        try {
            executor2.submit(() -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                    .insertLimpiezaCamionesDetalle(detalle)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Dialog dialog = getDialog();
        IOnSave.onSave( true);
        if( dialog != null ){ dialog.dismiss();}
    }


    public void bind(View view){


        et_nombre_chofer = view.findViewById(R.id.et_nombre_chofer);
        et_patente_camion = view.findViewById(R.id.et_patente_camion);
        et_patente_carro = view.findViewById(R.id.et_patente_carro);
        et_estado_general = view.findViewById(R.id.et_estado_general);
        et_equipos_utilizados_limpieza = view.findViewById(R.id.et_equipos_utilizados_limpieza);
        btn_puertas_laterales_si = view.findViewById(R.id.btn_puertas_laterales_si);
        btn_puertas_laterales_no = view.findViewById(R.id.btn_puertas_laterales_no);
        btn_puertas_traseras_si = view.findViewById(R.id.btn_puertas_traseras_si);
        btn_puertas_traseras_no = view.findViewById(R.id.btn_puertas_traseras_no);
        btn_limpieza_piso_si = view.findViewById(R.id.btn_limpieza_piso_si);
        btn_limpieza_piso_no = view.findViewById(R.id.btn_limpieza_piso_no);
        btn_inspeccion_rejillas_mallas_realizada_si = view.findViewById(R.id.btn_inspeccion_rejillas_mallas_realizada_si);
        btn_inspeccion_rejillas_mallas_realizada_no = view.findViewById(R.id.btn_inspeccion_rejillas_mallas_realizada_no);
        btn_pisos_costados_bateas_sin_orificio_si = view.findViewById(R.id.btn_pisos_costados_bateas_sin_orificio_si);
        btn_pisos_costados_bateas_sin_orificio_no = view.findViewById(R.id.btn_pisos_costados_bateas_sin_orificio_no);
        btn_camion_carro_limpio_revisado_si = view.findViewById(R.id.btn_camion_carro_limpio_revisado_si);
        btn_camion_carro_limpio_revisado_no = view.findViewById(R.id.btn_camion_carro_limpio_revisado_no);
        btn_carpa_limpia_revisada_si = view.findViewById(R.id.btn_carpa_limpia_revisada_si);
        btn_carpa_limpia_revisada_no = view.findViewById(R.id.btn_carpa_limpia_revisada_no);
        btn_sistema_cerrado_puertas_si = view.findViewById(R.id.btn_sistema_cerrado_puertas_si);
        btn_sistema_cerrado_puertas_no = view.findViewById(R.id.btn_sistema_cerrado_puertas_no);
        et_nivel_llenado_carga = view.findViewById(R.id.et_nivel_llenado_carga);
        et_carga_anterior = view.findViewById(R.id.et_carga_anterior);
        btn_sello_color_indica_condicion_si = view.findViewById(R.id.btn_sello_color_indica_condicion_si);
        btn_sello_color_indica_condicion_no = view.findViewById(R.id.btn_sello_color_indica_condicion_no);
        btn_etiqueta_cosecha_adherida_camion_si = view.findViewById(R.id.btn_etiqueta_cosecha_adherida_camion_si);
        btn_etiqueta_cosecha_adherida_camion_no = view.findViewById(R.id.btn_etiqueta_cosecha_adherida_camion_no);
        et_sello_verde_curimapu_cierre_camion = view.findViewById(R.id.et_sello_verde_curimapu_cierre_camion);
        btn_firma = view.findViewById(R.id.btn_firma);
        iv_firma = view.findViewById(R.id.iv_firma);
        btn_guardar_anexo_fecha = view.findViewById(R.id.btn_guardar_anexo_fecha);
        btn_posponer_anexo_fecha = view.findViewById(R.id.btn_posponer_anexo_fecha);

    }

}
