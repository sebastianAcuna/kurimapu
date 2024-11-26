package cl.smapdev.curimapu.clases.bd.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListGuiaInterna;

@Dao
public interface DaoCheckGuiaInterna {

    @Delete
    void deleteClGuiaInterna(CheckListGuiaInterna checklist);

    @Insert
    long insertClGuiaInterna(CheckListGuiaInterna checklist);

    @Update
    int updateClGuiaInterna(CheckListGuiaInterna checklist);


    @Query("SELECT * FROM anexo_checklist_guia_interna WHERE id_ac_cl_guia_interna = :id_ac;")
    List<CheckListGuiaInterna> getAllClGuiaInternaByAc(int id_ac);


    @Query("DELETE FROM anexo_checklist_guia_interna WHERE id_cl_guia_interna = :id_siembra; ")
    void deleteClGuiaInternaById(int id_siembra);

    @Query("SELECT * FROM anexo_checklist_guia_interna WHERE estado_sincronizacion = 0; ")
    List<CheckListGuiaInterna> getClGuiaInternaToSync();


    @Query("SELECT * FROM anexo_checklist_guia_interna WHERE id_cl_guia_interna = :id ;")
    CheckListGuiaInterna getClGuiaInternaById(int id);

    @Query("SELECT * FROM anexo_checklist_guia_interna WHERE clave_unica = :clave_unica ")
    CheckListGuiaInterna getCLGuiaInternaByClaveUnica(String clave_unica);

    @Query("SELECT * FROM anexo_checklist_guia_interna WHERE id_cl_guia_interna = :id AND estado_sincronizacion = :estado_sinc ;")
    CheckListGuiaInterna getClGuiaInternaById(int id, int estado_sinc);

}
