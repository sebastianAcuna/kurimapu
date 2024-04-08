package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.MuestraHumedad;


@Dao
public interface DaoMuestraHumedad {

    //    sincronizacion

    @Query("SELECT * FROM muestra_humedad WHERE estado_sincronizacion_muestrao = 0 AND estado_documento_muestra = 1 ")
    List<MuestraHumedad> getMuestrasToSync();

    @Query("SELECT * FROM muestra_humedad WHERE id_ac_muestra_humedad = :id_ac ")
    List<MuestraHumedad> getMuestrasByAc(int id_ac);


    @Query("SELECT * FROM muestra_humedad WHERE clave_unica_muestra = :clave_unica ")
    MuestraHumedad getMuestraByClaveUnica(String clave_unica);


    @Query("DELETE FROM muestra_humedad WHERE estado_sincronizacion_muestrao = 1; ")
    void deleteSyncedSamples();

    @Update
    void updateMuestraHumedad(MuestraHumedad muestraHumedad);

    @Insert
    void insertMuestraHumedad(MuestraHumedad muestraHumedad);

    @Insert
    void insertMuestraHumedad(List<MuestraHumedad> muestraHumedads);


}
