package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.AnexoVilab;

@Dao
public interface DaoVilab {

    @Query("SELECT * FROM anexo_vilab WHERE id_ac = :id_ac")
    AnexoVilab getVilabByAc(int id_ac);

    @Query("SELECT * FROM anexo_vilab")
    List<AnexoVilab> getVilab();

    @Query("DELETE FROM anexo_vilab")
    void clearVilabTable();

    @Delete
    void deleteVilab(AnexoVilab vilab);

    @Insert
    void insertVilab(AnexoVilab vilab);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVilab(List<AnexoVilab> vilabs);
}
