package cl.smapdev.curimapu.clases.utilidades;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.CheckListCapCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListLimpiezaCamionesCompleto;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.FichasNew;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Descargas {

    public static void primeraDescarga(final MainActivity activity, String imei, int id, String version) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Espere un momento...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();

        Config config = MainActivity.myAppDB.myDao().getConfig();

        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<GsonDescargas> call = apiService.descargaPrimera(imei, id, version);
        call.enqueue(new Callback<GsonDescargas>() {
            @Override
            public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {
                int problema = 0;
                GsonDescargas gsonDescargas = response.body();
                if (gsonDescargas != null) {
                    int id = gsonDescargas.getId_dispo();

                    int codigoRespuesta = 1;
                    if (gsonDescargas.getRespuestas() != null && gsonDescargas.getRespuestas().size() > 0) {

                        for (Respuesta rsp : gsonDescargas.getRespuestas()) {
                            if (rsp.getCodigoRespuesta() == 5) {
                                codigoRespuesta = 5;
                                break;
                            }
                        }
                    }

                    if (codigoRespuesta > 1) {

                        Utilidades.avisoListo(activity, "ATENCION", "NO TIENES LA ULTIMA VERSION DE LA APLICACION, FAVOR ACTUALIZAR", "ENTIENDO");
                        problema = 1;
                    } else {
                        if (id > 0) {
                            Config config = MainActivity.myAppDB.myDao().getConfig();
                            if (config == null) {
                                Config config1 = new Config();
                                config1.setId(id);
                                config1.setServidorSeleccionado(Utilidades.URL_SERVER_API);
                                MainActivity.myAppDB.myDao().setConfig(config1);
                            } else {
                                MainActivity.myAppDB.myDao().updateConfig(id);
                            }
                        }
                        if (gsonDescargas.getUsuarios() != null && gsonDescargas.getUsuarios().size() > 0) {
                            try {
                                MainActivity.myAppDB.myDao().deleteUsuario();
                                List<Long> inserts = MainActivity.myAppDB.myDao().setUsuarios(gsonDescargas.getUsuarios());
                                int c = 1;
                                for (long l : inserts) {
                                    if (l <= 0) {
                                        problema = 1;
                                    } else {
                                        progressDialog.setProgress(c * 100 / inserts.size());
                                        c++;
                                    }
                                }
                            } catch (SQLiteException e) {
                                problema = 1;
                                Log.e("SQLITE", e.getMessage());
                            }
                        }
                        if (gsonDescargas.getTemporadas() != null && gsonDescargas.getTemporadas().size() > 0) {
                            try {

                                MainActivity.myAppDB.myDao().deleteTemporadas();
                                List<Long> inserts = MainActivity.myAppDB.myDao().insertTemporada(gsonDescargas.getTemporadas());

                            } catch (SQLiteException e) {
                                Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                                Log.e("SQLITE", e.getMessage());
                            }
                        } else {
                            MainActivity.myAppDB.myDao().deleteTemporadas();
                        }

                        MainActivity.myAppDB.myDao().deleteEspecie();
                        if (gsonDescargas.getEspecieList() != null && !gsonDescargas.getEspecieList().isEmpty()) {
                            try {
                                MainActivity.myAppDB.myDao().insertEspecie(gsonDescargas.getEspecieList());

                            } catch (SQLiteException e) {
                                Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                                Log.e("SQLITE", e.getMessage());
                            }
                        }
                    }
                } else {
                    problema = 1;
                }

                if (problema > 0) {
                    Toasty.error(activity, "No se pudo descargar todo", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                } else {
                    Toasty.success(activity, "Todo descargado con exito", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
                Toasty.error(activity, "No se pudo descargar todo", Toast.LENGTH_SHORT, true).show();
                progressDialog.dismiss();
            }
        });

    }


    public static boolean[] volqueoDatos(GsonDescargas gsonDescargas) throws RuntimeException {

        boolean[] problema = {false, false};

        if (gsonDescargas.getArray_fechas_anexos() != null && !gsonDescargas.getArray_fechas_anexos().isEmpty()) {
            try {
                for (AnexoCorreoFechas fch : gsonDescargas.getArray_fechas_anexos()) {
                    AnexoCorreoFechas f = MainActivity.myAppDB.DaoAnexosFechas().getAnexoCorreoFechasByAnexo(fch.getId_ac_corr_fech());
                    if (f != null) {
                        f.setCorreo_cinco_porciento_floracion(fch.getCorreo_cinco_porciento_floracion());
                        f.setCorreo_inicio_corte_seda(fch.getCorreo_inicio_corte_seda());
                        f.setCorreo_inicio_cosecha(fch.getCorreo_inicio_cosecha());
                        f.setCorreo_inicio_despano(fch.getCorreo_inicio_despano());
                        f.setCorreo_termino_cosecha(fch.getCorreo_termino_cosecha());
                        f.setCorreo_termino_labores_post_cosechas(fch.getCorreo_termino_labores_post_cosechas());
                        f.setCorreo_destruccion_semillero(fch.getCorreo_destruccion_semillero());
                        f.setCorreo_siembra_temprana(fch.getCorreo_siembra_temprana());
                        f.setCorreo_inicio_siembra(fch.getCorreo_inicio_siembra());

                        f.setInicio_siembra(fch.getInicio_siembra());
                        f.setCinco_porciento_floracion(fch.getCinco_porciento_floracion());
                        f.setInicio_corte_seda(fch.getInicio_corte_seda());
                        f.setInicio_cosecha(fch.getInicio_cosecha());
                        f.setInicio_despano(fch.getInicio_despano());
                        f.setTermino_cosecha(fch.getTermino_cosecha());
                        f.setTermino_labores_post_cosechas(fch.getTermino_labores_post_cosechas());

                        f.setCantidad_has_destruidas(fch.getCantidad_has_destruidas());
                        f.setDestruc_semill_ensayo(fch.getDestruc_semill_ensayo());
                        f.setMotivo_destruccion(fch.getMotivo_destruccion());
                        f.setTipo_graminea(fch.getTipo_graminea());
                        f.setSiem_tempra_grami(fch.getSiem_tempra_grami());
                        f.setId_fieldman(fch.getId_fieldman());
                        f.setHora_destruccion_semillero(fch.getHora_destruccion_semillero());
                        f.setFecha_destruccion_semillero(fch.getFecha_destruccion_semillero());
                        f.setHora_inicio_cosecha(fch.getHora_inicio_cosecha());
                        f.setDetalle_labores(fch.getDetalle_labores());
                        f.setEstado_sincro_corr_fech(1);

                        MainActivity.myAppDB.DaoAnexosFechas().UpdateFechasAnexos(f);
                    } else {
                        MainActivity.myAppDB.DaoAnexosFechas().insertFechasAnexos(fch);
                    }
                }
            } catch (SQLiteException e) {
                Log.e("SQLITE", e.getMessage());
                problema[0] = true;
            }
        }

//        MainActivity.myAppDB.myDao().deleteProCliMat();
        if (gsonDescargas.getPro_cli_matList() != null && !gsonDescargas.getPro_cli_matList().isEmpty()) {
            try {
                MainActivity.myAppDB.myDao().insertInterfaz(gsonDescargas.getPro_cli_matList());
            } catch (SQLiteException ignored) {
            }
        }

        if (gsonDescargas.getChecklistDevolucionSemillas() != null && !gsonDescargas.getChecklistDevolucionSemillas().isEmpty()) {
            try {
                for (ChecklistDevolucionSemilla ck : gsonDescargas.getChecklistDevolucionSemillas()) {
                    ChecklistDevolucionSemilla chk = MainActivity.myAppDB.DaoCheckListDevolucionSemilla().getCLDevolucionSemillaByClaveUnica(ck.getClave_unica());
                    if (chk != null) {
                        ck.setId_cl_devolucion_semilla(chk.getId_cl_devolucion_semilla());
                        MainActivity.myAppDB.DaoCheckListDevolucionSemilla().updateClDevolucionSemilla(ck);
                    } else {
                        MainActivity.myAppDB.DaoCheckListDevolucionSemilla().insertClDevolucionSemilla(ck);
                    }
                }
            } catch (SQLiteException ignored) {
            }
        }

        if (gsonDescargas.getCheckListSiembras() != null && !gsonDescargas.getCheckListSiembras().isEmpty()) {
            try {
                for (CheckListSiembra ck : gsonDescargas.getCheckListSiembras()) {
                    CheckListSiembra chk = MainActivity.myAppDB.DaoClSiembra().getCLSiembraByClaveUnica(ck.getClave_unica());
                    if (chk != null) {
                        ck.setId_cl_siembra(chk.getId_cl_siembra());
                        MainActivity.myAppDB.DaoClSiembra().updateClSiembra(ck);
                    } else {
                        MainActivity.myAppDB.DaoClSiembra().insertClSiembra(ck);
                    }
                }
            } catch (SQLiteException ignored) {
            }
        }

        if (gsonDescargas.getCheckListCosecha() != null && !gsonDescargas.getCheckListCosecha().isEmpty()) {
            try {
                for (CheckListCosecha ck : gsonDescargas.getCheckListCosecha()) {
                    CheckListCosecha chk = MainActivity.myAppDB.DaoCheckListCosecha().getCLCosechaByClaveUnica(ck.getClave_unica());

                    if (chk != null) {
                        ck.setId_cl_siembra(chk.getId_cl_siembra());
                        MainActivity.myAppDB.DaoCheckListCosecha().updateClCosecha(ck);
                    } else {
                        MainActivity.myAppDB.DaoCheckListCosecha().insertClCosecha(ck);
                    }
                }
            } catch (SQLiteException ignored) {
            }

        }


        if (gsonDescargas.getCheckListLimpiezaCamionesCompletos() != null && !gsonDescargas.getCheckListLimpiezaCamionesCompletos().isEmpty()) {
            for (CheckListLimpiezaCamionesCompleto ck : gsonDescargas.getCheckListLimpiezaCamionesCompletos()) {
                CheckListLimpiezaCamiones chk = MainActivity.myAppDB
                        .DaoCheckListLimpiezaCamiones()
                        .getClLimpiezaCamionesByClaveUnica(ck.getCabecera().getClave_unica());
                //update
                if (chk != null) {
                    ck.getCabecera().setId_cl_limpieza_camiones(chk.getId_cl_limpieza_camiones());
                    MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                            .updateLimpiezaCamiones(ck.getCabecera());
                } else {
                    //insert
                    MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                            .insertLimpiezaCamiones(ck.getCabecera());
                }

                for (ChecklistLimpiezaCamionesDetalle detalle : ck.getDetalles()) {

                    ChecklistLimpiezaCamionesDetalle chkDF = MainActivity.myAppDB
                            .DaoCheckListLimpiezaCamiones()
                            .getLimpiezaCamionesDetallesByClaveUnica(
                                    detalle.getClave_unica_cl_limpieza_camiones_detalle());


                    if (chkDF != null) {
                        detalle.setId_ac_cl_limpieza_camiones_detalle(chkDF.getId_ac_cl_limpieza_camiones_detalle());
                        MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                .updateDetalle(detalle);
                    } else {
                        //insert
                        ChecklistLimpiezaCamionesDetalle det = new ChecklistLimpiezaCamionesDetalle();
                        det.setLimpieza_anterior_limpieza_camiones(detalle.getLimpieza_anterior_limpieza_camiones());
                        det.setClave_unica_cl_limpieza_camiones_detalle(detalle.getClave_unica_cl_limpieza_camiones_detalle());
                        det.setClave_unica_cl_limpieza_camiones(detalle.getClave_unica_cl_limpieza_camiones());
                        det.setNombre_chofer_limpieza_camiones(detalle.getNombre_chofer_limpieza_camiones());
                        det.setPatente_camion_limpieza_camiones(detalle.getPatente_camion_limpieza_camiones());
                        det.setPatente_carro_limpieza_camiones(detalle.getPatente_carro_limpieza_camiones());
                        det.setEstado_general_recepcion_camion_campo_limpieza_camiones(detalle.getEstado_general_recepcion_camion_campo_limpieza_camiones());
                        det.setEquipo_utilizado_limpieza_camiones(detalle.getEquipo_utilizado_limpieza_camiones());
                        det.setLimpieza_puertas_laterales_limpieza_camiones(detalle.getLimpieza_puertas_laterales_limpieza_camiones());
                        det.setLimpieza_puertas_traseras_limpieza_camiones(detalle.getLimpieza_puertas_traseras_limpieza_camiones());
                        det.setLimpieza_piso_limpieza_camiones(detalle.getLimpieza_piso_limpieza_camiones());
                        det.setInspeccion_rejillas_mallas_limpieza_camiones(detalle.getInspeccion_rejillas_mallas_limpieza_camiones());
                        det.setPisos_costados_batea_sin_orificios_limpieza_camiones(detalle.getPisos_costados_batea_sin_orificios_limpieza_camiones());
                        det.setCamion_carro_limpio_limpieza_camiones(detalle.getCamion_carro_limpio_limpieza_camiones());
                        det.setCarpa_limpia_limpieza_camiones(detalle.getCarpa_limpia_limpieza_camiones());
                        det.setSistema_cerrado_puertas_limpieza_camiones(detalle.getSistema_cerrado_puertas_limpieza_camiones());
                        det.setNivel_llenado_carga_limpieza_camiones(detalle.getNivel_llenado_carga_limpieza_camiones());
                        det.setSello_color_indica_condicion_limpieza_camiones(detalle.getSello_color_indica_condicion_limpieza_camiones());
                        det.setEtiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones(detalle.getEtiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones());
                        det.setSello_verde_curimapu_cierre_camion_limpieza_camiones(detalle.getSello_verde_curimapu_cierre_camion_limpieza_camiones());
                        det.setFirma_cl_limpieza_camiones_detalle(detalle.getFirma_cl_limpieza_camiones_detalle());
                        det.setStringed_cl_limpieza_camiones_detalle(detalle.getStringed_cl_limpieza_camiones_detalle());
                        det.setEstado_sincronizacion_detalle(detalle.getEstado_sincronizacion_detalle());
                        MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                .insertLimpiezaCamionesDetalle(det);
                    }

                }

            }
        }


        if (gsonDescargas.getCheckListCapCompletos() != null && !gsonDescargas.getCheckListCapCompletos().isEmpty()) {
            for (CheckListCapCompleto ck : gsonDescargas.getCheckListCapCompletos()) {
                CheckListCapacitacionSiembra chk = MainActivity.myAppDB
                        .DaoCheckListCapSiembra()
                        .getClCapSiembraByClaveUnica(ck.getCabecera().getClave_unica());
                //update
                if (chk != null) {
                    ck.getCabecera().setId_cl_cap_siembra(chk.getId_cl_cap_siembra());
                    MainActivity.myAppDB.DaoCheckListCapSiembra()
                            .updateCapacitacionSiembra(ck.getCabecera());
                } else {
                    //insert
                    MainActivity.myAppDB.DaoCheckListCapSiembra()
                            .insertCapacitacionSiembra(ck.getCabecera());
                }


                for (CheckListCapacitacionSiembraDetalle detalle : ck.getDetalles()) {

                    CheckListCapacitacionSiembraDetalle chkDF = MainActivity.myAppDB
                            .DaoCheckListCapSiembra()
                            .getCapSiembraDetallesByClaveUnica(
                                    detalle.getClave_unica_cl_cap_siembra_detalle());

                    if (chkDF != null) {
                        detalle.setId_cl_cap_siembra_detalle(chkDF.getId_cl_cap_siembra_detalle());
                        MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .updateDetalle(detalle);
                    } else {
                        //insert
                        MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .insertCapacitacionSiembraDetalle(detalle);
                    }
                }
            }
        }


        if (gsonDescargas.getEvaluaciones() != null && !gsonDescargas.getEvaluaciones().isEmpty()) {

            for (Evaluaciones ck : gsonDescargas.getEvaluaciones()) {
                Evaluaciones chk = MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByClaveUnica(ck.getClave_unica_recomendacion());
                if (chk != null) {
                    ck.setId_ac_recom(chk.getId_ac_recom());
                    MainActivity.myAppDB.DaoEvaluaciones().updateEvaluaciones(ck);
                } else {
                    MainActivity.myAppDB.DaoEvaluaciones().insertEvaluaciones(ck);
                }
            }
        }


//        MainActivity.myAppDB.myDao().deleteDetalle();
        if (gsonDescargas.getDetalle_visita_props() != null && !gsonDescargas.getDetalle_visita_props().isEmpty()) {
            try {
                MainActivity.myAppDB.myDao().insertDetalle(gsonDescargas.getDetalle_visita_props());
            } catch (SQLiteException ignored) {
            }
        }


//        MainActivity.myAppDB.myDao().deleteVisitas();
        if (gsonDescargas.getVisitasList() != null && !gsonDescargas.getVisitasList().isEmpty()) {
            try {

                MainActivity.myAppDB.myDao().setVisita(gsonDescargas.getVisitasList());
                for (Visitas ln : gsonDescargas.getVisitasList()) {
                    MainActivity.myAppDB.myDao().updateFotos(ln.getId_visita(), ln.getId_visita_local(), ln.getId_dispo());
                }
            } catch (SQLiteException e) {
                Log.e("SQLITE", e.getMessage());
                problema[0] = true;
            }
        }

//        MainActivity.myAppDB.myDao().deleteAnexos();
        if (gsonDescargas.getAnexoContratoList() != null && !gsonDescargas.getAnexoContratoList().isEmpty()) {
            try {
                MainActivity.myAppDB.myDao().insertAnexo(gsonDescargas.getAnexoContratoList());
            } catch (SQLiteException e) {
                Log.e("SQLITE", e.getMessage());
                problema[0] = true;
            }
        }


//        MainActivity.myAppDB.myDao().deleteQuotation();
        if (gsonDescargas.getQuotations() != null && !gsonDescargas.getQuotations().isEmpty()) {
            try {
                MainActivity.myAppDB.myDao().insertQuotation(gsonDescargas.getQuotations());
            } catch (SQLiteException e) {
                problema[0] = true;
                Log.e("SQLITE", e.getMessage());
            }
        }

//        MainActivity.myAppDB.myDao().deleteCliPCM();
        if (gsonDescargas.getCli_pcms() != null && !gsonDescargas.getCli_pcms().isEmpty()) {
            try {
                MainActivity.myAppDB.myDao().insertPCM(gsonDescargas.getCli_pcms());
            } catch (SQLiteException e) {
                Log.e("SQLITE", e.getMessage());
                problema[0] = true;
            }
        }


//        MainActivity.myAppDB.myDao().deleteFichas();
        if (gsonDescargas.getFichasList() != null && !gsonDescargas.getFichasList().isEmpty()) {
            try {

                MainActivity.myAppDB.myDao().insertFicha(gsonDescargas.getFichasList());
                Config config = MainActivity.myAppDB.myDao().getConfig();
                if (config != null) {
                    for (FichasNew ln : gsonDescargas.getFichasList()) {
                        MainActivity.myAppDB.myDao().updateFotosFichas(ln.getId_ficha(), ln.getId_ficha_local_ficha(), config.getId());
                    }
                }

            } catch (SQLiteException e) {
                problema[0] = true;
                Log.e("SQLITE", e.getMessage());
            }
        }

//        MainActivity.myAppDB.myDao().deleteProspectos();
        if (gsonDescargas.getProspectosList() != null && !gsonDescargas.getProspectosList().isEmpty()) {
            try {
                MainActivity.myAppDB.myDao().insertProsectos(gsonDescargas.getProspectosList());
            } catch (SQLiteException e) {
                problema[0] = true;
                Log.e("SQLITE", e.getMessage());
            }
        }


        try {

//            MainActivity.myAppDB.myDao().deleteAgriPredTemp();
            if (gsonDescargas.getPred_agr_temp() != null && !gsonDescargas.getPred_agr_temp().isEmpty()) {
                MainActivity.myAppDB.myDao().insertAgriPredTemp(gsonDescargas.getPred_agr_temp());
            }

//            MainActivity.myAppDB.myDao().deleteUsuario();
            if (gsonDescargas.getUsuarios() != null && !gsonDescargas.getUsuarios().isEmpty()) {
                MainActivity.myAppDB.myDao().setUsuarios(gsonDescargas.getUsuarios());
            }


            if (gsonDescargas.getAnexoVilabList() != null && !gsonDescargas.getAnexoVilabList().isEmpty()) {
//                MainActivity.myAppDB.DaoVilab().clearVilabTable();
                MainActivity.myAppDB.DaoVilab().insertVilab(gsonDescargas.getAnexoVilabList());
            }


            if (gsonDescargas.getArray_muestra_humedad() != null && !gsonDescargas.getArray_muestra_humedad().isEmpty()) {
//                MainActivity.myAppDB.DaoMuestraHumedad().deleteSyncedSamples();
                MainActivity.myAppDB.DaoMuestraHumedad().insertMuestraHumedad(gsonDescargas.getArray_muestra_humedad());
            }


//            MainActivity.myAppDB.DaoPrimeraPrioridad().limpiarPPs();
            if (gsonDescargas.getArray_primera_prioridad() != null && !gsonDescargas.getArray_primera_prioridad().isEmpty()) {
                MainActivity.myAppDB.DaoPrimeraPrioridad().insertarPPs(gsonDescargas.getArray_primera_prioridad());
            }

//            MainActivity.myAppDB.DaoSitiosNoVisitados().limpiarSNVs();
            if (gsonDescargas.getArray_sitios_no_visitados() != null && !gsonDescargas.getArray_sitios_no_visitados().isEmpty()) {
                MainActivity.myAppDB.DaoSitiosNoVisitados().insertarSNVs(gsonDescargas.getArray_sitios_no_visitados());
            }


            if (gsonDescargas.getTemporadas() != null && !gsonDescargas.getTemporadas().isEmpty()) {
//                MainActivity.myAppDB.myDao().deleteTemporadas();

                MainActivity.myAppDB.myDao().insertTemporada(gsonDescargas.getTemporadas());
            }

//            MainActivity.myAppDB.myDao().deleteCrops();
            if (gsonDescargas.getCropRotations() != null && !gsonDescargas.getCropRotations().isEmpty()) {
                MainActivity.myAppDB.myDao().insertCrop(gsonDescargas.getCropRotations());
            }

//            MainActivity.myAppDB.myDao().deleteAgricultores();
            if (gsonDescargas.getAgricultorList() != null && !gsonDescargas.getAgricultorList().isEmpty()) {
                MainActivity.myAppDB.myDao().insertAgricultor(gsonDescargas.getAgricultorList());
            }


//            MainActivity.myAppDB.myDao().deleteRegiones();
            if (gsonDescargas.getRegionList() != null && !gsonDescargas.getRegionList().isEmpty()) {
                MainActivity.myAppDB.myDao().insertRegiones(gsonDescargas.getRegionList());
            }


//            MainActivity.myAppDB.myDao().deleteEspecie();
            if (gsonDescargas.getEspecieList() != null && !gsonDescargas.getEspecieList().isEmpty()) {
                MainActivity.myAppDB.myDao().insertEspecie(gsonDescargas.getEspecieList());
            }


//            MainActivity.myAppDB.myDao().deleteClientes();
            if (gsonDescargas.getClientes() != null && !gsonDescargas.getClientes().isEmpty()) {
                MainActivity.myAppDB.myDao().insertClientes(gsonDescargas.getClientes());
            }

//            MainActivity.myAppDB.myDao().deleteFichaMaquinaria();
            if (gsonDescargas.getFichaMaquinarias() != null && !gsonDescargas.getFichaMaquinarias().isEmpty()) {
                MainActivity.myAppDB.myDao().insertFichaMaquinaria(gsonDescargas.getFichaMaquinarias());
            }

//            MainActivity.myAppDB.myDao().deleteTipoTenenciaTerreno();
            if (gsonDescargas.getTipoTenenciaTerrenos() != null && !gsonDescargas.getTipoTenenciaTerrenos().isEmpty()) {
                MainActivity.myAppDB.myDao().insertTipoTenenciaTerreno(gsonDescargas.getTipoTenenciaTerrenos());
            }

//            MainActivity.myAppDB.myDao().deleteTipoTenenciaMaquinaria();
            if (gsonDescargas.getTipoTenenciaMaquinarias() != null && !gsonDescargas.getTipoTenenciaMaquinarias().isEmpty()) {
                MainActivity.myAppDB.myDao().insertTipoTenenciaMaquinaria(gsonDescargas.getTipoTenenciaMaquinarias());
            }

//            MainActivity.myAppDB.myDao().deleteMaquinaria();
            if (gsonDescargas.getMaquinarias() != null && !gsonDescargas.getMaquinarias().isEmpty()) {
                MainActivity.myAppDB.myDao().insertMaquinara(gsonDescargas.getMaquinarias());
            }

//            MainActivity.myAppDB.myDao().deleteTipoSuelo();
            if (gsonDescargas.getTipoSuelos() != null && !gsonDescargas.getTipoSuelos().isEmpty()) {
                MainActivity.myAppDB.myDao().insertTipoSuelo(gsonDescargas.getTipoSuelos());
            }

//            MainActivity.myAppDB.myDao().deleteTipoRiego();
            if (gsonDescargas.getTipoRiegos() != null && !gsonDescargas.getTipoRiegos().isEmpty()) {
                MainActivity.myAppDB.myDao().insertTipoRiego(gsonDescargas.getTipoRiegos());
            }

//            MainActivity.myAppDB.myDao().deleteLotes();
            if (gsonDescargas.getLotes() != null && !gsonDescargas.getLotes().isEmpty()) {
                MainActivity.myAppDB.myDao().insertLotes(gsonDescargas.getLotes());
            }

//            MainActivity.myAppDB.myDao().deletePredios();
            if (gsonDescargas.getPredios() != null && !gsonDescargas.getPredios().isEmpty()) {
                MainActivity.myAppDB.myDao().insertPredios(gsonDescargas.getPredios());
            }

//            MainActivity.myAppDB.myDao().deleteUM();
            if (gsonDescargas.getUnidadMedidas() != null && !gsonDescargas.getUnidadMedidas().isEmpty()) {
                MainActivity.myAppDB.myDao().insertUM(gsonDescargas.getUnidadMedidas());
            }

//            MainActivity.myAppDB.myDao().deleteVariedad();
            if (gsonDescargas.getVariedadList() != null && !gsonDescargas.getVariedadList().isEmpty()) {
                MainActivity.myAppDB.myDao().insertVariedad(gsonDescargas.getVariedadList());
            }

//            MainActivity.myAppDB.myDao().deleteComuna();
            if (gsonDescargas.getComunaList() != null && !gsonDescargas.getComunaList().isEmpty()) {
                MainActivity.myAppDB.myDao().insertComunas(gsonDescargas.getComunaList());
            }

//            MainActivity.myAppDB.myDao().deleteProvincia();
            if (gsonDescargas.getProvinciaList() != null && !gsonDescargas.getProvinciaList().isEmpty()) {
                MainActivity.myAppDB.myDao().insertProvincias(gsonDescargas.getProvinciaList());
            }


        } catch (SQLiteException ignored) {
            problema[0] = true;
        }


        Config config = MainActivity.myAppDB.myDao().getConfig();

        if (config != null) {
            config.setHoraDescarga(Utilidades.hora());
            MainActivity.myAppDB.myDao().updateConfig(config);
        }


        return problema;
    }
}
