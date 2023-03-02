package cl.smapdev.curimapu.clases.bd.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;

@Dao
public interface DaoCheckListCosecha {

    @Delete
    void deleteClCosecha(CheckListCosecha checkListCosecha);

    @Insert
    long insertClCosecha(CheckListCosecha checkListCosecha);

    @Update
    int updateClCosecha(CheckListCosecha checkListCosecha);


    @Query("SELECT * FROM anexo_checklist_cosecha WHERE id_ac_cl_siembra = :id_ac;")
    List<CheckListCosecha> getAllClCosechaByAc(int id_ac);


    @Query("DELETE FROM anexo_checklist_cosecha WHERE id_cl_siembra = :id_siembra; ")
    void deleteClCosechaById(int id_siembra);

    @Query("SELECT * FROM anexo_checklist_cosecha WHERE estado_sincronizacion = 0; ")
    List<CheckListCosecha> getClCosechaToSync();


    @Query("SELECT * FROM anexo_checklist_cosecha WHERE id_cl_siembra = :id ;")
    CheckListCosecha getClCosechaById( int id );

    @Query("SELECT * FROM anexo_checklist_cosecha WHERE clave_unica = :clave_unica ")
    CheckListCosecha getCLCosechaByClaveUnica( String clave_unica );

    @Query("SELECT * FROM anexo_checklist_cosecha WHERE id_cl_siembra = :id AND estado_sincronizacion = :estado_sinc ;")
    CheckListCosecha getClCosechaById( int id , int estado_sinc);

}
