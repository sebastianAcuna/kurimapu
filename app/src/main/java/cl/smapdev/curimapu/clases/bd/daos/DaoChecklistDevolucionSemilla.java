package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;

@Dao
public interface DaoChecklistDevolucionSemilla {

    @Delete
    void deleteClDevolucionSemilla(ChecklistDevolucionSemilla checklistDevolucionSemilla);

    @Insert
    long insertClDevolucionSemilla(ChecklistDevolucionSemilla checklistDevolucionSemilla);

    @Update
    int updateClDevolucionSemilla(ChecklistDevolucionSemilla checklistDevolucionSemilla);


    @Query("SELECT * FROM anexo_checklist_devolucion_semilla WHERE id_ac_cl_devolucion_semilla = :id_ac;")
    List<ChecklistDevolucionSemilla> getAllClDevolucionSemillaByAc(int id_ac);


    @Query("DELETE FROM anexo_checklist_devolucion_semilla WHERE id_cl_devolucion_semilla = :id_siembra; ")
    void deleteClDevolucionSemillaById(int id_siembra);

    @Query("SELECT * FROM anexo_checklist_devolucion_semilla WHERE estado_sincronizacion = 0; ")
    List<ChecklistDevolucionSemilla> getClDevolucionSemillaToSync();


    @Query("SELECT * FROM anexo_checklist_devolucion_semilla WHERE id_cl_devolucion_semilla = :id ;")
    ChecklistDevolucionSemilla getClDevolucionSemillaById( int id );

    @Query("SELECT * FROM anexo_checklist_devolucion_semilla WHERE clave_unica = :clave_unica ")
    ChecklistDevolucionSemilla getCLDevolucionSemillaByClaveUnica( String clave_unica );

    @Query("SELECT * FROM anexo_checklist_devolucion_semilla WHERE id_cl_devolucion_semilla = :id AND estado_sincronizacion = :estado_sinc ;")
    ChecklistDevolucionSemilla getClDevolucionSemillaById( int id , int estado_sinc);

}
