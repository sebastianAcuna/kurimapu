package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.SitiosNoVisitados;

@Dao
public interface DaoSitiosNoVisitados {

    @Insert
    void insertarSNVs(List<SitiosNoVisitados> sitiosNoVisitados);

    @Query("DELETE FROM sitios_no_visitados;")
    void limpiarSNVs();


    @Query("SELECT * FROM sitios_no_visitados WHERE id_temporada = :temporada ORDER BY nombreEspecie ASC, numAnexo ASC; ")
    List<SitiosNoVisitados> getSNVByTemporada(int temporada);


}
