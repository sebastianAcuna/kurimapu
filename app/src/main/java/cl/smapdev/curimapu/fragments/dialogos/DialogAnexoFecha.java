package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogAnexoFecha  extends DialogFragment  {


    private AnexoWithDates anexos;


    private EditText et_inicio_despano;
    private EditText et_inicio_siembra;
    private EditText et_cinco_porc_floracion;
    private EditText et_inicio_corte_seda;
    private EditText et_inicio_cosecha;
    private EditText et_inicio_cosecha_hora;
    private EditText et_termino_cosecha;
    private EditText et_termino_labores_post_cosecha;
    private EditText et_detalle_labores;

    private Button btn_guardar_anexo_fecha;
    private Button btn_posponer_anexo_fecha;
    private IOnSave IOnSave;
    private String query;


    private EditText et_fecha_siembra_temprada;
    private EditText et_tipo_siembra_temprada;
    private EditText et_fecha_destruccion;
    private EditText et_hora_destruccion;
    private EditText et_cantidad_ha_destruccion;
    private EditText et_motivo_destruccion;


    private RadioButton rbtn_parcial;
    private RadioButton rbtn_completo;

    private ConstraintLayout cont_detalle_destruccion;


    public interface IOnSave {
        void onSave( boolean saved, String query );
    }


    public static DialogAnexoFecha newInstance(  AnexoWithDates anexos,
                                                 IOnSave onSave,
                                                 String query ){
        DialogAnexoFecha dg = new DialogAnexoFecha();
        dg.setAnexos( anexos );
        dg.setQuery( query );
        dg.setIOnSave( onSave );
        return dg;
    }


    public void setQuery(String query) { this.query = query; }
    public void setIOnSave(IOnSave onSave){ this.IOnSave = onSave; }
    public void setAnexos(AnexoWithDates anexos) {
        this.anexos = anexos;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_editar_fechas, null);
        builder.setView(view);

        bind(view);

        AnexoCorreoFechas anexoFechas = this.anexos.getAnexoCorreoFichas();
        AnexoCompleto anexoCompleto = this.anexos.getAnexoCompleto();

        builder.setTitle("Fechas Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato());
        if(anexoFechas != null){



            boolean inicioSiembraEnabled = anexoFechas.getCorreo_inicio_siembra() > 0;
            boolean inicioEnabled = anexoFechas.getCorreo_inicio_despano() > 0;
            boolean cincoPorcFloracionEnabled = anexoFechas.getCorreo_cinco_porciento_floracion() > 0;
            boolean inicioCorteSedaEnabled = anexoFechas.getCorreo_inicio_corte_seda() > 0;
            boolean inicioCosechaEnabled = anexoFechas.getCorreo_inicio_cosecha() > 0;
            boolean terminoCosechaEnabled = anexoFechas.getCorreo_termino_cosecha() > 0;
            boolean terminoLaboresEnabled = anexoFechas.getCorreo_termino_labores_post_cosechas() > 0;

            String inicioSiembra = (
                    anexoFechas.getInicio_siembra() != null &&
                    !anexoFechas.getInicio_siembra().equals("0000-00-00"))
                    ? Utilidades.voltearFechaVista(anexoFechas.getInicio_siembra())
                    : "";

            String inicioDespano = (
                    anexoFechas.getInicio_despano() != null &&
                    !anexoFechas.getInicio_despano().equals("0000-00-00"))
                    ? Utilidades.voltearFechaVista(anexoFechas.getInicio_despano())
                    : "";

            String cincoPorciento = (
                    anexoFechas.getCinco_porciento_floracion() != null &&
                            !anexoFechas.getCinco_porciento_floracion().equals("0000-00-00"))
                    ? Utilidades.voltearFechaVista(anexoFechas.getCinco_porciento_floracion())
                    : "";

            String inicioCorteSeda = (
                    anexoFechas.getInicio_corte_seda() != null &&
                            !anexoFechas.getInicio_corte_seda().equals("0000-00-00"))
                    ? Utilidades.voltearFechaVista(anexoFechas.getInicio_corte_seda())
                    : "";

            String inicioCosecha = (
                    anexoFechas.getInicio_cosecha() != null &&
                            !anexoFechas.getInicio_cosecha().equals("0000-00-00"))
                    ? Utilidades.voltearFechaVista(anexoFechas.getInicio_cosecha())
                    : "";

            String terminoCosecha = (
                    anexoFechas.getTermino_cosecha() != null &&
                            !anexoFechas.getTermino_cosecha().equals("0000-00-00"))
                    ? Utilidades.voltearFechaVista(anexoFechas.getTermino_cosecha())
                    : "";

            String terminoLaboresCosecha = (
                    anexoFechas.getTermino_labores_post_cosechas() != null &&
                            !anexoFechas.getTermino_labores_post_cosechas().equals("0000-00-00"))
                    ? Utilidades.voltearFechaVista(anexoFechas.getTermino_labores_post_cosechas())
                    : "";

            et_inicio_siembra.setEnabled(!inicioSiembraEnabled);
            et_inicio_despano.setEnabled(!inicioEnabled);
            et_cinco_porc_floracion.setEnabled(!cincoPorcFloracionEnabled);
            et_inicio_corte_seda.setEnabled(!inicioCorteSedaEnabled);
            et_inicio_cosecha.setEnabled(!inicioCosechaEnabled);
            et_inicio_cosecha_hora.setEnabled(!inicioCosechaEnabled);
            et_termino_cosecha.setEnabled(!terminoCosechaEnabled);
            et_termino_labores_post_cosecha.setEnabled(!terminoLaboresEnabled);


            et_inicio_siembra.setText(inicioSiembra);
            et_inicio_despano.setText(inicioDespano);
            et_cinco_porc_floracion.setText(cincoPorciento);
            et_inicio_corte_seda.setText(inicioCorteSeda);
            et_inicio_cosecha.setText(inicioCosecha);
            et_termino_cosecha.setText(terminoCosecha);
            et_termino_labores_post_cosecha.setText(terminoLaboresCosecha);
            et_inicio_cosecha_hora.setText(anexoFechas.getHora_inicio_cosecha());
            et_detalle_labores.setText(anexoFechas.getDetalle_labores());



            et_fecha_siembra_temprada.setText(Utilidades.voltearFechaVista(anexoFechas.getSiem_tempra_grami()));
            et_tipo_siembra_temprada.setText(anexoFechas.getTipo_graminea());

            et_fecha_destruccion.setText(Utilidades.voltearFechaVista(anexoFechas.getFecha_destruccion_semillero()));
            et_hora_destruccion.setText(anexoFechas.getHora_destruccion_semillero());
            if(anexoFechas.getCantidad_has_destruidas() != null){
                et_cantidad_ha_destruccion.setText(String.valueOf(anexoFechas.getCantidad_has_destruidas()));
            }
            et_motivo_destruccion.setText(anexoFechas.getMotivo_destruccion());

            boolean isDestructionComplete = (
                    anexoFechas.getDestruc_semill_ensayo() != null &&
                    anexoFechas.getDestruc_semill_ensayo().equals("PARCIAL"));

            boolean isCompleteChecked = (anexoFechas.getDestruc_semill_ensayo() != null &&
                    anexoFechas.getDestruc_semill_ensayo().equals("COMPLETO"));

            et_motivo_destruccion.setEnabled(!isDestructionComplete);
            et_cantidad_ha_destruccion.setEnabled(!isDestructionComplete);


            rbtn_completo.setChecked(isCompleteChecked);
            rbtn_parcial.setChecked(isDestructionComplete);

            rbtn_parcial.setEnabled(!isDestructionComplete);
            rbtn_completo.setEnabled(!isDestructionComplete);

            cont_detalle_destruccion.setVisibility( (isDestructionComplete || isCompleteChecked) ? View.VISIBLE : View.GONE);


        }



        onFocus();

        rbtn_parcial.setOnClickListener(view1 -> {
            cont_detalle_destruccion.setVisibility(View.VISIBLE);
            //eliminar data que tenga los cuadros de texto
        });

        rbtn_completo.setOnClickListener(view1 -> {
            cont_detalle_destruccion.setVisibility(View.VISIBLE);
        });
        btn_guardar_anexo_fecha.setOnClickListener(view1 -> onSave());
        btn_posponer_anexo_fecha.setOnClickListener(view1 -> cerrar());

        return builder.create();
    }


    private void onFocus () {


        et_inicio_cosecha_hora.setOnFocusChangeListener((view1, b) -> {
            if(b) Utilidades.levantarHora(et_inicio_cosecha_hora, getContext());
        });

        et_hora_destruccion.setOnFocusChangeListener((view1, b) -> {
            if(b) Utilidades.levantarHora(et_hora_destruccion, getContext());
        });


        et_inicio_siembra.setOnFocusChangeListener((view, b) -> {
            if(b) levantarFecha(et_inicio_siembra);
        });

        et_fecha_siembra_temprada.setOnFocusChangeListener((view, b) -> {
            if(b) levantarFecha(et_fecha_siembra_temprada);
        });

        et_fecha_destruccion.setOnFocusChangeListener((view, b) -> {
            if(b) levantarFecha(et_fecha_destruccion);
        });

        et_inicio_despano.setOnFocusChangeListener((v1, b) -> {
            if (b){ levantarFecha(et_inicio_despano);  }
        });

        et_cinco_porc_floracion.setOnFocusChangeListener((v1, b) -> {
            if (b){  levantarFecha(et_cinco_porc_floracion);  }
        });

        et_termino_cosecha.setOnFocusChangeListener((v1, b) -> {
            if (b){ levantarFecha(et_termino_cosecha); }
        });

        et_inicio_cosecha.setOnFocusChangeListener((v1, b) -> {
            if (b){ levantarFecha(et_inicio_cosecha); }
        });

        et_inicio_corte_seda.setOnFocusChangeListener((v1, b) -> {
            if (b){ levantarFecha(et_inicio_corte_seda);  }
        });

        et_termino_labores_post_cosecha.setOnFocusChangeListener((v1, b) -> {
            if (b){ levantarFecha(et_termino_labores_post_cosecha);  }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if( dialog != null ){
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
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

        Dialog dialog = getDialog();

        if(!et_termino_labores_post_cosecha.getText().toString().isEmpty() && et_detalle_labores.getText().toString().isEmpty()){
            String message = "Debes ingresar el detalle al ingresar el termino de labores ";
            Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            return;
        }

        if(!et_fecha_destruccion.getText().toString().isEmpty() && et_hora_destruccion.getText().toString().isEmpty()){
            String message = "Al ingresar fecha de destruccion debes ingresar la hora de destruccion ";
            Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            return;
        }

        if(et_fecha_destruccion.getText().toString().isEmpty() && !et_hora_destruccion.getText().toString().isEmpty()){
            String message = "Al ingresar hora de destruccion debes ingresar la fecha de destruccion ";
            Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            return;
        }

        boolean isDestructionComplete = rbtn_parcial.isChecked();
        boolean isCompleteChecked = rbtn_completo.isChecked();

        if((!et_fecha_destruccion.getText().toString().isEmpty() || !et_hora_destruccion.getText().toString().isEmpty()) && (!isDestructionComplete && !isCompleteChecked)){
            String message = "Al ingresar fecha u hora de destruccion debes seleccionar parcial o completo ";
            Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            return;
        }


        if( (!et_inicio_cosecha.getText().toString().isEmpty() && et_inicio_cosecha_hora.getText().toString().isEmpty()) ||
            (et_inicio_cosecha.getText().toString().isEmpty() && !et_inicio_cosecha_hora.getText().toString().isEmpty())){

            String message = "Para inicio de cosecha se debe ingresar fecha y hora. ";
            Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            return;

        }


        if(
           ( isDestructionComplete || isCompleteChecked) &&
            (
                et_cantidad_ha_destruccion.getText().toString().isEmpty() ||
                et_motivo_destruccion.getText().toString().isEmpty()
            )
        ){
            String message = "Debe ingresar cantidad y motivo. ";
            Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            return;
        }

        AnexoCorreoFechas anexoFechas = this.anexos.getAnexoCorreoFichas();
        AnexoCompleto anexoCompleto = this.anexos.getAnexoCompleto();

        AnexoCorreoFechas fhc = new AnexoCorreoFechas();

        fhc.setInicio_siembra(Utilidades.voltearFechaBD(et_inicio_siembra.getText().toString()));
        fhc.setInicio_despano(Utilidades.voltearFechaBD(et_inicio_despano.getText().toString()));
        fhc.setCinco_porciento_floracion(Utilidades.voltearFechaBD(et_cinco_porc_floracion.getText().toString()));
        fhc.setTermino_cosecha(Utilidades.voltearFechaBD(et_termino_cosecha.getText().toString()));
        fhc.setInicio_cosecha(Utilidades.voltearFechaBD(et_inicio_cosecha.getText().toString()));
        fhc.setInicio_corte_seda(Utilidades.voltearFechaBD(et_inicio_corte_seda.getText().toString()));
//        fhc.setTermino_cosecha(Utilidades.voltearFechaBD(et_termino_labores_post_cosecha.getText().toString()));
        fhc.setTermino_labores_post_cosechas(Utilidades.voltearFechaBD(et_termino_labores_post_cosecha.getText().toString()));
        fhc.setDetalle_labores(et_detalle_labores.getText().toString());
        fhc.setHora_inicio_cosecha(et_inicio_cosecha_hora.getText().toString());

        if(isDestructionComplete || isCompleteChecked){
            fhc.setCantidad_has_destruidas(Double.parseDouble(et_cantidad_ha_destruccion.getText().toString()));
            fhc.setMotivo_destruccion(et_motivo_destruccion.getText().toString());
        }

        fhc.setDestruc_semill_ensayo((isDestructionComplete) ? "PARCIAL" : (isCompleteChecked) ? "COMPLETO" : "");
        fhc.setFecha_destruccion_semillero(Utilidades.voltearFechaBD(et_fecha_destruccion.getText().toString()));
        fhc.setHora_destruccion_semillero(et_hora_destruccion.getText().toString());
        fhc.setTipo_graminea(et_tipo_siembra_temprada.getText().toString());
        fhc.setSiem_tempra_grami(et_fecha_siembra_temprada.getText().toString());


        fhc.setCorreo_inicio_siembra(anexoFechas == null ? 0 : anexoFechas.getCorreo_inicio_siembra());
        fhc.setCorreo_inicio_despano(anexoFechas == null ? 0 : anexoFechas.getCorreo_inicio_despano());
        fhc.setCorreo_cinco_porciento_floracion(anexoFechas == null ? 0 :anexoFechas.getCorreo_cinco_porciento_floracion());
        fhc.setCorreo_inicio_corte_seda(anexoFechas == null ? 0: anexoFechas.getCorreo_inicio_corte_seda());
        fhc.setCorreo_inicio_cosecha(anexoFechas == null ? 0: anexoFechas.getCorreo_inicio_cosecha());
        fhc.setCorreo_termino_cosecha(anexoFechas == null ? 0: anexoFechas.getCorreo_termino_cosecha());
        fhc.setCorreo_termino_labores_post_cosechas(anexoFechas == null ? 0: anexoFechas.getCorreo_termino_labores_post_cosechas());

        Config config = MainActivity.myAppDB.myDao().getConfig();
        fhc.setId_fieldman(config.getId_usuario());
        fhc.setId_fieldman(config.getId_usuario());
        fhc.setId_ac_corr_fech(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        fhc.setEstado_sincro_corr_fech(0);




        if(anexoFechas != null){
            fhc.setId_ac_cor_fech(anexoFechas.getId_ac_cor_fech());
            int id = MainActivity.myAppDB.DaoAnexosFechas().UpdateFechasAnexos(fhc);
            if(id > 0){
                Toasty.success(requireActivity(), "Fechas guardada con exito", Toast.LENGTH_SHORT, true).show();
            }else{
                Toasty.error(requireActivity(), "No se pudo guardar la fecha", Toast.LENGTH_SHORT, true).show();
            }
            this.IOnSave.onSave( (id > 0), this.query );
            if( dialog != null ){ dialog.dismiss();}
            return;
        }

        long id = MainActivity.myAppDB.DaoAnexosFechas().insertFechasAnexos(fhc);
        if(id > 0){
            Toasty.success(requireActivity(), "Fechas guardada con exito", Toast.LENGTH_SHORT, true).show();
        }else{
            Toasty.error(requireActivity(), "No se pudo guardar la fecha", Toast.LENGTH_SHORT, true).show();
        }

        this.IOnSave.onSave( (id > 0), this.query );
        if( dialog != null ){ dialog.dismiss();}
    }

    private void levantarFecha(final EditText edit){


        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota;

        if (!TextUtils.isEmpty(edit.getText())){
            try{ fechaRota = Utilidades.voltearFechaBD(edit.getText().toString()).split("-");  }
            catch (Exception e){ fechaRota = fecha.split("-");  }
        } else{  fechaRota = fecha.split("-");  }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, dayOfMonth) -> {

            month = month + 1;

            String mes = "", dia;

            if (month < 10) {  mes = "0" + month; }
            else {  mes = String.valueOf(month); }

            if (dayOfMonth < 10) dia = "0" + dayOfMonth;
            else dia = String.valueOf(dayOfMonth);

            String finalDate = dia + "-" + mes + "-" + year;
            edit.setText(finalDate);
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
        datePickerDialog.show();

    }

    public void bind(View view){

        et_inicio_despano = view.findViewById(R.id.et_inicio_despano);
        et_inicio_siembra = view.findViewById(R.id.et_inicio_siembra);        et_cinco_porc_floracion = view.findViewById(R.id.et_cinco_porc_floracion);
        et_inicio_corte_seda = view.findViewById(R.id.et_inicio_corte_seda);
        et_inicio_cosecha = view.findViewById(R.id.et_inicio_cosecha);
        et_inicio_cosecha_hora = view.findViewById(R.id.et_inicio_cosecha_hora);
        et_termino_cosecha = view.findViewById(R.id.et_termino_cosecha);
        et_termino_labores_post_cosecha = view.findViewById(R.id.et_termino_labores_post_cosecha);
        et_detalle_labores = view.findViewById(R.id.et_detalle_labores);

        btn_guardar_anexo_fecha = view.findViewById(R.id.btn_guardar_anexo_fecha);
        btn_posponer_anexo_fecha = view.findViewById(R.id.btn_posponer_anexo_fecha);


        et_fecha_siembra_temprada = view.findViewById(R.id.et_fecha_siembra_temprada);
        et_tipo_siembra_temprada = view.findViewById(R.id.et_tipo_siembra_temprada);
        et_fecha_destruccion = view.findViewById(R.id.et_fecha_destruccion);
        et_hora_destruccion = view.findViewById(R.id.et_hora_destruccion);
        et_cantidad_ha_destruccion = view.findViewById(R.id.et_cantidad_ha_destruccion);
        et_motivo_destruccion = view.findViewById(R.id.et_motivo_destruccion);

        rbtn_parcial = view.findViewById(R.id.rbtn_parcial);
        rbtn_completo = view.findViewById(R.id.rbtn_completo);


        cont_detalle_destruccion = view.findViewById(R.id.cont_detalle_destruccion);


    }
}
