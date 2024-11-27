package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutos;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosFotos;

@Dao
public interface DaoCheckListRevisionFrutos {

    @Query("SELECT * FROM checklist_revision_frutos WHERE id_ac_cl_revision_frutos = :id_ac;")
    List<CheckListRevisionFrutos> getAllClrevisionFrutosByAc(int id_ac);

    @Query("SELECT * FROM checklist_revision_frutos WHERE id_cl_revision_frutos = :id;")
    CheckListRevisionFrutos getclrevisionFrutosById(int id);

    @Query("SELECT * FROM checklist_revision_frutos WHERE clave_unica = :claveUnica;")
    CheckListRevisionFrutos getClrevisionFrutosByClaveUnica(String claveUnica);

    @Query("SELECT * FROM checklist_revision_frutos WHERE id_cl_revision_frutos = :id AND estado_sincronizacion = :estado ;")
    CheckListRevisionFrutos getClrevisionFrutosByIdAndEstado(int id, int estado);

    @Query("SELECT * FROM checklist_revision_frutos WHERE estado_sincronizacion = 0")
    List<CheckListRevisionFrutos> getClrevisionFrutosToSync();


    @Query("DELETE FROM checklist_revision_frutos;")
    void deleteAllRevisionFrutos();

    @Update
    void updateclrevisionFrutos(CheckListRevisionFrutos chk);


    @Insert
    long insertclrevisionFrutos(CheckListRevisionFrutos chk);

    @Insert
    List<Long> insertclrevisionFrutos(List<CheckListRevisionFrutos> chks);


    //    DETALLE
    @Delete
    void deleteDetalleRevisionFrutos(CheckListRevisionFrutosDetalle chk);

    @Query("DELETE FROM checklist_revision_frutos_detalle WHERE clave_unica_revision_frutos IS NULL")
    void deleteDetalleRevisionFrutosSinPadre();

    @Update
    void updateclrevisionFrutosDetalle(CheckListRevisionFrutosDetalle chk);

    @Insert
    void insertDetallesRevFrutos(CheckListRevisionFrutosDetalle detalle);

    @Query("SELECT * FROM checklist_revision_frutos_detalle WHERE clave_unica_detalle = :claveUnica;")
    CheckListRevisionFrutosDetalle obtenerDetallesPorClaveUnica(String claveUnica);

    @Query("SELECT * FROM checklist_revision_frutos_detalle WHERE clave_unica_revision_frutos = :claveUnica OR clave_unica_revision_frutos IS NULL;")
    List<CheckListRevisionFrutosDetalle> obtenerDetallesPorClaveUnicaPadreOrNull(String claveUnica);


    @Query("SELECT * FROM checklist_revision_frutos_detalle WHERE clave_unica_revision_frutos = :claveUnica AND estado_sincronizacion = 0")
    List<CheckListRevisionFrutosDetalle> obtenerDetallesPorClaveUnicaPadreToSynk(String claveUnica);


    //    FOTOS

    @Delete
    void deleteFotosRevisionFrutos(CheckListRevisionFrutosFotos fotos);

    @Query("DELETE FROM checklist_revision_frutos_fotos WHERE clave_unica_revision_frutos IS NULL; ")
    void deleteFotosRevisionFrutosSinPadre();

    @Query("SELECT * FROM checklist_revision_frutos_fotos WHERE clave_unica_revision_frutos = :claveUnica OR clave_unica_revision_frutos IS NULL;")
    List<CheckListRevisionFrutosFotos> obtenerFotosPorClaveUnicaPadreOrNull(String claveUnica);

    @Insert
    void insertFotosRevFrutos(CheckListRevisionFrutosFotos fotos);

    @Update
    void updateclrevisionFrutosFotos(CheckListRevisionFrutosFotos chk);

    @Query("SELECT * FROM checklist_revision_frutos_fotos WHERE clave_unica_revision_frutos = :claveUnica AND estado_sincronizacion = 0")
    List<CheckListRevisionFrutosFotos> obtenerFotosPorClaveUnicaPadreToSynk(String claveUnica);

    @Query("SELECT * FROM checklist_revision_frutos_fotos WHERE clave_unica_foto = :claveUnica ")
    CheckListRevisionFrutosFotos obtenerFotosPorClaveUnica(String claveUnica);

}
