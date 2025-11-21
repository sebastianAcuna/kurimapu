package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.VisitaAlmacigoCompleto;
import cl.smapdev.curimapu.clases.tablas.FotosAlmacigos;
import cl.smapdev.curimapu.clases.tablas.VisitasAlmacigos;

@Dao
public interface VisitasFotosAlmacigos {


    @Query("DELETE FROM almacigos_visita WHERE estado_sincronizacion = 1; ")
    void limpiarVisitasSubidas();

    @Insert()
    void insertarVisitasAlmacigos(List<VisitasAlmacigos> visitasAlmacigosList);

    @Insert()
    void insertarVisitasAlmacigos(VisitasAlmacigos visitasAlmacigosList);

    @Query("SELECT * FROM almacigos_visita WHERE estado_sincronizacion = 0 ")
    List<VisitasAlmacigos> getVisitasAlmacigosPorSync();

    @Query("SELECT* FROM almacigos_visita  ")
    List<VisitasAlmacigos> getVisitas();

    @Query("SELECT* FROM almacigos_visita  WHERE uid_visita = :uidVisita ")
    VisitasAlmacigos getVisitaByUid(String uidVisita);

    @Query("SELECT * FROM almacigos_visita INNER JOIN op_almacigos ON (op_almacigos.id_v_post_siembra = almacigos_visita.id_valor_post_siembra) WHERE id_valor_post_siembra = :id_op ")
    List<VisitaAlmacigoCompleto> getVisitasAlmacigosPoridOP(int id_op);

    @Query("UPDATE almacigos_visita SET estado_sincronizacion = 1 WHERE estado_sincronizacion = 0 ")
    void marcarVisitasSincronizadadas();


    @Query("DELETE FROM almacigos_fotos_visita WHERE estado_sincronizacion = 1")
    void limpiarFotosSubidas();


    @Insert()
    void insertarFotoAlmacigos(FotosAlmacigos fotosAlmacigos);


    @Query("SELECT * FROM almacigos_fotos_visita WHERE uid_visita = :uid ")
    List<FotosAlmacigos> getFotosAlmacigosPorUidVisita(String uid);


    @Query("UPDATE almacigos_fotos_visita SET estado_sincronizacion = '1' WHERE estado_sincronizacion = '0' AND uid_visita = :uid ")
    void marcarFotosSincronizadadasByUid(String uid);

    @Query("UPDATE almacigos_fotos_visita SET estado_sincronizacion = '1' WHERE estado_sincronizacion = '0'")
    void marcarFotosSincronizadadas();

    @Query("SELECT * FROM almacigos_fotos_visita WHERE estado_sincronizacion = '0'")
    List<FotosAlmacigos> getFotosAlmacigosPorSync();


}
