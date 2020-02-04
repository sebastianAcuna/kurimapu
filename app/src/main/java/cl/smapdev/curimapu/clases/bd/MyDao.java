package cl.smapdev.curimapu.clases.bd;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.Agricultor;
import cl.smapdev.curimapu.clases.Comuna;
import cl.smapdev.curimapu.clases.Fichas;
import cl.smapdev.curimapu.clases.Fotos;
import cl.smapdev.curimapu.clases.Region;
import cl.smapdev.curimapu.clases.relaciones.AgricultorCompleto;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;

@Dao
public interface MyDao {


    /* FOTOS */
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




    /* FICHAS */


    @Insert
    long insertFicha(Fichas fichas);

    @Update
    int updateFicha(Fichas fichas);

    @Query("SELECT " +
            "fichas.id_ficha, " +
            "fichas.anno, " +
            "fichas.rut_agricultor_fichas, " +
            "fichas.oferta_negocio, " +
            "fichas.localidad," +
            "fichas.has_disponible," +
            "fichas.observaciones," +
            "fichas.norting," +
            "fichas.easting," +
            "fichas.activa," +
            "fichas.subida," +
            "agricultor.nombre_agricultor," +
            "agricultor.telefono_agricultor," +
            "agricultor.administrador_agricultor," +
            "agricultor.telefono_admin_agricultor," +
            "agricultor.region_agricultor," +
            "agricultor.comuna_agricultor," +
            "agricultor.agricultor_subido," +
            "agricultor.rut_agricultor," +
            "region.id_region," +
            "region.desc_region," +
            "comuna.id_comuna," +
            "comuna.desc_comuna," +
            "comuna.id_region_comuna FROM fichas " +
            "INNER JOIN agricultor ON (agricultor.rut_agricultor = fichas.rut_agricultor_fichas) " +
            "INNER JOIN region ON (region.id_region = agricultor.region_agricultor)" +
            "INNER JOIN comuna ON (comuna.id_comuna = agricultor.comuna_agricultor)" +
            "WHERE anno = :year ")
    List<FichasCompletas> getFichasByYear(int year);




    @Insert
    void insertAgricultor(Agricultor agricultor);

    @Query("SELECT  * FROM agricultor")
    List<Agricultor> getAgricultores();


    @Query("SELECT  * FROM agricultor WHERE rut_agricultor = :rut")
    AgricultorCompleto getAgricultorByRut(String rut);




    @Insert
    void insertRegiones(List<Region> regiones);

    @Insert
    void insertRegion(Region regiones);

    @Query("SELECT * FROM region ")
    List<Region> getRegiones();




    @Insert
    void insertComunas(List<Comuna> comunas);

    @Insert
    void insertComuna(Comuna comunas);

    @Query("SELECT * FROM comuna ")
    List<Comuna> getComunas();


    @Query("SELECT * FROM comuna WHERE id_region_comuna = :region")
    List<Comuna> getComunaByRegion(int region);


}
