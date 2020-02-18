package cl.smapdev.curimapu.clases.bd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.Flowering;
import cl.smapdev.curimapu.clases.tablas.Harvest;
import cl.smapdev.curimapu.clases.tablas.Sowing;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Region;
import cl.smapdev.curimapu.clases.relaciones.AgricultorCompleto;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempFlowering;
import cl.smapdev.curimapu.clases.temporales.TempHarvest;
import cl.smapdev.curimapu.clases.temporales.TempSowing;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;

@Dao
public interface MyDao {


    /* FOTOS */
    @Insert
    long insertFotos(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field ")
    List<Fotos> getFotosByField(int field);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha = :ficha")
    List<Fotos> getFotosByFieldAndView(int field, int view, int ficha);

    @Query("SELECT  * FROM fotos WHERE vista = :view AND id_ficha = :ficha")
    List<Fotos> getFotosByFieldAndView(int view, int ficha);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND nombre_foto != :nonn AND id_ficha = :ficha")
    List<Fotos> getFotosByFieldAndView(int field, String nonn, int ficha);

    @Query("SELECT  COUNT(id_foto) FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha = :ficha")
    int getCantAgroByFieldViewAndFicha(int field, int view, int ficha);

    @Update
    void updateFavorita(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE id_foto = :idFoto")
    Fotos getFotosById(int idFoto);

    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND favorita = 1")
    int getCantFavoritasByFieldbookAndFicha(int fieldbook, int idFicha);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE id_ficha = :idFicha AND favorita = 1")
    int getCantFavoritasByFieldbookAndFicha(int idFicha);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND vista = :view AND favorita = 1")
    int getCantFavoritasByFieldbookFichaAndVista(int fieldbook, int idFicha, int view);


    @Query("SELECT  * FROM fotos WHERE id_visita_foto = :id_visita")
    List<Fotos> getFotosByIdVisita(int id_visita);

    @Query("SELECT COUNT(id_foto)  FROM fotos WHERE id_ficha = :idFicha ")
    int getCantFotos(int idFicha);

    @Delete
    void deleteFotos(Fotos fotos);




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


    /* VISITAS TEMP*/
    @Query("SELECT * FROM temp_visitas ")
    TempVisitas getTempFichas();

    @Update
    void updateTempVisitas(TempVisitas tempVisitas);

    @Insert
    long setTempVisitas(TempVisitas tempVisitas);


    @Query("DELETE FROM temp_visitas")
    void deleteTempVisitas();

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='temp_visitas'")
    void resetTempVisitas();

    @Query("SELECT  COUNT(*) FROM temp_visitas WHERE id_anexo_temp_visita = :idFicha")
    int getCantTempVisitas(int idFicha);



    /* HARVEST TEMP */
    @Query("SELECT * FROM temp_harvest WHERE id_anexo_temp_harvest = :anexo")
    TempHarvest getTempHarvest(int anexo);

    @Update
    void updateTempHarvest(TempHarvest tempHarvest);

    @Insert
    long setTempHarvest(TempHarvest tempHarvest);


    @Query("DELETE FROM temp_harvest")
    void deleteTempHarvest();

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='temp_harvest'")
    void resetTempHarvest();

    @Query("SELECT  COUNT(id_temp_harvest) FROM temp_harvest WHERE id_anexo_temp_harvest = :idFicha")
    int getCantTempHarvest(int idFicha);


    /* SOWING TEMP */
    @Query("SELECT * FROM temp_sowing WHERE id_anexo_temp_sowing = :anexo")
    TempSowing getTempSowing(int anexo);



    @Update
    void updateTempSowing(TempSowing tempSowing);

    @Insert
    long setTempSowing(TempSowing tempSowing);


    @Query("DELETE FROM temp_sowing")
    void deleteTempSowing();

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='temp_sowing'")
    void resetTempSowing();

    @Query("SELECT  COUNT(id_temp_sowing) FROM temp_sowing WHERE id_anexo_temp_sowing = :idFicha")
    int getCantTempSowing(int idFicha);



    /* FLOWERING TEMP */
    @Query("SELECT * FROM temp_flowering WHERE id_anexo_temp_flowering = :anexo")
    TempFlowering getTempFlowering(int anexo);

    @Update
    void updateTempFlowering(TempFlowering tempFlowering);

    @Insert
    long setTempFlowering(TempFlowering tempFlowering);


    @Query("DELETE FROM temp_flowering")
    void deleteTempFlowering();

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='temp_flowering'")
    void resetTempFlowering();

    @Query("SELECT  COUNT(id_temp_flowering) FROM temp_flowering WHERE id_anexo_temp_flowering = :idFicha")
    int getCantTempFlowering(int idFicha);




    /* VISITAS */

    @Insert
    long setVisita(Visitas visitas);


    /* SOWING */
    @Insert
    long setSowing(Sowing sowing);

    /* FLOWERING */
    @Insert
    long setFlowering(Flowering flowering);

    /* HARVEST */
    @Insert
    long setHarvest(Harvest harvest);





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
