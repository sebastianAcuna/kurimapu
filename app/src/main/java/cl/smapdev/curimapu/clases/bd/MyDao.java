package cl.smapdev.curimapu.clases.bd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;
import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.CantidadVisitas;
import cl.smapdev.curimapu.clases.relaciones.DetalleCampo;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Flowering;
import cl.smapdev.curimapu.clases.tablas.Harvest;
import cl.smapdev.curimapu.clases.tablas.Provincia;
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
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.temporales.TempFlowering;
import cl.smapdev.curimapu.clases.temporales.TempHarvest;
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
    @Insert
    List<Long> insertFicha(List<Fichas> fichas);

    @Update
    int updateFicha(Fichas fichas);


    @Query("SELECT " +
            "fichas.id_ficha, " +
            "fichas.anno, " +
            "fichas.id_agricultor_ficha, " +
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
            "comuna.id_provincia_comuna, " +
            "provincia.id_provincia, " +
            "provincia.nombre_provincia, " +
            "provincia.id_region_provincia " +
            "FROM fichas " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = fichas.id_agricultor_ficha) " +
            "INNER JOIN comuna ON (comuna.id_comuna = fichas.id_comuna_ficha)" +
            "INNER JOIN region ON (region.id_region = fichas.id_region_ficha)" +
            "INNER JOIN provincia ON (provincia.id_provincia = comuna.id_provincia_comuna)" +
            "WHERE anno = :year ")
    List<FichasCompletas> getFichasByYear(int year);

    @Query("DELETE FROM fichas WHERE subida = 1 ")
    void deleteFichas();


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
    @Query("SELECT valor_detalle FROM detalle_visita_prop where id_prop_mat_cli_detalle = :idProp AND id_visita_detalle = :idVisita")
    String getDatoDetalle(int idProp, int idVisita);

    @Query("SELECT id_det_vis_prop_detalle FROM detalle_visita_prop where id_prop_mat_cli_detalle = :idProp AND id_visita_detalle = :idVisita")
    int getIdDatoDetalle(int idProp, int idVisita);

    @Insert
    void insertDatoDetalle(detalle_visita_prop detalle_visita_prop);
    @Update
    void updateDatoDetalle(detalle_visita_prop detalle_visita_prop);

    @Query("UPDATE detalle_visita_prop SET  id_visita_detalle =  :idVisita ")
    void updateDetallesToVisits(int idVisita);

    @Query("DELETE FROM detalle_visita_prop WHERE id_visita_detalle = 0")
    void deleteDetalleVacios();



    @Query("SELECT COUNT(*) as todos , etapa_visitas, estado_visita FROM visitas WHERE id_anexo_visita = :idAnexo AND visitas.temporada = :temporada GROUP BY etapa_visitas")
    List<CantidadVisitas> getCantidadVisitas(int idAnexo, int temporada);


    @Query("SELECT COUNT(visitas.id_visita) AS todos, etapa_visitas, estado_visita FROM visitas WHERE temporada = :temporada GROUP BY estado_visita")
    List<CantidadVisitas> getCantidadVisitasByEstado(int temporada);


    /* VISITAS */

    @Insert
    long setVisita(Visitas visitas);

    @Insert
    List<Long> setVisita(List<Visitas> visitas);



    @Query("SELECT * FROM visitas INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato = visitas.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_anexo_contrato) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "WHERE id_anexo_visita = :idAnexo AND fecha_visita BETWEEN :annoDesde AND  :annoHasta " +
            "ORDER BY fecha_visita, hora_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(int idAnexo, String annoDesde, String annoHasta);

    @Query("SELECT * FROM visitas INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato = visitas.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_anexo_contrato) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "WHERE id_anexo_visita = :idAnexo AND visitas.etapa_visitas = :etapa AND fecha_visita BETWEEN :annoDesde AND  :annoHasta " +
            "ORDER BY fecha_visita, hora_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(int idAnexo, int etapa, String annoDesde, String annoHasta);


    @Update
    void updateVisita(Visitas visitas);


    @Query("SELECT  estado_visita FROM visitas WHERE id_visita = :idVisita ")
    int getEstadoVisita(int idVisita);


    @Query("DELETE FROM visitas WHERE estado_server_visitas = 1")
    void deleteVisitas();


    /* SOWING */
    @Insert
    long setSowing(Sowing sowing);

    @Update
    void updateSowing(Sowing sowing);


    @Query("SELECT * FROM sowing WHERE id_anexo_sowing = :idAnexo AND id_visita_sowing = :idVisita")
    Sowing getSowing(int idVisita, int idAnexo);

    @Query("SELECT * FROM crop_rotation WHERE id_ficha_crop_rotation = :idFicha")
    List<CropRotation> getCropRotation(int idFicha);

    @Query("SELECT * FROM crop_rotation")
    List<CropRotation> getCropRotation();

    @Query("DELETE FROM crop_rotation")
    void deleteCrops();

    @Insert
    List<Long> insertCrop(List<CropRotation> cropRotation);



    /* DETALLE*/
    @Query("DELETE FROM detalle_visita_prop WHERE estado_detalle = 1")
    void deleteDetalle();


    @Insert
    List<Long> insertDetalle(List<detalle_visita_prop> detalle_visita_props);


    @Query("SELECT " +
            "detalle_visita_prop.valor_detalle, " +
            "detalle_visita_prop.id_prop_mat_cli_detalle, " +
            "pro_cli_mat.nombre_elemento " +
            "FROM detalle_visita_prop " +
            "INNER JOIN pro_cli_mat ON ( pro_cli_mat.id_prop_mat_cli = detalle_visita_prop.id_prop_mat_cli_detalle ) " +
            "WHERE detalle_visita_prop.id_visita_detalle = :id_visita AND pro_cli_mat.id_prop = :id_prop " +
            "ORDER BY pro_cli_mat.id_prop_mat_cli ASC")
    List<DetalleCampo> detalleCampo(int id_visita, int id_prop);


    @Query("SELECT group_concat(valor_detalle ||'--'|| pro_cli_mat.nombre_elemento,'&&')" +
            "FROM visitas " +
            "LEFT JOIN detalle_visita_prop ON (detalle_visita_prop.id_visita_detalle = visitas.id_visita) " +
            "LEFT JOIN pro_cli_mat ON (pro_cli_mat.id_prop_mat_cli = detalle_visita_prop.id_prop_mat_cli_detalle) " +
            "WHERE visitas.id_anexo_visita = :idAnexo AND pro_cli_mat.id_prop = :idProp GROUP BY id_visita ORDER BY visitas.id_anexo_visita, detalle_visita_prop.id_prop_mat_cli_detalle ASC")
    List<String> getDetalleCampo(int idAnexo, int idProp);


    @Query("SELECT COUNT(id_prop_mat_cli) FROM pro_cli_mat WHERE id_prop = :idEtapa")
    int getCorteCabecera(int idEtapa);




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

    @Query("SELECT  * FROM pro_cli_mat WHERE id_materiales = :idMaterial AND id_etapa = :idEtapa ORDER BY orden ASC ")
    List<pro_cli_mat> getProCliMatByMateriales(int idMaterial, int idEtapa);

    @Query("SELECT  * FROM pro_cli_mat WHERE id_prop = :idProp ORDER BY orden ASC ")
    List<pro_cli_mat> getProCliMatByIdProp(int idProp);


    /* temporadas */
    @Insert
    List<Long> insertTemporada(List<Temporada> temporadas);

    @Query("SELECT  * FROM temporada ")
    List<Temporada> getTemporada();

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
            "anexo_contrato.id_agricultor_anexo, " +
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
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
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
            "anexo_contrato.id_agricultor_anexo, " +
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
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "WHERE fichas.anno = :year")
    List<AnexoCompleto> getAnexosByYear(int year);

    @Insert
    void insertAnexo(AnexoContrato anexoContrato);

    @Insert
    List<Long> insertAnexo(List<AnexoContrato> anexoContrato);

    @RawQuery
    List<AnexoCompleto> getAnexosFilter(SupportSQLiteQuery query);


    @Query("DELETE FROM anexo_contrato")
    void deleteAnexos();


    /* ESPECIE*/
    @Query("SELECT * FROM especie")
    List<Especie> getEspecies();

    @Insert
    void insertEspecie(Especie especie);
    @Insert
    List<Long> insertEspecie(List<Especie> especie);

    @Query("DELETE FROM especie")
    void deleteEspecie();



    /*VARIEDAD*/
    @Query("SELECT  * FROM variedad")
    List<Variedad> getVariedades();

    @Query("SELECT * FROM variedad WHERE id_especie_variedad = :idEspecie")
    List<Variedad> getVariedadesByEspecie(int idEspecie);

    @Insert
    void insertVariedad(Variedad variedad);
    @Insert
    List<Long> insertVariedad(List<Variedad> variedad);

    @Query("DELETE FROM variedad")
    void deleteVariedad();


    /* AGRICULTOR*/
    @Insert
    void insertAgricultor(Agricultor agricultor);

    @Insert
    List<Long> insertAgricultor(List<Agricultor> agricultor);

    @Query("SELECT  * FROM agricultor")
    List<Agricultor> getAgricultores();


    @Query("SELECT  * FROM agricultor INNER JOIN comuna ON comuna.id_comuna = agricultor.comuna_agricultor WHERE rut_agricultor = :rut")
    AgricultorCompleto getAgricultorByRut(String rut);

    @Query("DELETE FROM agricultor")
    void deleteAgricultores();

    @Query("SELECT id_agricultor FROM agricultor WHERE rut_agricultor = :rutAgri")
    int getIdAgricutorByRut(String rutAgri);




    @Insert
    List<Long> insertRegiones(List<Region> regiones);

    @Insert
    void insertRegion(Region regiones);

    @Query("SELECT * FROM region ")
    List<Region> getRegiones();

    @Query("DELETE FROM region")
    void deleteRegiones();



    @Insert
    List<Long> insertProvincias(List<Provincia> provincias);

    @Insert
    void insertProvincia(Provincia provincia);

    @Query("SELECT * FROM provincia ")
    List<Provincia> getProvincias();


    @Query("SELECT * FROM provincia WHERE id_region_provincia = :region")
    List<Provincia> getProvinciaByRegion(int region);

    @Query("DELETE FROM provincia")
    void deleteProvincia();





    @Insert
    List<Long> insertComunas(List<Comuna> comunas);

    @Insert
    void insertComuna(Comuna comunas);

    @Query("SELECT * FROM comuna ")
    List<Comuna> getComunas();


    @Query("SELECT * FROM comuna WHERE id_provincia_comuna = :provincia")
    List<Comuna> getComunaByProvincia(int provincia);


    @Query("DELETE FROM comuna")
    void deleteComuna();



}
