package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.DesplegableAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.DesplegableNumeroAplicacionHormonas;

@Dao
public interface DaoDesplegables {


    @Insert
    void insertDesplegableNumeroAplicacionHormonas(DesplegableNumeroAplicacionHormonas desplegableNumeroAplicacionHormonas);

    @Insert
    void insertDesplegableNumeroAplicacionHormonas(List<DesplegableNumeroAplicacionHormonas> desplegableNumeroAplicacionHormonas);

    @Insert
    void insertDesplegableAplicacionHormonas(DesplegableAplicacionHormonas desplegableAplicacionHormonas);

    @Insert
    void insertDesplegableAplicacionHormonas(List<DesplegableAplicacionHormonas> desplegableAplicacionHormonas);


    @Query("DELETE FROM desplegable_numero_aplicacion_hormonas;")
    void deleteDesplegableNumeroAplicacionHormonas();

    @Query("DELETE FROM desplegable_aplicacion_hormonas;")
    void deleteDesplegableAplicacionHormonas();


    @Query("SELECT * FROM desplegable_numero_aplicacion_hormonas;")
    List<DesplegableNumeroAplicacionHormonas> getDesplegableNumeroAplicacionHormonas();

    @Query("SELECT * FROM desplegable_aplicacion_hormonas;")
    List<DesplegableAplicacionHormonas> getDesplegableAplicacionHormonas();


}
