package cl.smapdev.curimapu.clases.bd.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;

@Dao
public interface DaoCheckListSiembra {

    @Delete
    void deleteClSiembra(CheckListSiembra checkListSiembra);

    @Insert
    long insertClSiembra(CheckListSiembra checkListSiembra);

    @Update
    int updateClSiembra(CheckListSiembra checkListSiembra);


    @Query("SELECT * FROM anexo_checklist_siembra WHERE id_ac_cl_siembra = :id_ac;")
    List<CheckListSiembra> getAllClSiembraByAc(int id_ac);


    @Query("DELETE FROM anexo_checklist_siembra WHERE id_cl_siembra = :id_siembra; ")
    void deleteClSiembraById(int id_siembra);

    @Query("SELECT * FROM anexo_checklist_siembra WHERE estado_sincronizacion = 0; ")
    List<CheckListSiembra> getClSiembraToSync();


    @Query("SELECT * FROM anexo_checklist_siembra WHERE id_cl_siembra = :id ;")
    CheckListSiembra getClSiembraById( int id );

    @Query("SELECT * FROM anexo_checklist_siembra WHERE clave_unica = :clave_unica ")
    CheckListSiembra getCLSiembraByClaveUnica( String clave_unica );

    @Query("SELECT * FROM anexo_checklist_siembra WHERE id_cl_siembra = :id AND estado_sincronizacion = :estado_sinc ;")
    CheckListSiembra getClSiembraById( int id , int estado_sinc);

}
