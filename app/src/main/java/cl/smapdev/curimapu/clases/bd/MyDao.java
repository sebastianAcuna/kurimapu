package cl.smapdev.curimapu.clases.bd;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import cl.smapdev.curimapu.Variedad;
import cl.smapdev.curimapu.clases.Agricultor;
import cl.smapdev.curimapu.clases.AnexoContrato;
import cl.smapdev.curimapu.clases.Comuna;
import cl.smapdev.curimapu.clases.Especie;
import cl.smapdev.curimapu.clases.Fichas;
import cl.smapdev.curimapu.clases.Fotos;
import cl.smapdev.curimapu.clases.Region;
import cl.smapdev.curimapu.clases.relaciones.AgricultorCompleto;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
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



    @RawQuery
    List<FichasCompletas> getFichasFilter(SupportSQLiteQuery query);

    /* ANEXOS*/

    @Query("SELECT " +
            "anexo_contrato.id_anexo_contrato, " +
            "anexo_contrato.anexo_contrato, " +
            "anexo_contrato.id_especie_anexo, " +
            "anexo_contrato.id_variedad_anexo, " +
            "anexo_contrato.id_ficha_contrato, " +
            "anexo_contrato.protero," +
            "anexo_contrato.rut_agricultor_anexo, " +
            "agricultor.nombre_agricultor," +
            "agricultor.telefono_agricultor," +
            "agricultor.administrador_agricultor," +
            "agricultor.telefono_admin_agricultor," +
            "agricultor.region_agricultor," +
            "agricultor.comuna_agricultor," +
            "agricultor.agricultor_subido," +
            "agricultor.rut_agricultor, " +
            "especie.desc_especie, " +
            "variedad.desc_variedad, " +
            "fichas.anno " +
            "FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.rut_agricultor = anexo_contrato.rut_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "")
    List<AnexoCompleto> getAnexos();

    @Query("SELECT " +
            "anexo_contrato.id_anexo_contrato, " +
            "anexo_contrato.anexo_contrato, " +
            "anexo_contrato.id_especie_anexo, " +
            "anexo_contrato.id_variedad_anexo, " +
            "anexo_contrato.id_ficha_contrato, " +
            "anexo_contrato.protero," +
            "anexo_contrato.rut_agricultor_anexo, " +
            "agricultor.nombre_agricultor," +
            "agricultor.telefono_agricultor," +
            "agricultor.administrador_agricultor," +
            "agricultor.telefono_admin_agricultor," +
            "agricultor.region_agricultor," +
            "agricultor.comuna_agricultor," +
            "agricultor.agricultor_subido," +
            "agricultor.rut_agricultor, " +
            "especie.desc_especie, " +
            "variedad.desc_variedad, " +
            "fichas.anno " +
            "FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.rut_agricultor = anexo_contrato.rut_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "WHERE fichas.anno = :year")
    List<AnexoCompleto> getAnexosByYear(int year);

    @Insert
    void insertAnexo(AnexoContrato anexoContrato);

    @RawQuery
    List<AnexoCompleto> getAnexosFilter(SupportSQLiteQuery query);


    /* ESPECIE*/
    @Query("SELECT * FROM especie")
    List<Especie> getEspecies();

    @Insert
    void insertEspecie(Especie especie);


    /*VARIEDAD*/
    @Query("SELECT  * FROM variedad")
    List<Variedad> getVariedades();

    @Query("SELECT * FROM variedad WHERE id_especie_variedad = :idEspecie")
    List<Variedad> getVariedadesByEspecie(int idEspecie);

    @Insert
    void insertVariedad(Variedad variedad);

    /* AGRICULTOR*/
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
