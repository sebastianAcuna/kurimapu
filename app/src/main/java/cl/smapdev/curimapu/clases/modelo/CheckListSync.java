package cl.smapdev.curimapu.clases.modelo;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.CheckListRoguingCompleto;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.CheckListAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.CheckListGuiaInterna;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoCabecera;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckListSync {

    public interface ResponseData {
        void onResponseData(boolean state, String message);
    }

    private ResponseData IResponse;
    private CheckListRequest checkListRequest;
    private Context context;


    public CheckListSync(CheckListRequest checkListRequest, Context context, ResponseData IResponse) {
        this.IResponse = IResponse;
        this.checkListRequest = checkListRequest;
        this.context = context;

        subirCheckList();
    }

    private void subirCheckList() {

        ProgressDialog pd = new ProgressDialog(this.context);
        pd.setTitle("Preparando datos para subir...");
        pd.setCancelable(false);
        pd.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());


        if (checkListRequest.getCheckListAplicacionHormonas() != null &&
                !checkListRequest.getCheckListAplicacionHormonas().isEmpty()) {

            List<CheckListAplicacionHormonas> clHormonas = new ArrayList<>();

            for (CheckListAplicacionHormonas completo : checkListRequest.getCheckListAplicacionHormonas()) {


                if (completo.getFirma_responsable_ap_hormonas() != null &&
                        !completo.getFirma_responsable_ap_hormonas().isEmpty()) {

                    String stringed = Utilidades.imageToString(completo.getFirma_responsable_ap_hormonas());
                    completo.setStringed_firma_responsable_ap_hormonas(stringed.isEmpty() ? "" : stringed);
                }

                if (completo.getFirma_prestador_servicio_ap_hormonas() != null &&
                        !completo.getFirma_prestador_servicio_ap_hormonas().isEmpty()) {

                    String stringed = Utilidades.imageToString(completo.getFirma_prestador_servicio_ap_hormonas());
                    completo.setStringer_firma_prestador_servicio_ap_hormonas(stringed.isEmpty() ? "" : stringed);
                }


                clHormonas.add(completo);

            }

            checkListRequest.setCheckListAplicacionHormonas(clHormonas);

        }

        if (checkListRequest.getCheckListSiembras() != null
                && !checkListRequest.getCheckListSiembras().isEmpty()) {
            List<CheckListSiembra> chkS = new ArrayList<>();
            for (CheckListSiembra chk : checkListRequest.getCheckListSiembras()) {


                if (chk.getRuta_foto_envase() != null && !chk.getRuta_foto_envase().isEmpty()) {
                    String stringed = Utilidades.imageToString(chk.getRuta_foto_envase());
                    chk.setStringed_foto_envase(stringed.isEmpty() ? "" : stringed);
                }

                if (chk.getRuta_foto_semilla() != null && !chk.getRuta_foto_semilla().isEmpty()) {
                    String stringed = Utilidades.imageToString(chk.getRuta_foto_semilla());
                    chk.setStringed_foto_semilla(stringed.isEmpty() ? "" : stringed);
                }

                chkS.add(chk);
            }

            checkListRequest.setCheckListSiembras(chkS);
        }


        if (checkListRequest.getCheckListGuiaInternas() != null && !checkListRequest.getCheckListGuiaInternas().isEmpty()) {
            List<CheckListGuiaInterna> cl = new ArrayList<>();

            for (CheckListGuiaInterna completo : checkListRequest.getCheckListGuiaInternas()) {
                if (completo.getFirma_supervisor() != null &&
                        !completo.getFirma_supervisor().isEmpty()) {
                    String stringed = Utilidades.imageToString(completo.getFirma_supervisor());
                    completo.setStringed_firma_supervisor(stringed.isEmpty() ? "" : stringed);
                }

                if (completo.getFirma_transportista() != null &&
                        !completo.getFirma_transportista().isEmpty()) {
                    String stringed = Utilidades.imageToString(completo.getFirma_transportista());
                    completo.setStringed_firma_transportista(stringed.isEmpty() ? "" : stringed);
                }
                cl.add(completo);
            }

            checkListRequest.setCheckListGuiaInternas(cl);
        }

        if (checkListRequest.getCheckListRoguing() != null && !checkListRequest.getCheckListRoguing().isEmpty()) {
            List<CheckListRoguingCompleto> ccpl = new ArrayList<>();

            for (CheckListRoguingCompleto c : checkListRequest.getCheckListRoguing()) {
                CheckListRoguingCompleto ccp = new CheckListRoguingCompleto();
                ccp.setCheckListRoguing(c.getCheckListRoguing());
                ccp.setCheckListRoguingDetalle(c.getCheckListRoguingDetalle());
                ccp.setCheckListRoguingDetalleFechas(c.getCheckListRoguingDetalleFechas());

                if (c.getCheckListFotoCabecera() != null && !c.getCheckListFotoCabecera().isEmpty()) {
                    List<CheckListRoguingFotoCabecera> cab = new ArrayList<>();
                    for (CheckListRoguingFotoCabecera fc : c.getCheckListFotoCabecera()) {
                        if (fc.getRuta() != null && !fc.getRuta().isEmpty()) {
                            {
                                String stringed = Utilidades.imageToString(fc.getRuta());
                                fc.setStringed_foto(stringed.isEmpty() ? "" : stringed);
                                cab.add(fc);
                            }
                        }
                    }
                    ccp.setCheckListFotoCabecera(cab);
                }

                if (c.getCheckListFotoDetalle() != null && !c.getCheckListFotoDetalle().isEmpty()) {
                    List<CheckListRoguingFotoDetalle> cab = new ArrayList<>();
                    for (CheckListRoguingFotoDetalle fc : c.getCheckListFotoDetalle()) {
                        if (fc.getRuta() != null && !fc.getRuta().isEmpty()) {
                            {
                                String stringed = Utilidades.imageToString(fc.getRuta());
                                fc.setStringed_foto(stringed.isEmpty() ? "" : stringed);
                                cab.add(fc);
                            }
                        }
                    }
                    ccp.setCheckListFotoDetalle(cab);
                }
                ccpl.add(ccp);
            }

            checkListRequest.setCheckListRoguing(ccpl);

        }

        try {
            Config config = configFuture.get();
            checkListRequest.setIdDispo(config.getId());
            checkListRequest.setIdUsuario(config.getId_usuario());

            ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);

            Call<Respuesta> call = apiService.subirCheckList(checkListRequest);

            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {

                    if (response.isSuccessful()) {

                        Respuesta res = response.body();

                        if (res == null) {
                            IResponse.onResponseData(false, "Respuesta nula");
                            pd.dismiss();
                            executor.shutdown();
                            return;
                        }

                        if (res.getCodigoRespuesta() != 0) {
                            IResponse.onResponseData(false, res.getMensajeRespuesta());
                            pd.dismiss();
                            executor.shutdown();
                            return;
                        }


                        if (checkListRequest.getCheckListRoguing() != null && !checkListRequest.getCheckListRoguing().isEmpty()) {

                            for (CheckListRoguingCompleto chk : checkListRequest.getCheckListRoguing()) {
                                chk.getCheckListRoguing().setEstado_sincronizacion(1);

                                for (CheckListRoguingDetalle c : chk.getCheckListRoguingDetalle()) {
                                    c.setEstado_sincronizacion(1);
                                    try {
                                        executor.submit(() -> MainActivity.myAppDB
                                                .DaoCLRoguing()
                                                .updateDetalleRoguing(c)).get();
                                    } catch (ExecutionException | InterruptedException e) {
                                    }
                                }

                                try {
                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoCLRoguing()
                                            .updateclroguing(chk.getCheckListRoguing())).get();
                                } catch (ExecutionException | InterruptedException e) {
                                }

                            }
                        }


                        if (checkListRequest.getCheckListGuiaInternas() != null && !checkListRequest.getCheckListGuiaInternas().isEmpty()) {
                            for (CheckListGuiaInterna chk : checkListRequest.getCheckListGuiaInternas()) {
                                chk.setEstado_sincronizacion(1);

                                try {
                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoCLGuiaInterna()
                                            .updateClGuiaInterna(chk)).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (checkListRequest.getCheckListAplicacionHormonas() != null
                                && !checkListRequest.getCheckListAplicacionHormonas().isEmpty()) {
                            for (CheckListAplicacionHormonas chk : checkListRequest.getCheckListAplicacionHormonas()) {
                                chk.setEstado_sincronizacion(1);

                                try {
                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoCLAplicacionHormonas()
                                            .updateCLApHormonas(chk)).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        if (checkListRequest.getCheckListSiembras() != null
                                && !checkListRequest.getCheckListSiembras().isEmpty()) {
                            for (CheckListSiembra chk : checkListRequest.getCheckListSiembras()) {

                                chk.setEstado_sincronizacion(1);

                                try {
                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoClSiembra()
                                            .updateClSiembra(chk)).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        IResponse.onResponseData(true, res.getMensajeRespuesta());
                        pd.dismiss();
                        executor.shutdown();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                    pd.dismiss();
                    IResponse.onResponseData(false, t.getMessage());
                    executor.shutdown();
                }
            });


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            IResponse.onResponseData(false, e.getMessage());
            executor.shutdown();
            pd.dismiss();
        }


    }


}
