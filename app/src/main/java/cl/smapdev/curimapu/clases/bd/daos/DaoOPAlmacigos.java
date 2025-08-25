package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.OpAlmacigos;

@Dao
public interface DaoOPAlmacigos {

    @Query("DELETE FROM op_almacigos ;")
    void limpiarOPAlmacigo();

    @Insert()
    void insertarOpAlmacigos(List<OpAlmacigos> almacigos);

    @Insert()
    void insertarOpAlmacigo(OpAlmacigos almacigos);

    @Query("SELECT * FROM op_almacigos WHERE id_v_post_siembra = :id ")
    OpAlmacigos getOpAlmacigoById(int id);

    @Query("SELECT * FROM op_almacigos ORDER BY fecha_siembra ASC ")
    List<OpAlmacigos> getOpAlmacigos();

}
