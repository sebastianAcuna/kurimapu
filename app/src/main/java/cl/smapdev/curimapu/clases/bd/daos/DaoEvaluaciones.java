package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.Evaluaciones;

@Dao
public interface DaoEvaluaciones {

    @Insert
    long insertEvaluaciones(Evaluaciones evaluaciones);

    @Insert
    List<Long> insertEvaluaciones(List<Evaluaciones> evaluaciones);


    @Delete
    void deleteEvaluaciones(Evaluaciones evaluaciones);

    @Query("DELETE FROM anexo_recomendaciones ")
    void deleteAllEvaluaciones();

    @Update
    int updateEvaluaciones(Evaluaciones evaluaciones);

    @Query("SELECT * FROM anexo_recomendaciones WHERE id_ac = :id_ace ")
    List<Evaluaciones> getEvaluacionesByAC( int id_ace );

    @Query("SELECT * FROM anexo_recomendaciones WHERE id_ac = :id_ac AND estado = :estado ")
    List<Evaluaciones> getEvaluacionesByEstado( int id_ac, String estado );

    @Query("UPDATE anexo_recomendaciones SET marca_evaluacion_server = 1 WHERE estado_server = 0 AND id_ac = :id_ac; ")
    void marcarEvaluaciones(int id_ac);

    @Query("UPDATE anexo_recomendaciones SET  estado_server = 0  WHERE  marca_evaluacion_server = 1 ;")
    int updateEvaluacionesTomadasBack();

    @Query("UPDATE anexo_recomendaciones SET estado_server = 1, cabecera_server = :idCab, marca_evaluacion_server = 0 WHERE estado_server = 0 AND marca_evaluacion_server = 1; ")
    int updateEvaluacionesSubidasTomada(int idCab);
}
