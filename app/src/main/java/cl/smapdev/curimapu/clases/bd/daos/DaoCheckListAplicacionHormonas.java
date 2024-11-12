package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListAplicacionHormonas;

@Dao
public interface DaoCheckListAplicacionHormonas {

    @Query("SELECT * FROM checklist_aplicacion_hormonas WHERE id_ac_cl_ap_hormonas = :id_ac;")
    List<CheckListAplicacionHormonas> getAllClApHormonasByAc(int id_ac);

    @Query("SELECT * FROM checklist_aplicacion_hormonas WHERE id_cl_ap_hormonas = :id;")
    CheckListAplicacionHormonas getAllClApHormonasById(int id);

    @Query("SELECT * FROM checklist_aplicacion_hormonas WHERE clave_unica = :claveUnica;")
    CheckListAplicacionHormonas getAllClApHormonasByClaveUnica(String claveUnica);

    @Query("SELECT * FROM checklist_aplicacion_hormonas WHERE id_cl_ap_hormonas = :id AND estado_sincronizacion = :estado ;")
    CheckListAplicacionHormonas getAllClApHormonasByIdAndEstado(int id, int estado);

    @Query("SELECT * FROM checklist_aplicacion_hormonas WHERE estado_sincronizacion = 0")
    List<CheckListAplicacionHormonas> getClApHormonasToSync();


    @Query("DELETE FROM checklist_aplicacion_hormonas;")
    void deleteAll();

    @Update
    void updateClApHormonas(CheckListAplicacionHormonas checklistAplicacionHormonas);


    @Insert
    long insertCLApHormonas(CheckListAplicacionHormonas checklistAplicacionHormonas);

    @Insert
    List<Long> insertCLApHormonas(List<CheckListAplicacionHormonas> checklistAplicacionHormonas);


    @Update
    int updateCLApHormonas(CheckListAplicacionHormonas checklistAplicacionHormonas);

}
