package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListRoguing;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoCabecera;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoDetalle;

@Dao
public interface DaoCheckListRoguing {

//    CABECERA

    @Query("SELECT * FROM checklist_roguing WHERE id_ac_cl_roguing = :id_ac;")
    List<CheckListRoguing> getAllClroguingByAc(int id_ac);

    @Query("SELECT * FROM checklist_roguing WHERE id_cl_roguing = :id;")
    CheckListRoguing getclroguingById(int id);

    @Query("SELECT * FROM checklist_roguing WHERE clave_unica = :claveUnica;")
    CheckListRoguing getClroguingByClaveUnica(String claveUnica);

    @Query("SELECT * FROM checklist_roguing WHERE id_cl_roguing = :id AND estado_sincronizacion = :estado ;")
    CheckListRoguing getClroguingByIdAndEstado(int id, int estado);

    @Query("SELECT * FROM checklist_roguing WHERE estado_sincronizacion = 0")
    List<CheckListRoguing> getClroguingToSync();

    @Query("DELETE FROM checklist_roguing;")
    void deleteAllRoguing();

    @Update
    void updateclroguing(CheckListRoguing chk);

    @Insert
    long insertclroguing(CheckListRoguing chk);

    @Insert
    List<Long> insertclroguing(List<CheckListRoguing> chks);


    //FOTOS CABECERA

    @Query("SELECT * FROM checklist_roguing_foto_cabecera WHERE clave_unica = :claveUnica; ")
    CheckListRoguingFotoCabecera obtenerRoguingFotoCabPorClaveUnidad(String claveUnica);

    @Query("UPDATE checklist_roguing_foto_cabecera SET clave_unica_roguing = :claveUnica WHERE clave_unica_roguing = :claveUnica OR clave_unica_roguing IS NULL")
    void updateFotoCabRoguingClaveUnicaPadreFinal(String claveUnica);

    @Query("DELETE FROM checklist_roguing_foto_cabecera WHERE clave_unica IS NULL; ")
    void deleteFotosRoguingSinPadre();

    @Query("DELETE FROM checklist_roguing_foto_cabecera WHERE clave_unica_roguing IS NULL; ")
    void deleteFotosRoguingSinPadres();

    @Delete
    void deleteFotoRoguingCabeceraPorId(CheckListRoguingFotoCabecera foto);

    @Update
    void updateFotosRoguing(CheckListRoguingFotoCabecera fotos);

    @Insert
    void insertFotosRoguing(CheckListRoguingFotoCabecera fotos);

    @Query("SELECT * FROM checklist_roguing_foto_cabecera WHERE clave_unica IS NULL;")
    List<CheckListRoguingFotoCabecera> obtenerFotosSinPadre();

    @Query("SELECT * FROM checklist_roguing_foto_cabecera WHERE ( clave_unica_roguing IS NULL OR clave_unica_roguing = :claveUnica ) AND tipo_foto = 'H';")
    List<CheckListRoguingFotoCabecera> obtenerFotosSinPadreHembra(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_foto_cabecera WHERE ( clave_unica_roguing IS NULL OR clave_unica_roguing = :claveUnica ) AND tipo_foto = 'M';")
    List<CheckListRoguingFotoCabecera> obtenerFotosSinPadreMacho(String claveUnica);

    @Update
    void updateFotosCabeceraRoguing(List<CheckListRoguingFotoCabecera> fotos);

    @Query("SELECT * FROM checklist_roguing_foto_cabecera WHERE clave_unica_roguing = :claveUnica;")
    List<CheckListRoguingFotoCabecera> obtenerDetalleRoguingFotoCabPorClaveUnicaPadre(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_foto_cabecera WHERE clave_unica_roguing = :claveUnica AND estado_sincronizacion = 0;")
    List<CheckListRoguingFotoCabecera> obtenerDetalleRoguingFotoCabPorClaveUnicaPadreToSynk(String claveUnica);


    //    DETALLE FECHAS

    @Query("SELECT * FROM checklist_roguing_detalle_fechas WHERE clave_unica_detalle_fecha = :claveUnica; ")
    CheckListRoguingDetalleFechas obtenerRoguingDetalleFechaPorClaveUnidad(String claveUnica);

    @Query("UPDATE checklist_roguing_detalle_fechas SET clave_unica_roguing = :claveUnica WHERE clave_unica_roguing = :claveUnica OR clave_unica_roguing IS NULL")
    void updateDetalleFechaRoguingClaveUnicaPadreFinal(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_detalle_fechas WHERE clave_unica_roguing IS NULL OR clave_unica_roguing = :claveUnica ;")
    List<CheckListRoguingDetalleFechas> obtenerDetalleFechaRoguingPorClaveUnicaPadre(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_detalle_fechas WHERE clave_unica_roguing = :claveUnica ;")
    List<CheckListRoguingDetalleFechas> obtenerDetalleFechaRoguingPorClaveUnicaPadreFinal(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_detalle_fechas WHERE clave_unica_roguing = :claveUnica AND estado_sincronizacion = 0 ;")
    List<CheckListRoguingDetalleFechas> obtenerDetalleFechaRoguingPorClaveUnicaPadreFinalToSynk(String claveUnica);

    @Query("DELETE FROM checklist_roguing_detalle_fechas WHERE clave_unica_roguing IS NULL ;")
    void deleteDetalleFechaSinPadreFinal();

    @Insert
    void insertRoguingDetalleFecha(CheckListRoguingDetalleFechas roguingDetalleFechas);

    @Update
    void updateRoguingDetalleFecha(CheckListRoguingDetalleFechas roguingDetalleFechas);

    @Delete
    void deleteRoguingDetalleFecha(CheckListRoguingDetalleFechas roguingDetalleFechas);


    //detalle roguing
    @Delete
    void deleteRoguingDetalle(CheckListRoguingDetalle detalle);

    @Query("DELETE FROM checklist_roguing_detalle WHERE clave_unica_detalle IS NULL; ")
    void deleteRoguingDetalleSinPadre();

    @Query("DELETE FROM checklist_roguing_detalle WHERE clave_unica_detalle_fecha IS NULL; ")
    void deleteRoguingDetalleSinPadres();

    @Query("DELETE FROM checklist_roguing_detalle WHERE clave_unica_roguing IS NULL; ")
    void deleteRoguingDetalleSinPadreFinal();

    @Query("SELECT * FROM checklist_roguing_detalle WHERE clave_unica_detalle = :claveUnica; ")
    CheckListRoguingDetalle obtenerRoguingDetallePorClaveUnidad(String claveUnica);

    @Insert
    void insertDetallesRoguing(CheckListRoguingDetalle detalle);

    @Update
    void updateDetalleRoguing(CheckListRoguingDetalle detalle);

    @Query("UPDATE checklist_roguing_detalle SET clave_unica_roguing = :claveUnica WHERE clave_unica_roguing = :claveUnica OR clave_unica_roguing IS NULL")
    void updateDetalleRoguingClaveUnicaPadreFinal(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_detalle WHERE clave_unica_detalle IS NULL ORDER BY genero, descripcion_fuera_tipo ASC;")
    List<CheckListRoguingDetalle> obtenerDetalleSinPadre();

    @Query("SELECT * FROM checklist_roguing_detalle WHERE clave_unica_detalle_fecha IS NULL ORDER BY genero, descripcion_fuera_tipo ASC;")
    List<CheckListRoguingDetalle> obtenerDetalleSinPadreFecha();

    @Query("SELECT * FROM checklist_roguing_detalle WHERE clave_unica_detalle_fecha IS NULL OR clave_unica_detalle_fecha = :claveUnica ORDER BY genero, descripcion_fuera_tipo ASC;")
    List<CheckListRoguingDetalle> obtenerDetalleSinPadreFechaoPadre(String claveUnica);


    @Query("SELECT * FROM checklist_roguing_detalle WHERE clave_unica_roguing = :claveUnica ORDER BY genero, descripcion_fuera_tipo ASC;")
    List<CheckListRoguingDetalle> obtenerDetalleRoguingPorClaveUnicaPadre(String claveUnica);


    @Query("SELECT * FROM checklist_roguing_detalle WHERE clave_unica_roguing = :claveUnica AND estado_sincronizacion = 0 ORDER BY genero, descripcion_fuera_tipo ASC;")
    List<CheckListRoguingDetalle> obtenerDetalleRoguingPorClaveUnicaPadreToSynk(String claveUnica);

    //DETALLE
    @Query("SELECT * FROM checklist_roguing_detalle WHERE clave_unica_detalle_fecha  = :claveUnica ORDER BY genero, descripcion_fuera_tipo ASC")
    List<CheckListRoguingDetalle> obtenerDetallePorPadreFecha(String claveUnica);

    @Query(" SELECT * FROM checklist_roguing_detalle " +
            " LEFT JOIN checklist_roguing_detalle_fechas ON (checklist_roguing_detalle_fechas.clave_unica_detalle_fecha = checklist_roguing_detalle.clave_unica_detalle_fecha ) " +
            " WHERE  (checklist_roguing_detalle.clave_unica_roguing = :claveUnica OR checklist_roguing_detalle.clave_unica_roguing IS NULL) ORDER BY checklist_roguing_detalle_fechas.fecha, genero, descripcion_fuera_tipo ASC ;")
    List<CheckListRoguingDetalle> obtenerDetalleRoguingPorClaveUnicaPadreFechas(String claveUnica);


    @Query(" SELECT checklist_roguing_detalle.id_cl_roguing_detalle, checklist_roguing_detalle.id_user_tx,  checklist_roguing_detalle.estado_sincronizacion, SUM(cantidad) AS cantidad, checklist_roguing_detalle.descripcion_fuera_tipo, checklist_roguing_detalle.genero FROM checklist_roguing_detalle " +
            " WHERE  (checklist_roguing_detalle.clave_unica_roguing = :claveUnica OR checklist_roguing_detalle.clave_unica_roguing IS NULL) " +
            " GROUP BY descripcion_fuera_tipo, genero  ORDER BY genero, descripcion_fuera_tipo ASC ;")
    List<CheckListRoguingDetalle> obtenerResumenFueraTipo(String claveUnica);


    //   FOTO DETALLE

    @Query("SELECT * FROM checklist_roguing_foto_detalle WHERE clave_unica = :claveUnica; ")
    CheckListRoguingFotoDetalle obtenerRoguingFotoDetPorClaveUnidad(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_foto_detalle WHERE clave_unica_roguing = :claveUnica;")
    List<CheckListRoguingFotoDetalle> obtenerDetalleRoguingFotoDetPorClaveUnicaPadre(String claveUnica);

    @Query("SELECT * FROM checklist_roguing_foto_detalle WHERE clave_unica_roguing = :claveUnica AND estado_sincronizacion = 0;")
    List<CheckListRoguingFotoDetalle> obtenerDetalleRoguingFotoDetPorClaveUnicaPadreToSynk(String claveUnica);


    @Query("DELETE FROM checklist_roguing_foto_detalle WHERE clave_unica IS NULL")
    void deleteFotosRoguingDetalleSinPadre();

    @Query("DELETE FROM checklist_roguing_foto_detalle WHERE clave_unica_roguing IS NULL")
    void deleteFotosRoguingDetalleSinPadreFinal();


    @Query("DELETE FROM checklist_roguing_foto_detalle WHERE clave_unica_detalle IS NULL")
    void deleteFotosRoguingDetalleSinPadres();

    @Delete
    void deleteFotoRoguingDetalle(CheckListRoguingFotoDetalle foto);


    @Insert
    void insertRoguingFotoDetalle(CheckListRoguingFotoDetalle detalle);

    @Update
    void updateRoguingFotoDetalle(CheckListRoguingFotoDetalle detalle);

    @Query("SELECT * FROM checklist_roguing_foto_detalle WHERE clave_unica_detalle IS NULL;")
    List<CheckListRoguingFotoDetalle> obtenerFotosDetalleSinPadre();

    @Query("SELECT * FROM checklist_roguing_foto_detalle WHERE clave_unica_detalle IS NULL OR clave_unica_detalle = :claveUnica;")
    List<CheckListRoguingFotoDetalle> obtenerFotosDetalleSinPadreoPadre(String claveUnica);

    @Query("UPDATE checklist_roguing_foto_detalle SET clave_unica_roguing = :claveUnica WHERE clave_unica_roguing = :claveUnica OR clave_unica_roguing IS NULL")
    void updateFotoDetRoguingClaveUnicaPadreFinal(String claveUnica);

}


