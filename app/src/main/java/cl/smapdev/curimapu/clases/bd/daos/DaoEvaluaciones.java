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

    @Query("SELECT * FROM anexo_recomendaciones WHERE clave_unica_visita = :claveUnica AND estado = 'P' ")
    List<Evaluaciones> getEvaluacionesClon(String claveUnica);

    @Query("UPDATE anexo_recomendaciones SET obliga_visita = 0, clave_unica_visita = :claveUnica WHERE id_ac = :id_ace AND estado_server = 0 ")
    int updateEvaluacionesObligadas(int id_ace, String claveUnica);

    @Query("UPDATE anexo_recomendaciones SET clave_unica_visita = :claveUnica, obliga_visita = 0  WHERE id_ac = :id_ace AND estado_server = 0 ")
    int updateEvaluacionesGuardar(int id_ace, String claveUnica);


    @Query("SELECT * FROM anexo_recomendaciones WHERE id_ac = :id_ace ")
    List<Evaluaciones> getEvaluacionesByAC(int id_ace);

    @Query("SELECT * FROM anexo_recomendaciones WHERE clave_unica_recomendacion = :claveUnica ")
    Evaluaciones getEvaluacionesByClaveUnica(String claveUnica);

    @Query("SELECT * FROM anexo_recomendaciones WHERE id_ac = :id_ac AND estado = :estado ORDER BY anexo_recomendaciones.fecha_hora_tx DESC ")
    List<Evaluaciones> getEvaluacionesByEstado(int id_ac, String estado);

    @Query("SELECT * FROM anexo_recomendaciones WHERE id_ac = :id_ace AND  obliga_visita = 1 ")
    List<Evaluaciones> getEvaluacionesByACObliga(int id_ace);

    @Query("SELECT * FROM anexo_recomendaciones WHERE estado_server = 0 ")
    List<Evaluaciones> getEvaluacionesPendientesSync();

    @Query("UPDATE anexo_recomendaciones SET marca_evaluacion_server = 1 WHERE estado_server = 0 AND id_ac = :id_ac; ")
    void marcarEvaluaciones(int id_ac);

    @Query("UPDATE anexo_recomendaciones SET  estado_server = 0  WHERE  marca_evaluacion_server = 1 ;")
    int updateEvaluacionesTomadasBack();

    @Query("UPDATE anexo_recomendaciones SET estado_server = 1, cabecera_server = :idCab, marca_evaluacion_server = 0 WHERE estado_server = 0 AND marca_evaluacion_server = 1; ")
    int updateEvaluacionesSubidasTomada(int idCab);
}
