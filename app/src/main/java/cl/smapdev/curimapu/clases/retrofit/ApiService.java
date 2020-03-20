package cl.smapdev.curimapu.clases.retrofit;

import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("descarga_de_datos.php")
    Call<GsonDescargas> descargarDatos(@Query("id") int id, @Query("id_usuario") int id_user);

    @GET("primera_descarga.php")
    Call<GsonDescargas> descargaPrimera(@Query("imei") String imei, @Query("id") int id);

    @POST("subida_de_datos.php")
    Call<Respuesta> enviarDatos(@Body SubidaDatos subidaDatos);

    @GET("comprobacion.php")
    Call<Respuesta> comprobacion(@Query("id") int id_dispo, @Query("id_usuario") int id_user, @Query("id_cab") int id_cab);
}
