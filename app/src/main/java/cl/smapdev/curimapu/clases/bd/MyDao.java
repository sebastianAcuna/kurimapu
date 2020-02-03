package cl.smapdev.curimapu.clases.bd;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.Fotos;

@Dao
public interface MyDao {


    @Insert
    long insertFotos(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field ")
    List<Fotos> getFotosByField(int field);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND vista = :view")
    List<Fotos> getFotosByFieldAndView(int field, int view);

    @Query("SELECT  COUNT(id_foto) FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha = :ficha")
    int getCantAgroByFieldViewAndFicha(int field, int view, int ficha);

    @Update
    void updateFavorita(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE id_foto = :idFoto")
    Fotos getFotosById(int idFoto);

    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND favorita = 1")
    int getCantFavoritasByFieldbookAndFicha(int fieldbook, int idFicha);

    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND vista = :view AND favorita = 1")
    int getCantFavoritasByFieldbookFichaAndVista(int fieldbook, int idFicha, int view);



}
