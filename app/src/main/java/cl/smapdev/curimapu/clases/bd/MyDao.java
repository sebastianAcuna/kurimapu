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
import cl.smapdev.curimapu.clases.tablas.Clientes;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.FichaMaquinaria;
import cl.smapdev.curimapu.clases.tablas.Lotes;
import cl.smapdev.curimapu.clases.tablas.Maquinaria;
import cl.smapdev.curimapu.clases.tablas.Predios;
import cl.smapdev.curimapu.clases.tablas.Provincia;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.TipoRiego;
import cl.smapdev.curimapu.clases.tablas.TipoSuelo;
import cl.smapdev.curimapu.clases.tablas.TipoTenenciaMaquinaria;
import cl.smapdev.curimapu.clases.tablas.TipoTenenciaTerreno;
import cl.smapdev.curimapu.clases.tablas.UnidadMedida;
import cl.smapdev.curimapu.clases.tablas.Usuario;
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
import cl.smapdev.curimapu.clases.temporales.TempVisitas;

@Dao
public interface MyDao {


    /* FOTOS */
    @Insert
    long insertFotos(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE estado_fotos = 0 AND id_visita_foto > 0")
    List<Fotos> getFotos();

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field ")
    List<Fotos> getFotosByField(int field);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    List<Fotos> getFotosByFieldAndView(int field, int view, String ficha, int idVisita);

    @Query("SELECT  * FROM fotos WHERE vista = :view AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    List<Fotos> getFotosByFieldAndView(int view, String ficha, int idVisita);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND nombre_foto != :nonn AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    List<Fotos> getFotosByFieldAndView(int field, String nonn, String ficha, int idVisita);

    @Query("SELECT  COUNT(id_foto) FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha = :ficha AND id_visita_foto = :idVisita")
    int getCantAgroByFieldViewAndFicha(int field, int view, String ficha, int idVisita);

    @Query("SELECT * FROM fotos WHERE id_ficha = :idFicha AND id_visita_foto = :idVisita AND favorita = 1 ORDER BY fecha DESC LIMIT 1")
    Fotos getFoto(String idFicha, int idVisita);

    @Query("UPDATE fotos SET id_visita_foto = :idVisita WHERE id_ficha = :idAnexo AND id_visita_foto = 0")
    void updateFotosWithVisita(int idVisita, String idAnexo);

    @Query("UPDATE fotos SET estado_fotos = 1, cabecera_fotos = :idCab WHERE estado_fotos = 0")
    int updateFotosSubidas(int idCab);

    @Query("UPDATE fotos SET estado_fotos = 0, cabecera_fotos = 0 WHERE cabecera_fotos = :idCab")
    int updateFotosBack(int idCab);


    @Update
    void updateFavorita(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE id_foto = :idFoto")
    Fotos getFotosById(int idFoto);

    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookAndFicha(int fieldbook, String idFicha, int idVisita);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE id_ficha = :idFicha AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookAndFicha( String idFicha, int idVisita);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha = :idFicha AND vista = :view AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookFichaAndVista(int fieldbook, String idFicha, int view, int idVisita);


    @Query("SELECT  * FROM fotos WHERE id_visita_foto = :id_visita")
    List<Fotos> getFotosByIdVisita(int id_visita);

    @Query("SELECT COUNT(id_foto)  FROM fotos WHERE id_ficha = :idFicha AND id_visita_foto = :idVisita ")
    int getCantFotos(String idFicha, int idVisita);

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
    List<FichasCompletas> getFichasByYear(String year);

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

    @Query("SELECT * FROM visitas WHERE estado_server_visitas = 0")
    List<Visitas> getVisitasPorSubir();

    @Query("SELECT valor_detalle FROM detalle_visita_prop where id_prop_mat_cli_detalle = :idProp AND id_visita_detalle = :idVisita")
    String getDatoDetalle(int idProp, int idVisita);

    @Query("SELECT valor_detalle FROM detalle_visita_prop " +
            "INNER JOIN visitas ON (visitas.id_visita = detalle_visita_prop.id_visita_detalle ) " +
            "INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato = visitas.id_anexo_visita) " +
            "WHERE id_prop_mat_cli_detalle = :idProp AND anexo_contrato.id_anexo_contrato = :idAnexo " +
            "ORDER BY detalle_visita_prop.id_det_vis_prop_detalle DESC LIMIT 1;")
    String getDatoDetalle(int idProp, String idAnexo);


    @Query("SELECT id_det_vis_prop_detalle FROM detalle_visita_prop where id_prop_mat_cli_detalle = :idProp AND id_visita_detalle = :idVisita")
    int getIdDatoDetalle(int idProp, int idVisita);

    @Insert
    void insertDatoDetalle(detalle_visita_prop detalle_visita_prop);
    @Update
    void updateDatoDetalle(detalle_visita_prop detalle_visita_prop);

    @Query("UPDATE detalle_visita_prop SET  id_visita_detalle =  :idVisita WHERE id_visita_detalle = 0 ")
    void updateDetallesToVisits(int idVisita);

    @Query("DELETE FROM detalle_visita_prop WHERE id_visita_detalle = 0")
    void deleteDetalleVacios();


    /* ===================================================================================*/

    /* VISITAS */

    @Insert
    long setVisita(Visitas visitas);

    @Insert
    List<Long> setVisita(List<Visitas> visitas);

    @Query("SELECT * FROM visitas " +
            "INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato = visitas.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN clientes ON (clientes.id_ac_clientes = anexo_contrato.id_anexo_contrato) AND (clientes.id_materiales_clientes = variedad.id_variedad) " +
            "WHERE id_anexo_visita = :idAnexo AND anexo_contrato.temporada_anexo = :annoDesde " +
            "ORDER BY fecha_visita, hora_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(String idAnexo, String annoDesde);

    @Query("SELECT * FROM visitas INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato = visitas.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN clientes ON (clientes.id_ac_clientes = anexo_contrato.id_anexo_contrato) AND (clientes.id_materiales_clientes = variedad.id_variedad) " +
            "WHERE id_anexo_visita = :idAnexo AND visitas.etapa_visitas = :etapa AND anexo_contrato.temporada_anexo = :annoDesde " +
            "ORDER BY fecha_visita, hora_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(String idAnexo, int etapa, String annoDesde);


    @Query("UPDATE visitas SET estado_server_visitas = 1, cabecera_visita = :idCab  WHERE estado_server_visitas = 0")
    int updateVisitasSubidas(int idCab);

    @Query("UPDATE visitas SET estado_server_visitas = 0, cabecera_visita = 0  WHERE cabecera_visita = :idCab ")
    int updateVisitasBack(int idCab);


    @Query("SELECT * FROM visitas " +
            "INNER JOIN anexo_contrato ON (anexo_contrato.id_anexo_contrato  = visitas.id_anexo_visita)" +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo)" +
            "INNER JOIN region ON (region.id_region = agricultor.region_agricultor)" +
            "INNER JOIN comuna ON (comuna.id_comuna = agricultor.comuna_agricultor)" +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo)" +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo)" +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN predios ON (predios.id_pred = fichas.id_pred_ficha) " +
            "INNER JOIN lote ON (lote.lote = fichas.id_lote_ficha) " +
            "INNER JOIN clientes ON (clientes.id_ac_clientes = anexo_contrato.id_anexo_contrato) AND (clientes.id_materiales_clientes = variedad.id_variedad) " +
            "WHERE id_anexo_visita = :idAnexo ORDER BY id_visita DESC LIMIT 1;")
    VisitasCompletas getUltimaVisitaByAnexo(String idAnexo);


    @Update
    void updateVisita(Visitas visitas);


    @Query("SELECT  estado_visita FROM visitas WHERE id_visita = :idVisita ")
    int getEstadoVisita(int idVisita);


    @Query("DELETE FROM visitas WHERE estado_server_visitas = 1")
    void deleteVisitas();


    @Query("SELECT COUNT(*) as todos , etapa_visitas, estado_visita FROM visitas WHERE id_anexo_visita = :idAnexo AND visitas.temporada = :temporada GROUP BY etapa_visitas")
    List<CantidadVisitas> getCantidadVisitas(String idAnexo, String temporada);


    @Query("SELECT COUNT(visitas.id_visita) AS todos, etapa_visitas, estado_visita FROM visitas WHERE temporada = :temporada GROUP BY estado_visita")
    List<CantidadVisitas> getCantidadVisitasByEstado(String temporada);


    /* CROP ROTATION */

    @Query("SELECT * FROM crop_rotation WHERE id_ficha_crop_rotation = :idFicha")
    List<CropRotation> getCropRotation(int idFicha);

    @Query("SELECT * FROM crop_rotation")
    List<CropRotation> getCropRotation();

    @Query("DELETE FROM crop_rotation")
    void deleteCrops();

    @Insert
    List<Long> insertCrop(List<CropRotation> cropRotation);

    /*=====================================================================*/

    /* ERRORES */
    @Insert
    void setErrores(Errores errores);

    @Query("SELECT * FROM errores WHERE estado_error = 0")
    List<Errores> getErroresPorSubir();

    @Query("UPDATE errores SET estado_error = 1, cabecera_error = :idCab WHERE estado_error = 0")
    int updateErroresSubidos(int idCab);

    @Query("UPDATE errores SET estado_error = 0, cabecera_error =  0 WHERE cabecera_error = :idCab")
    int updateErroresBack(int idCab);

    /* =====================================================================*/



    /* DETALLE*/
    @Query("DELETE FROM detalle_visita_prop WHERE estado_detalle = 1")
    void deleteDetalle();

    @Query("UPDATE detalle_visita_prop SET estado_detalle = 1, cabecera_detalle = :idCab WHERE estado_detalle = 0")
    int updateDetalleVisitaSubidas(int idCab);


    @Query("UPDATE detalle_visita_prop SET estado_detalle = 0, cabecera_detalle = 0 WHERE cabecera_detalle = :idCab")
    int updateDetalleVisitaBack(int idCab);

    @Query("SELECT * FROM detalle_visita_prop WHERE estado_detalle = 0 AND id_visita_detalle > 0")
    List<detalle_visita_prop> getDetallesPorSubir();
    @Insert
    List<Long> insertDetalle(List<detalle_visita_prop> detalle_visita_props);


    @Query("SELECT " +
            "detalle_visita_prop.valor_detalle, " +
            "detalle_visita_prop.id_prop_mat_cli_detalle, " +
            "pro_cli_mat.nombre_elemento_en," +
            "pro_cli_mat.nombre_elemento_es " +
            "FROM detalle_visita_prop " +
            "INNER JOIN pro_cli_mat ON ( pro_cli_mat.id_prop_mat_cli = detalle_visita_prop.id_prop_mat_cli_detalle ) " +
            "WHERE detalle_visita_prop.id_visita_detalle = :id_visita AND pro_cli_mat.id_prop = :id_prop " +
            "ORDER BY pro_cli_mat.id_prop_mat_cli ASC")
    List<DetalleCampo> detalleCampo(int id_visita, int id_prop);


    @Query("SELECT group_concat(valor_detalle ||'--'|| pro_cli_mat.nombre_elemento_en,'&&')" +
            "FROM detalle_visita_prop " +
            "LEFT JOIN visitas ON (visitas.id_visita = detalle_visita_prop.id_visita_detalle) " +
            "LEFT JOIN pro_cli_mat ON (pro_cli_mat.id_prop_mat_cli = detalle_visita_prop.id_prop_mat_cli_detalle) " +
            "WHERE (visitas.id_anexo_visita = :idAnexo OR detalle_visita_prop.id_visita_detalle = 0) AND pro_cli_mat.id_prop = :idProp " +
            "GROUP BY id_visita " +
            "ORDER BY visitas.id_anexo_visita, detalle_visita_prop.id_prop_mat_cli_detalle ASC")
    List<String> getDetalleCampo(String idAnexo, int idProp);

    /* ===================================================================================*/


    /* USUARIO */

    @Query("DELETE FROM usuario ")
    void deleteUsuario();

    @Insert
    List<Long> setUsuarios(List<Usuario> usuarios);

    @Query("SELECT  * FROM usuario WHERE usuario.user = :user AND usuario.pass = :pass")
    Usuario getUsuarioLogin(String user, String pass);

    @Query("SELECT * FROM usuario WHERE id_usuario = :idUsuario")
    Usuario getUsuarioById(int idUsuario);

    /*=======================================================================================*/


    /* CONFIG */

    @Query("SELECT * FROM config LIMIT 1")
    Config getConfig();

    @Query("SELECT * FROM config WHERE id = :id LIMIT 1")
    Config getConfig(int id);


    @Insert
    long setConfig(Config config);

    @Update
    int updateConfig(Config config);

    /* ======================================================================================== */


    /* pro_cli_mat  */
    @Insert
    List<Long> insertInterfaz(List<pro_cli_mat> pro_cli_mats);

    @Query("DELETE FROM pro_cli_mat")
    void deleteProCliMat();

    @Query("SELECT  * FROM pro_cli_mat WHERE id_materiales = :idMaterial AND id_etapa = :idEtapa ORDER BY orden ASC ")
    List<pro_cli_mat> getProCliMatByMateriales(String idMaterial, int idEtapa);

    @Query("SELECT  * FROM pro_cli_mat WHERE id_prop = :idProp ORDER BY orden ASC ")
    List<pro_cli_mat> getProCliMatByIdProp(int idProp);
    /* ===================================================================================*/

    /* temporadas */
    @Insert
    List<Long> insertTemporada(List<Temporada> temporadas);

    @Query("SELECT  * FROM temporada ")
    List<Temporada> getTemporada();

    @Query("DELETE FROM temporada ")
    void deleteTemporadas();

    /* =====================================================================*/

    /* UM */
    @Query("SELECT * FROM unidad_medida")
    List<UnidadMedida> getUM();

    @Query("DELETE FROM unidad_medida")
    void deleteUM();

    @Insert
    List<Long> insertUM(List<UnidadMedida> unidadMedidas);

    /*============================================================================*/


    /* ANEXOS*/

    @Query("SELECT  * " +
            "FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN predios ON (predios.id_pred = fichas.id_pred_ficha) " +
            "INNER JOIN lote ON (lote.lote = fichas.id_lote_ficha) " +
            "")
    List<AnexoCompleto> getAnexos();


    @Query("SELECT * FROM anexo_contrato WHERE id_anexo_contrato = :idAnexo")
    AnexoContrato getAnexos(String idAnexo);

    @Query("SELECT * " +
            "FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN predios ON (predios.id_pred = fichas.id_pred_ficha) " +
            "INNER JOIN lote ON (lote.lote = fichas.id_lote_ficha) " +
            "WHERE fichas.anno = :year")
    List<AnexoCompleto> getAnexosByYear(String year);

    @Insert
    void insertAnexo(AnexoContrato anexoContrato);

    @Insert
    List<Long> insertAnexo(List<AnexoContrato> anexoContrato);

    @RawQuery
    List<AnexoCompleto> getAnexosFilter(SupportSQLiteQuery query);


    @Query("DELETE FROM anexo_contrato")
    void deleteAnexos();

    /* ===================================================================================*/

    /* ESPECIE*/
    @Query("SELECT * FROM especie")
    List<Especie> getEspecies();

    @Insert
    void insertEspecie(Especie especie);
    @Insert
    List<Long> insertEspecie(List<Especie> especie);

    @Query("DELETE FROM especie")
    void deleteEspecie();

     /* ================================================================== */


    /*VARIEDAD*/
    @Query("SELECT  * FROM variedad")
    List<Variedad> getVariedades();

    @Query("SELECT * FROM variedad WHERE id_especie_variedad = :idEspecie")
    List<Variedad> getVariedadesByEspecie(String idEspecie);

    @Insert
    void insertVariedad(Variedad variedad);
    @Insert
    List<Long> insertVariedad(List<Variedad> variedad);

    @Query("DELETE FROM variedad")
    void deleteVariedad();

    /* ====================================================================== */


    /*CLIENTES*/

    @Query("SELECT  * FROM clientes")
    List<Clientes> getClientes();


    @Query("SELECT * FROM clientes WHERE clientes.id_ac_clientes = :idAnexo AND clientes.id_materiales_clientes = :idMateriales ")
    List<Clientes> getClientesByAnexoMaterial(String idAnexo, String idMateriales);

    @Insert
    void insertClientes(Clientes clientes);
    @Insert
    List<Long> insertClientes(List<Clientes> clientes);

    @Query("DELETE FROM clientes")
    void deleteClientes();



    /* ====================================================================== */

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
    String getIdAgricutorByRut(String rutAgri);

    /* ============================================================================ */


    /* REGIONES  */
    @Insert
    List<Long> insertRegiones(List<Region> regiones);

    @Insert
    void insertRegion(Region regiones);

    @Query("SELECT * FROM region ")
    List<Region> getRegiones();

    @Query("DELETE FROM region")
    void deleteRegiones();

    /*====================================================================================*/


    /* PROVINCIAS */
    @Insert
    List<Long> insertProvincias(List<Provincia> provincias);

    @Insert
    void insertProvincia(Provincia provincia);

    @Query("SELECT * FROM provincia ")
    List<Provincia> getProvincias();


    @Query("SELECT * FROM provincia WHERE id_region_provincia = :region")
    List<Provincia> getProvinciaByRegion(String region);

    @Query("DELETE FROM provincia")
    void deleteProvincia();
    /* ===================================================================================*/



    /* COMUNAS */
    @Insert
    List<Long> insertComunas(List<Comuna> comunas);

    @Insert
    void insertComuna(Comuna comunas);

    @Query("SELECT * FROM comuna ")
    List<Comuna> getComunas();


    @Query("SELECT * FROM comuna WHERE id_provincia_comuna = :provincia")
    List<Comuna> getComunaByProvincia(String provincia);


    @Query("DELETE FROM comuna")
    void deleteComuna();
    /* ===================================================================================*/

    /* FICHAS MAQUINARIAS */
    @Insert
    List<Long> insertFichaMaquinaria(List<FichaMaquinaria> fichaMaquinarias);

    @Query("SELECT * FROM ficha_maquinaria")
    List<FichaMaquinaria> getFichasMaquinarias();

    @Query("DELETE FROM ficha_maquinaria")
    void deleteFichaMaquinaria();

    /* ===================================================================================*/

    /* LOTES */
    @Insert
    List<Long> insertLotes(List<Lotes> lotes);

    @Query("SELECT * FROM lote ")
    List<Lotes> getLotes();

    @Query("DELETE FROM lote")
    void deleteLotes();

    /* ===================================================================================*/

    /* MAQUINARIA */
    @Insert
    List<Long> insertMaquinara(List<Maquinaria> maquinarias);

    @Query("SELECT * FROM maquinaria ")
    List<Maquinaria> getMaquinaria();

    @Query("DELETE FROM maquinaria")
    void deleteMaquinaria();

    /* ===================================================================================*/

    /* PREDIOS */
    @Insert
    List<Long> insertPredios(List<Predios> predios);

    @Query("SELECT * FROM predios ")
    List<Predios> getPredios();

    @Query("DELETE FROM predios")
    void deletePredios();

    /* ===================================================================================*/

    /* TIPO RIEGO */
    @Insert
    List<Long> insertTipoRiego(List<TipoRiego> tipoRiegos);

    @Query("SELECT * FROM tipo_riego ")
    List<TipoRiego> getTipoRiego();

    @Query("DELETE FROM tipo_riego")
    void deleteTipoRiego();

    /* ===================================================================================*/


    /* TIPO SUELO */
    @Insert
    List<Long> insertTipoSuelo(List<TipoSuelo> tipoSuelos);

    @Query("SELECT * FROM tipo_suelo ")
    List<TipoSuelo> getTipoSuelo();

    @Query("DELETE FROM tipo_suelo")
    void deleteTipoSuelo();

    /* ===================================================================================*/

    /* TIPO TENENCIA MAQUINARIA */
    @Insert
    List<Long> insertTipoTenenciaMaquinaria(List<TipoTenenciaMaquinaria> tenenciaMaquinarias);

    @Query("SELECT * FROM tipo_tenencia_maquinaria ")
    List<TipoTenenciaMaquinaria> getTipoTenenciaMaquinaria();

    @Query("DELETE FROM tipo_tenencia_maquinaria")
    void deleteTipoTenenciaMaquinaria();

    /* ===================================================================================*/

    /* TIPO TENENCIA TERRENO */
    @Insert
    List<Long> insertTipoTenenciaTerreno(List<TipoTenenciaTerreno> tenenciaTerrenos);

    @Query("SELECT * FROM tipo_tenencia_terreno ")
    List<TipoTenenciaTerreno> getTipoTenenciaTerreno();

    @Query("DELETE FROM tipo_tenencia_terreno")
    void deleteTipoTenenciaTerreno();

    /* ===================================================================================*/








}
