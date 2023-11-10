package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;

@Dao
public interface DaoEstacionFloracion {


//    sincronizacion

    @Query("SELECT * FROM estacion_floracion WHERE estado_sincronizacion = 0 AND estado_documento = 1 ")
    List<EstacionFloracion> getEstacionesToSync();

    @Query("SELECT * FROM estacion_floracion WHERE id_ac_floracion = :id_ac ")
    List<EstacionFloracion> getEstacionesByAc(int id_ac);


    @Query("SELECT * FROM estacion_floracion_estaciones WHERE clave_unica_floracion_cabecera = :clave_unica ")
        List<EstacionFloracionEstaciones> getEstacionesByPadre(String clave_unica);


    @Query("SELECT * FROM estacion_floracion_detalle WHERE clave_unica_floracion = :clave_unica ")
    List<EstacionFloracionDetalle> getDetalleByClavePadre(String clave_unica);


    @Query("SELECT * FROM estacion_floracion_detalle WHERE clave_unica_floracion_estacion = :clave_unica ")
    List<EstacionFloracionDetalle> getDetalleByClaveEstacion(String clave_unica);



    @Query("UPDATE estacion_floracion_detalle SET clave_unica_floracion_estacion = :clave_unica WHERE clave_unica_floracion_estacion = 0  ")
    void updateDetalleClaveUnicaEstacion(String clave_unica);



    @Query("DELETE FROM estacion_floracion_detalle WHERE clave_unica_floracion_estacion = 0 ")
    void deleteDetalleSuelto();

    @Query("DELETE FROM estacion_floracion_estaciones WHERE clave_unica_floracion_cabecera = 0 ")
    void deleteEstacionesSueltas();

    @Query("UPDATE estacion_floracion_estaciones SET clave_unica_floracion_cabecera = :clave_unica WHERE clave_unica_floracion_cabecera = 0; ")
    void updateClaveCabeceraEstaciones(String clave_unica);

    @Query("UPDATE estacion_floracion_detalle SET clave_unica_floracion = :clave_unica WHERE clave_unica_floracion = 0 OR clave_unica_floracion IS NULL ; ")
    void updateClaveCabeceraDetalle(String clave_unica);

    @Query("UPDATE estacion_floracion SET estado_sincronizacion = 0 WHERE clave_unica_floracion = :clave_unica; ")
    void updateCabeceraSinc(String clave_unica);


    @Delete
    void deleteDetalles(List<EstacionFloracionDetalle> detalles);

    @Delete
    void deleteDetalle(EstacionFloracionDetalle detalle);

    @Delete
    void deleteEstacion(EstacionFloracionEstaciones estacion);

    @Insert
    void insertEstacionDetalle(EstacionFloracionDetalle detalle);

    @Update
    void updateEstacionDetalle(EstacionFloracionDetalle detalle);


    @Insert
    void insertEstacion(EstacionFloracionEstaciones estacion);

    @Update
    void updateEstacion(EstacionFloracionEstaciones estacion);

    @Insert
    void insertEstacionCabecera(EstacionFloracion floracion);

    @Update
    void updateEstacionCabecera(EstacionFloracion floracion);
}
