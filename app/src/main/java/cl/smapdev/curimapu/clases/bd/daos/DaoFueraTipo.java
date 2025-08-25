package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.FueraTipoCategoria;
import cl.smapdev.curimapu.clases.tablas.FueraTipoSubCategoria;

@Dao
public interface DaoFueraTipo {

    @Insert
    void insertarCategorias(List<FueraTipoCategoria> categorias);

    @Insert
    void insertarSubCategorias(List<FueraTipoSubCategoria> subCategorias);

    @Query("DELETE FROM fuera_tipo_categoria;")
    void limpiarCategorias();

    @Query("DELETE FROM fuera_tipo_sub_categoria;")
    void limpiarSubCategorias();


    @Query("SELECT * FROM fuera_tipo_categoria ORDER BY descripcion ASC; ")
    List<FueraTipoCategoria> obtenerCategorias();

    @Query("SELECT * FROM fuera_tipo_categoria WHERE id_fuera_tipo_cat = :id ; ")
    FueraTipoCategoria obtenerCategoria(int id);

    @Query("SELECT * FROM fuera_tipo_sub_categoria WHERE id_fuera_tipo_sub_cat = :id; ")
    FueraTipoSubCategoria obtenerSubCategoriaPorId(int id);


    @Query("SELECT * FROM fuera_tipo_sub_categoria WHERE id_fuera_tipo_cat = :id ORDER BY descripcion_sub_cat ASC; ")
    List<FueraTipoSubCategoria> obtenerSubCategoriaPorIdCat(int id);
}
