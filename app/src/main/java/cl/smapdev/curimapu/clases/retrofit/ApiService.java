package cl.smapdev.curimapu.clases.retrofit;

import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import cl.smapdev.curimapu.clases.relaciones.SubirFechasRetro;
import cl.smapdev.curimapu.clases.relaciones.resFecha;
import cl.smapdev.curimapu.clases.tablas.WeatherApiStatus;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("descarga_de_datos.php")
    Call<GsonDescargas> descargarDatos(@Query("id") int id, @Query("id_usuario") int id_user, @Query("version") String version);

    @GET("primera_descarga.php")
    Call<GsonDescargas> descargaPrimera(@Query("imei") String imei, @Query("id") int id, @Query("version") String version);

    @POST("subida_de_datos.php")
    Call<Respuesta> enviarDatos(@Body SubidaDatos subidaDatos);


//    @Multipart
    @POST("subir_checklists.php")
    Call<Respuesta> subirCheckList(@Body CheckListRequest checkListRequest);



    @POST("comprobar_servidor.php")
    Call<Respuesta> comprobarServidor(@Body SubidaDatos subidaDatos);


    @POST("subir_fechas.php")
    Call<resFecha> enviarFechas(@Body SubirFechasRetro subidaDatos);

    @GET("comprobacion.php")
    Call<Respuesta> comprobacion(@Query("id") int id_dispo, @Query("id_usuario") int id_user, @Query("id_cab") int id_cab);


    @GET("index.php")
    Call<WeatherApiStatus> getData(
            @Query("api_lang") String language,
            @Query("localidad") String localidad,
            @Query("affiliate_id") String userId,
            @Query("v") String version);


}
