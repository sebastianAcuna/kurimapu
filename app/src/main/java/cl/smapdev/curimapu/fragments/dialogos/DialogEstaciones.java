package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionDetalleAdapter;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogEstaciones extends DialogFragment {








    private TextView tv_cantidad_machos;
    private Button   btn_agregar_muestra;
    private RecyclerView rv_lista_muestra;
    private Button   btn_guardar_muestra;
    private Button   btn_cancelar_muestra;

    private IOnSave IOnSave;

    public interface IOnSave {
        void onSave( boolean saved );
    }

    private EstacionFloracion estacion;
    private EstacionesCompletas estacionesCompletas;

    private int cantidadMachos;

    public void setCantidadMachos(int cantidadMachos) {
        this.cantidadMachos = cantidadMachos;
    }

    public void setEstacionesCompletas(EstacionesCompletas estacionesCompletas) {
        this.estacionesCompletas = estacionesCompletas;
    }

    public void setEstacion(EstacionFloracion estacion) {
        this.estacion = estacion;
    }

    public static DialogEstaciones newInstance(EstacionFloracion estacion, EstacionesCompletas estacionesCompletas, int cantidadMachos, IOnSave onSave){
        DialogEstaciones dg = new DialogEstaciones();
        dg.setIOnSave( onSave );
        dg.setCantidadMachos(cantidadMachos);
        dg.setEstacionesCompletas(estacionesCompletas);
        dg.setEstacion(estacion);
        return dg;
    }

    public void setIOnSave(IOnSave onSave){ this.IOnSave = onSave; }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_estaciones, null);
        builder.setView(view);

        bind(view);

        builder.setTitle("Nueva Estacion");

        tv_cantidad_machos.setText(String.valueOf(cantidadMachos));
        btn_agregar_muestra.setOnClickListener(v -> {
            if(cantidadMachos == 0){
                Toasty.error(requireContext(), "Debes ingresar la cantidad de machos antes de agregar las muestras", Toast.LENGTH_LONG, true).show();
                return;
            }

            int contadorMachos = getCantidadMachos();

            String tipo = (contadorMachos == cantidadMachos) ? "H" : "M";
            String titulo = (contadorMachos == cantidadMachos) ? "H" : "M"+(contadorMachos + 1);


            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_MUESTRA_ESTCION);
            if (prev != null) {
                ft.remove(prev);
            }

            DialogMuestraEstacion dialogo = DialogMuestraEstacion.newInstance( ((estacionesCompletas != null) ? estacionesCompletas.getEstaciones() : null),titulo,tipo, null,  saved -> cargarListaAsistentes());
            dialogo.show(ft, Utilidades.DIALOG_TAG_MUESTRA_ESTCION);
        });
        btn_guardar_muestra.setOnClickListener(view1 -> onSave());
        btn_cancelar_muestra.setOnClickListener(view1 -> cerrar());

        cargarListaAsistentes();

        return builder.create();
    }


    public void cargarListaAsistentes(){

        String claveUnica = (estacionesCompletas != null ) ? estacionesCompletas.getEstaciones().getClave_unica_floracion_estaciones() : "0";
            List<EstacionFloracionDetalle> details  = getDetalle(claveUnica);
            habilitarBotonAgregarMuestra(details);
            EstacionFloracionDetalleAdapter adapter = new EstacionFloracionDetalleAdapter(details,
                    (v, eliminar) -> {},
                    (v, edit)->{
                        if(cantidadMachos == 0){
                            Toasty.error(requireContext(), "Debes ingresar la cantidad de machos antes de agregar las muestras", Toast.LENGTH_LONG, true).show();
                            return;
                        }
                        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                        Fragment prev = requireActivity()
                                .getSupportFragmentManager()
                                .findFragmentByTag(Utilidades.DIALOG_TAG_MUESTRA_ESTCION);
                        if (prev != null) {
                            ft.remove(prev);
                        }

                        DialogMuestraEstacion dialogo = DialogMuestraEstacion.newInstance( ((estacionesCompletas != null) ? estacionesCompletas.getEstaciones() : null),edit.getTituloDato(),edit.getTipo_dato(), edit,  saved -> cargarListaAsistentes());
                        dialogo.show(ft, Utilidades.DIALOG_TAG_MUESTRA_ESTCION);
                    },
                    requireActivity()
            );
            rv_lista_muestra.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            rv_lista_muestra.setHasFixedSize(true);
            rv_lista_muestra.setAdapter(adapter);
    }



    private List<EstacionFloracionDetalle> getDetalle(String claveUnica){

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<EstacionFloracionDetalle>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoEstacionFloracion()
                        .getDetalleByClaveEstacion(claveUnica));

        try {
            return futureDetails.get();
        } catch (ExecutionException | InterruptedException e) {
            return Collections.emptyList();
        }
    }

    private int getCantidadMachos(){
        String claveUnica = (estacionesCompletas != null ) ? estacionesCompletas.getEstaciones().getClave_unica_floracion_estaciones() : "0";
        List<EstacionFloracionDetalle> detalles  = getDetalle(claveUnica);

        int contadorM = 0;
        for (EstacionFloracionDetalle floracionDetalle : detalles){
            if(floracionDetalle.getTipo_dato().equals("M")) contadorM++;
        }

        habilitarBotonAgregarMuestra(detalles);

        return contadorM;
    }


    public void habilitarBotonAgregarMuestra(List<EstacionFloracionDetalle> details){
        btn_agregar_muestra.setEnabled((details.size() < cantidadMachos + 1));
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
        String claveUnica = (estacionesCompletas != null ) ? estacionesCompletas.getEstaciones().getClave_unica_floracion_estaciones() : "0";
        List<EstacionFloracionDetalle> detalles  = getDetalle(claveUnica);

        if(detalles.size() != cantidadMachos + 1){
            Toasty.error(requireActivity(), "Debes Agregar los elementos requeridos ("+cantidadMachos+" machos y 1 hembra) ", Toast.LENGTH_LONG, true).show();
            return;
        }

        EstacionFloracionEstaciones  estacionSave = new EstacionFloracionEstaciones();
        estacionSave.setClave_unica_floracion_cabecera((estacion != null) ? estacion.getClave_unica_floracion() : "0");
        estacionSave.setClave_unica_floracion_estaciones((estacionesCompletas != null ? estacionesCompletas.getEstaciones().getClave_unica_floracion_estaciones() : UUID.randomUUID().toString()));




        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        try {
        if(estacionesCompletas != null){
            estacionSave.setId_estacion_floracion_estaciones(estacionesCompletas.getEstaciones().id_estacion_floracion_estaciones);

            executor2.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion()
                    .updateEstacion(estacionSave)).get();
        }else{

            executor2.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion()
                    .insertEstacion(estacionSave)).get();
        }

            executor2.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion()
                    .updateDetalleClaveUnicaEstacion(estacionSave.clave_unica_floracion_estaciones)).get();

            if(estacion != null){
                estacion.setEstado_sincronizacion(0);
                executor2.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion()
                        .updateEstacionCabecera(estacion)).get();
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Dialog dialog = getDialog();
        IOnSave.onSave( true);
        if( dialog != null ){ dialog.dismiss();}
    }


    public void bind(View view){

        tv_cantidad_machos = view.findViewById(R.id.tv_cantidad_machos);
        btn_agregar_muestra = view.findViewById(R.id.btn_agregar_muestra);
        rv_lista_muestra     = view.findViewById(R.id.rv_lista_muestra);

        btn_guardar_muestra = view.findViewById(R.id.btn_guardar_muestra);
        btn_cancelar_muestra = view.findViewById(R.id.btn_cancelar_muestra);


        String claveUnica = (estacionesCompletas != null ) ? estacionesCompletas.getEstaciones().getClave_unica_floracion_estaciones() : "0";
        List<EstacionFloracionDetalle> detalles  = getDetalle(claveUnica);
        habilitarBotonAgregarMuestra(detalles);

    }

}
