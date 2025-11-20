package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.PrimeraPrioridad;

@Dao
public interface DaoPrimeraPrioridad {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarPPs(List<PrimeraPrioridad> prioridadList);

    @Query("DELETE FROM primera_prioridad;")
    void limpiarPPs();


    @Query("SELECT * FROM anexo_contrato WHERE temporada_anexo = :temporada ORDER BY fecha ASC")
    List<AnexoCompleto> getPPByTemporada(int temporada);


}
