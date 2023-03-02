package cl.smapdev.curimapu.clases.modelo;

import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.WeatherApiStatus;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import retrofit2.Call;

public class WeatherApiRequest {

    ApiService apiService;
    String localidad;

    public WeatherApiRequest(String localidad) {
        this.localidad = localidad;
        this.apiService = RetrofitClient
                .getWeather()
                .create(ApiService.class);
    }


    public Call<WeatherApiStatus> obtenerData(){


        return this.apiService.getData(
                "cl",
                this.localidad,
                Utilidades.affiliate_id,
                "3.0");
    }
}
