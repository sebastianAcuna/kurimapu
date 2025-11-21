package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantinera;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalleFotos;

@Dao
public interface DaoCheckListRecepcionPlantineras {

    //    cabecera
    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras WHERE id_ac_recepcion_plantinera = :id_ac;")
    List<CheckListRecepcionPlantinera> getAllClRPByAc(int id_ac);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras WHERE id_cl_recepcion_plantinera = :id;")
    CheckListRecepcionPlantinera getclRPgById(int id);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras WHERE clave_unica = :claveUnica;")
    CheckListRecepcionPlantinera getClRPByClaveUnica(String claveUnica);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras WHERE estado_sincronizacion = 0")
    List<CheckListRecepcionPlantinera> getClRPToSync();

    @Update
    void updateclRP(CheckListRecepcionPlantinera chk);

    @Insert
    long insertclRP(CheckListRecepcionPlantinera chk);


    //    detalle
    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras_detalle WHERE clave_unica_rp_detalle = :claveUnica; ")
    CheckListRecepcionPlantineraDetalle obtenerRPDetallePorClaveUnica(String claveUnica);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras_detalle WHERE clave_unica_rp_cabecera = :claveUnica; ")
    List<CheckListRecepcionPlantineraDetalle> obtenerRPDetallePorClaveCabecera(String claveUnica);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras_detalle WHERE clave_unica_rp_cabecera = :claveUnica OR clave_unica_rp_cabecera IS NULL; ")
    List<CheckListRecepcionPlantineraDetalle> obtenerRPDetallePorClaveCabeceraONula(String claveUnica);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras_detalle WHERE clave_unica_rp_cabecera = :claveUnica AND estado_sincronizacion = 0; ")
    List<CheckListRecepcionPlantineraDetalle> obtenerRPDetallePorClaveCabeceraToSynk(String claveUnica);

    @Query("DELETE FROM anexo_checklist_recepcion_plantineras_detalle WHERE clave_unica_rp_cabecera IS NULL")
    void eliminarDetalleSinClaveUnica();

    @Query("UPDATE anexo_checklist_recepcion_plantineras_detalle SET clave_unica_rp_cabecera = :claveNueva WHERE clave_unica_rp_cabecera IS NULL")
    void actualizarClaveForaneaDetalle(String claveNueva);

    @Delete
    void eliminarDetalle(CheckListRecepcionPlantineraDetalle detalle);

    @Insert
    void insertDetalle(CheckListRecepcionPlantineraDetalle detalle);

    @Update
    void updateDetalle(CheckListRecepcionPlantineraDetalle detalle);


    // fotos
    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras_detalle_fotos WHERE clave_unica_rp_cabecera = :claveUnica AND estado_sincronizacion = 0; ")
    List<CheckListRecepcionPlantineraDetalleFotos> obtenerRPDetalleFotoPorClaveCabeceraToSynk(String claveUnica);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras_detalle_fotos WHERE clave_unica_foto = :claveUnica; ")
    CheckListRecepcionPlantineraDetalleFotos obtenerRPDetalleFotoPorClaveUnica(String claveUnica);

    @Query("DELETE FROM anexo_checklist_recepcion_plantineras_detalle_fotos WHERE clave_unica_rp_cabecera IS NULL ")
    void eliminarFotosSinClaveUnica();

    @Insert
    void insertFoto(CheckListRecepcionPlantineraDetalleFotos foto);

    @Update
    void updateFoto(CheckListRecepcionPlantineraDetalleFotos foto);

    @Query("SELECT * FROM anexo_checklist_recepcion_plantineras_detalle_fotos WHERE clave_unica_rp_detalle = :claveUnica OR clave_unica_rp_detalle IS NULL; ")
    List<CheckListRecepcionPlantineraDetalleFotos> contarFotosPorClaveCabeceraOVacias(String claveUnica);

    @Delete
    void deleteFoto(CheckListRecepcionPlantineraDetalleFotos foto);

    @Query("UPDATE  anexo_checklist_recepcion_plantineras_detalle_fotos SET clave_unica_rp_cabecera = :claveNueva WHERE clave_unica_rp_cabecera IS NULL")
    void actualizarClaveForaneaFotos(String claveNueva);
}
