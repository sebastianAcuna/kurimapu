package cl.smapdev.curimapu.clases.bd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.CantidadVisitas;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Flowering;
import cl.smapdev.curimapu.clases.tablas.Harvest;
import cl.smapdev.curimapu.clases.tablas.Sowing;
import cl.smapdev.curimapu.clases.tablas.Temporada;
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
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
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

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    List<Fotos> getFotosByFieldAndView(int field, int view, int ficha, int idVisita);

    @Query("SELECT  * FROM fotos WHERE vista = :view AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    List<Fotos> getFotosByFieldAndView(int view, int ficha, int idVisita);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND nombre_foto != :nonn AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    List<Fotos> getFotosByFieldAndView(int field, String nonn, int ficha, int idVisita);

    @Query("SELECT  COUNT(id_foto) FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    int getCantAgroByFieldViewAndFicha(int field, int view, int ficha, int idVisita);

    @Query("SELECT * FROM fotos WHERE id_ficha = :idFicha AND id_visita_foto = :idVisita AND favorita = 1 ORDER BY fecha DESC LIMIT 1")
    Fotos getFoto(int idFicha, int idVisita);

    @Query("UPDATE fotos SET id_visita_foto = :idVisita WHERE id_ficha = :idAnexo AND id_visita_foto = 0")
    void updateFotosWithVisita(int idVisita, int idAnexo);



    @Update
    void updateFavorita(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE id_foto = :idFoto")
    Fotos getFotosById(int idFoto);

    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookAndFicha(int fieldbook, int idFicha, int idVisita);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE id_ficha = :idFicha AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookAndFicha(int idFicha, int idVisita);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND vista = :view AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookFichaAndVista(int fieldbook, int idFicha, int view, int idVisita);


    @Query("SELECT  * FROM fotos WHERE id_visita_foto = :id_visita")
    List<Fotos> getFotosByIdVisita(int id_visita);

    @Query("SELECT COUNT(id_foto)  FROM fotos WHERE id_ficha = :idFicha AND id_visita_foto = :idVisita ")
    int getCantFotos(int idFicha, int idVisita);

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


    @Query("SELECT  COUNT(id_temp_harvest) FROM temp_harvest WHERE id_anexo_temp_harvest = :idFicha")
    int getCantTempHarvest(int idFicha);


    /* SOWING TEMP */
    @Query("SELECT * FROM temp_sowing WHERE id_anexo_temp_sowing = :anexo")
    TempSowing getTempSowing(int anexo);


    @Query("SELECT COUNT(*) as todos , etapa_visitas, estado_visita FROM visitas WHERE id_anexo_visita = :idAnexo AND visitas.temporada = :temporada GROUP BY etapa_visitas")
    List<CantidadVisitas> getCantidadVisitas(int idAnexo, int temporada);


    @Query("SELECT COUNT(visitas.id_visita) AS todos, etapa_visitas, estado_visita FROM visitas WHERE temporada = :temporada GROUP BY estado_visita")
    List<CantidadVisitas> getCantidadVisitasByEstado(int temporada);



    @Update
    void updateTempSowing(TempSowing tempSowing);

    @Insert
    long setTempSowing(TempSowing tempSowing);


    @Query("DELETE FROM temp_sowing")
    void deleteTempSowing();


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


    @Query("SELECT  COUNT(id_temp_flowering) FROM temp_flowering WHERE id_anexo_temp_flowering = :idFicha")
    int getCantTempFlowering(int idFicha);




    /* VISITAS */

    @Insert
    long setVisita(Visitas visitas);



    @Query("SELECT * FROM visitas INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato = visitas.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.rut_agricultor = anexo_contrato.rut_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "WHERE id_anexo_visita = :idAnexo AND temporada = :anno " +
            "ORDER BY fecha_visita, hora_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(int idAnexo, int anno);

    @Query("SELECT * FROM visitas INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato = visitas.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.rut_agricultor = anexo_contrato.rut_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "WHERE id_anexo_visita = :idAnexo AND visitas.etapa_visitas = :etapa AND temporada = :anno " +
            "ORDER BY fecha_visita, hora_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(int idAnexo, int etapa, int anno);


    @Update
    void updateVisita(Visitas visitas);


    @Query("SELECT  estado_visita FROM visitas WHERE id_visita = :idVisita ")
    int getEstadoVisita(int idVisita);



    /* SOWING */
    @Insert
    long setSowing(Sowing sowing);

    @Update
    void updateSowing(Sowing sowing);


    @Query("SELECT * FROM sowing WHERE id_anexo_sowing = :idAnexo AND id_visita_sowing = :idVisita")
    Sowing getSowing(int idVisita, int idAnexo);

    @Query("SELECT * FROM crop_rotation WHERE id_anexo_crop_rotation = :idAnexo")
    List<CropRotation> getCropRotation(int idAnexo);

    @Query("SELECT * FROM crop_rotation")
    List<CropRotation> getCropRotation();

    @Insert
    void insertCrop(CropRotation cropRotation);





    /* FLOWERING */
    @Insert
    long setFlowering(Flowering flowering);

    @Query("SELECT  * FROM flowering WHERE id_anexo_flowering = :idAnexo AND id_visita_flowering = :idVisita")
    Flowering getFlowering(int idVisita, int idAnexo);

    @Update
    void updateFlowering(Flowering flowering);


    /* HARVEST */
    @Insert
    long setHarvest(Harvest harvest);

    @Query("SELECT  * FROM harvest WHERE id_anexo_temp_harvest = :idAnexo AND id_visita_harvest = :idVisita")
    Harvest getharvest(int idVisita, int idAnexo);

    @Update
    void updateHarvest(Harvest harvest);

    /* pro_cli_mat  */

    @Insert
    List<Long> insertInterfaz(List<pro_cli_mat> pro_cli_mats);

    @Query("DELETE FROM pro_cli_mat")
    void deleteProCliMat();

    @Query("SELECT  * FROM pro_cli_mat WHERE id_materiales = :idMaterial")
    List<pro_cli_mat> getProCliMatByMateriales(int idMaterial);


    /* temporadas */
    @Insert
    List<Long> insertTemporada(List<Temporada> temporadas);

    @Query("DELETE FROM temporada ")
    void deleteTemporadas();














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
