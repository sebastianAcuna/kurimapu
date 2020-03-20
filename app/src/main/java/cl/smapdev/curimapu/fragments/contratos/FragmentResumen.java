package cl.smapdev.curimapu.fragments.contratos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentResumen extends Fragment {

    private MainActivity activity = null ;

    private TextView fecha_resumen, estado_fenologico, estado_general, estado_crecimiento, estado_maleza, estado_fito, humedad_suelo, cosecha, observation, recomendaciones,
            anexo ,orden_culplica, cliente, especie, variedad, ready_batch, raw_batch, grower, predio, potrero, siembra_sag, sag_register_number, irrigation_system, soil_type,
            production_location, has_contrato, has_customer, fieldman, rch,ptos_ampros;

    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();


        if (activity != null) prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resumen, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);


        if (prefs != null){
            llenarResumen(MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")));
        }


    }



    private void llenarResumen(VisitasCompletas visitasCompletas){

        if (visitasCompletas != null){
                /* visitas */
            if (visitasCompletas.getVisitas() != null){
                fecha_resumen.setText(Utilidades.voltearFechaBD(visitasCompletas.getVisitas().getFecha_visita()));
                estado_fenologico.setText(visitasCompletas.getVisitas().getPhenological_state_visita());
                estado_general.setText(visitasCompletas.getVisitas().getOverall_status_visita());
                estado_crecimiento.setText(visitasCompletas.getVisitas().getGrowth_status_visita());
                estado_maleza.setText(visitasCompletas.getVisitas().getWeed_state_visita());
                estado_fito.setText(visitasCompletas.getVisitas().getPhytosanitary_state_visita());
                humedad_suelo.setText(visitasCompletas.getVisitas().getHumidity_floor_visita());
                cosecha.setText(visitasCompletas.getVisitas().getHarvest_visita());
                observation.setText(visitasCompletas.getVisitas().getObservation_visita());
                recomendaciones.setText(visitasCompletas.getVisitas().getRecomendation_visita());


                Usuario usuario = MainActivity.myAppDB.myDao().getUsuarioById(visitasCompletas.getVisitas().getId_user_visita());
                if (usuario != null){
                    String nombre = usuario.getNombre() + " " + usuario.getApellido_p() + " " + usuario.getApellido_m();
                    fieldman.setText(nombre);
                }
            }

            /*anexo*/
            if (visitasCompletas.getAnexoCompleto() != null){

                anexo.setText(visitasCompletas.getAnexoCompleto().getAnexoContrato().getAnexo_contrato());
                orden_culplica.setText("¿?");

                if (visitasCompletas.getClientes() != null){

                    cliente.setText(visitasCompletas.getClientes().getRazon_social_clientes());
                }

                especie.setText(visitasCompletas.getAnexoCompleto().getEspecie().getDesc_especie());
                variedad.setText(visitasCompletas.getAnexoCompleto().getVariedad().getDesc_variedad());

                ready_batch.setText("¿?");
                raw_batch.setText("¿?");

                if (visitasCompletas.getAnexoCompleto().getLotes() != null){
                    grower.setText((visitasCompletas.getAnexoCompleto().getLotes().getNombre_ac() != null) ? visitasCompletas.getAnexoCompleto().getLotes().getNombre_ac() : "");
                    potrero.setText(visitasCompletas.getAnexoCompleto().getLotes().getNombre_lote());
                    ptos_ampros.setText(visitasCompletas.getAnexoCompleto().getLotes().getCoo_utm_ampros());
                }

                if (visitasCompletas.getAnexoCompleto().getPredios() != null){
                    predio.setText(visitasCompletas.getAnexoCompleto().getPredios().getNombre());
                }






                siembra_sag.setText("¿?");
                sag_register_number.setText("¿?");
                irrigation_system.setText("¿?");
                soil_type.setText("¿?");
                production_location.setText("¿?");
                has_contrato.setText("¿?");
                has_customer.setText("¿?");
                rch.setText("¿?");


            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity != null){
            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumenes, FragmentFotos.getInstance(1), Utilidades.FRAGMENT_FOTOS).commit();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
//                Toast.makeText(activity, "Visible resumen", Toast.LENGTH_SHORT).show();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumenes, FragmentFotos.getInstance(1), Utilidades.FRAGMENT_FOTOS).commit();
            }
        }
    }


    private void bind(View view){

        fecha_resumen = (TextView) view.findViewById(R.id.fecha_resumen);
        estado_fenologico = (TextView) view.findViewById(R.id.estado_fenologico);
        estado_general = (TextView) view.findViewById(R.id.estado_general);
        estado_crecimiento = (TextView) view.findViewById(R.id.estado_crecimiento);
        estado_maleza = (TextView) view.findViewById(R.id.estado_maleza);
        estado_fito = (TextView) view.findViewById(R.id.estado_fito);
        humedad_suelo = (TextView) view.findViewById(R.id.humedad_suelo);
        cosecha = (TextView) view.findViewById(R.id.cosecha);
        observation = (TextView) view.findViewById(R.id.observation);
        recomendaciones = (TextView) view.findViewById(R.id.recomendaciones);
        anexo = (TextView) view.findViewById(R.id.anexo);
        orden_culplica = (TextView) view.findViewById(R.id.orden_culplica);
        cliente = (TextView) view.findViewById(R.id.cliente);
        especie = (TextView) view.findViewById(R.id.especie);
        variedad = (TextView) view.findViewById(R.id.variedad);
        ready_batch = (TextView) view.findViewById(R.id.ready_batch);
        raw_batch = (TextView) view.findViewById(R.id.raw_batch);
        grower = (TextView) view.findViewById(R.id.grower);
        predio = (TextView) view.findViewById(R.id.predio);
        potrero = (TextView) view.findViewById(R.id.potrero);
        siembra_sag = (TextView) view.findViewById(R.id.siembra_sag);
        sag_register_number = (TextView) view.findViewById(R.id.sag_register_number);
        irrigation_system = (TextView) view.findViewById(R.id.irrigation_system);
        soil_type = (TextView) view.findViewById(R.id.soil_type);
        production_location = (TextView) view.findViewById(R.id.production_location);
        has_contrato = (TextView) view.findViewById(R.id.has_contrato);
        has_customer = (TextView) view.findViewById(R.id.has_customer);
        fieldman = (TextView) view.findViewById(R.id.fieldman);
        rch = (TextView) view.findViewById(R.id.rch);
        ptos_ampros = (TextView) view.findViewById(R.id.ptos_ampros);

    }

}
