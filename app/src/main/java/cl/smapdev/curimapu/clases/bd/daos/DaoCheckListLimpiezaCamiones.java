package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;

@Dao
public interface DaoCheckListLimpiezaCamiones {


    @Query(" SELECT * FROM anexo_checklist_limpieza_camiones WHERE id_ac_cl_limpieza_camiones = :id_ac ")
    List<CheckListLimpiezaCamiones> getAllClLimpiezaCamionesByAc(int id_ac );

    @Query(" SELECT * FROM anexo_checklist_limpieza_camiones WHERE id_cl_limpieza_camiones = :id_cap ")
    CheckListLimpiezaCamiones getClLimpiezaCamionesByAc(int id_cap);


    @Query(" SELECT * FROM anexo_checklist_limpieza_camiones WHERE clave_unica = :clave_unica ")
    CheckListLimpiezaCamiones getClLimpiezaCamionesByClaveUnica(String clave_unica);

    @Query(" SELECT * FROM anexo_checklist_limpieza_camiones WHERE id_cl_limpieza_camiones = :id_cap " +
            " AND estado_sincronizacion = :estado_sinc ")
    CheckListLimpiezaCamiones getClLimpiezaCamionesByAcAndEstado(int id_cap, int estado_sinc);

    @Query(" SELECT * FROM anexo_checklist_limpieza_camiones WHERE " +
            " estado_sincronizacion = :estado_sinc ")
    List<CheckListLimpiezaCamiones> getClLimpiezaCamionesByEstado(int estado_sinc);

    @Query("SELECT * FROM anexo_checklist_limpieza_camiones_detalle " +
            "WHERE ( clave_unica_cl_limpieza_camiones = :clave_unica OR clave_unica_cl_limpieza_camiones = 0)")
    List<ChecklistLimpiezaCamionesDetalle> getLimpiezaCamionesDetallesByPadre(String clave_unica);


    @Query("SELECT * FROM anexo_checklist_limpieza_camiones_detalle " +
            "WHERE clave_unica_cl_limpieza_camiones_detalle = :clave_unica ")
    ChecklistLimpiezaCamionesDetalle getLimpiezaCamionesDetallesByClaveUnica(String clave_unica);


    @Query("DELETE FROM anexo_checklist_limpieza_camiones_detalle " +
            " WHERE clave_unica_cl_limpieza_camiones = '0' ")
    void deleteDetalles();


    @Query("UPDATE anexo_checklist_limpieza_camiones_detalle " +
            "SET clave_unica_cl_limpieza_camiones = :clave_unica  " +
            "WHERE clave_unica_cl_limpieza_camiones = 0 ")
    void updateLimpiezaCamionesDetalleConCero(String clave_unica);

    @Update
    void updateDetalle(ChecklistLimpiezaCamionesDetalle detalle);


    @Insert
    void insertLimpiezaCamionesDetalle(ChecklistLimpiezaCamionesDetalle detalle);


    @Insert
    void insertLimpiezaCamiones(CheckListLimpiezaCamiones cabecera);

    @Update
    void updateLimpiezaCamiones(CheckListLimpiezaCamiones cabecera);

    @Delete
    void deleteDetalle(ChecklistLimpiezaCamionesDetalle detalle);

}
