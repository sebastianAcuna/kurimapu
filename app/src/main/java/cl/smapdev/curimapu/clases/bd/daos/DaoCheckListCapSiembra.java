package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;

@Dao
public interface DaoCheckListCapSiembra {


    @Query(" SELECT * FROM anexo_checklist_capacitacion_siembra WHERE id_ac_cl_cap_siembra = :id_ac ")
    List<CheckListCapacitacionSiembra> getAllClCapSiembraByAc(int id_ac);

    @Query(" SELECT * FROM anexo_checklist_capacitacion_siembra WHERE id_cl_cap_siembra = :id_cap ")
    CheckListCapacitacionSiembra getClCapSiembraByAc(int id_cap);


    @Query(" SELECT * FROM anexo_checklist_capacitacion_siembra WHERE clave_unica = :clave_unica ")
    CheckListCapacitacionSiembra getClCapSiembraByClaveUnica(String clave_unica);

    @Query(" SELECT * FROM anexo_checklist_capacitacion_siembra WHERE id_cl_cap_siembra = :id_cap " +
            " AND estado_sincronizacion = :estado_sinc ")
    CheckListCapacitacionSiembra getClCapSiembraByAcAndEstado(int id_cap, int estado_sinc);

    @Query(" SELECT * FROM anexo_checklist_capacitacion_siembra WHERE " +
            " estado_sincronizacion = :estado_sinc ")
    List<CheckListCapacitacionSiembra> getClCapSiembraByEstado(int estado_sinc);

    @Query("SELECT * FROM anexo_checklist_capacitacion_siembra_detalle " +
            "WHERE clave_unica_cl_cap_siembra = :clave_unica OR clave_unica_cl_cap_siembra = 0 ")
    List<CheckListCapacitacionSiembraDetalle> getCapSiembraDetallesByPadre(String clave_unica);


    @Query("SELECT * FROM anexo_checklist_capacitacion_siembra_detalle " +
            "WHERE clave_unica_cl_cap_siembra_detalle = :clave_unica ")
    CheckListCapacitacionSiembraDetalle getCapSiembraDetallesByClaveUnica(String clave_unica);


    @Query("DELETE FROM anexo_checklist_capacitacion_siembra_detalle " +
            " WHERE clave_unica_cl_cap_siembra = '0' ")
    void deleteDetalles();


    @Query("UPDATE anexo_checklist_capacitacion_siembra_detalle " +
            "SET clave_unica_cl_cap_siembra = :clave_unica  " +
            "WHERE clave_unica_cl_cap_siembra = 0 ")
    void updateCapacitacionSiembraDetalleConCero(String clave_unica);

    @Update
    void updateDetalle(CheckListCapacitacionSiembraDetalle detalle);


    @Insert
    void insertCapacitacionSiembraDetalle(CheckListCapacitacionSiembraDetalle detalle);


    @Insert
    void insertCapacitacionSiembra(CheckListCapacitacionSiembra cabecera);

    @Update
    void updateCapacitacionSiembra(CheckListCapacitacionSiembra cabecera);

    @Delete
    void deleteDetalle(CheckListCapacitacionSiembraDetalle detalle);
}
