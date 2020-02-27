package cl.smapdev.curimapu.clases.retrofit;

import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("descarga_de_datos.php")
    Call<GsonDescargas> descargarDatos();
}
