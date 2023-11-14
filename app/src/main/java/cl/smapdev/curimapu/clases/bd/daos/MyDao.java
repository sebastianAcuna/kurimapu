package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;
import cl.smapdev.curimapu.clases.relaciones.CantidadVisitas;
import cl.smapdev.curimapu.clases.relaciones.DetalleCampo;
import cl.smapdev.curimapu.clases.relaciones.SitiosNoVisitadosAnexos;
import cl.smapdev.curimapu.clases.relaciones.VisitaDetalle;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.AgrPredTemp;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.CardViewsResumen;
import cl.smapdev.curimapu.clases.tablas.Clientes;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.FichaMaquinaria;
import cl.smapdev.curimapu.clases.tablas.FichasNew;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;
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
import cl.smapdev.curimapu.clases.tablas.cli_pcm;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.tablas.quotation;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;

@Dao
public interface MyDao {


    /* FOTOS */
    @Insert
    long insertFotos(Fotos fotos);


    @Query("SELECT * FROM agricultor;")
    List<Agricultor> getAgricultoresss();

    @Query("SELECT  * FROM anexo_contrato AC " +
            " INNER JOIN materiales M ON (AC.id_variedad_anexo = M.id_variedad) " +
            " INNER JOIN especie E ON (E.id_especie = M.id_especie_variedad) " +
            " INNER JOIN lote L  ON (AC.id_potrero = L.lote) " +
            " INNER JOIN predio P  ON (L.id_pred_lote = P.id_pred) " +
            " INNER JOIN ficha_new F ON  (F.id_ficha_new = AC.id_ficha_contrato) " +
            " INNER JOIN usuarios U1 ON (F.id_usuario_new = U1.id_usuario) " +
            " LEFT JOIN agricultor AG ON (F.id_agric_new = AG.id_agricultor) " +
            " INNER JOIN temporada T ON ( F.id_tempo_new = T.id_tempo_tempo) " +
            " WHERE  T.id_tempo_tempo = :id_tempo AND AC.destruido = '0' " +
            " ORDER BY E.desc_especie ASC, AC.num_anexo ASC ; ")
    List<SitiosNoVisitadosAnexos>  getSitiosNoVisitados(int id_tempo);


    @Query("SELECT * FROM visita V " +
            "LEFT JOIN detalle_visita_prop DVP ON (DVP.id_visita_detalle = V.id_visita) " +
            "LEFT JOIN pro_cli_mat PCM ON (PCM.id_prop_mat_cli = DVP.id_prop_mat_cli_detalle) " +
            "WHERE id_anexo_visita = :id_ac  " +
            "AND V.fecha_visita IS NOT NULL AND ( (PCM.marca_sitios_no_visitados = 1 AND valor_detalle IS NOT NULL ) OR valor_detalle IS NULL ); ")
    List<VisitaDetalle> getVisitaDetalle(int id_ac);

    @Query("SELECT * FROM visita V WHERE V.id_anexo_visita = :id_ac  GROUP BY V.id_visita ORDER BY V.fecha_visita DESC LIMIT 1;")
    List<Visitas> traeVisitaPorAnexo(int id_ac);


    @Query("SELECT * FROM visita V " +
            "INNER JOIN anexo_contrato AC ON (AC.id_anexo_contrato = V.id_anexo_visita) " +
            "INNER JOIN ficha_new F ON (F.id_ficha_new = AC.id_ficha_contrato) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "INNER JOIN agricultor A ON (A.id_agricultor = F.id_agric_new) " +
            "INNER JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo) " +
            "INNER JOIN especie E ON (E.id_especie = M.id_especie_variedad) " +
            "INNER JOIN usuarios U ON (V.id_user_visita = U.id_usuario) " +
            "WHERE F.id_tempo_new = :id_tempo AND AC.destruido = '0' " +
            "ORDER BY V.fecha_visita DESC, AC.num_anexo ASC, E.desc_especie ASC, V.hora_visita DESC ; ")
    List<VisitasCompletas> getPrimeraPrioridad(int id_tempo);




    @Query("SELECT  * FROM fotos WHERE estado_fotos = 0 AND id_visita_foto > 0")
    List<Fotos> getFotos();

    @Query("SELECT  * FROM fotos WHERE estado_fotos = 0 AND id_visita_foto > 0 AND id_visita_foto = :id_visita; ")
    List<Fotos> getFotosLimit(int id_visita);

    @Query("SELECT  * FROM fotos WHERE estado_fotos = 0 AND id_visita_foto > 0 AND tomada_foto = 1 ; ")
    List<Fotos> getFotosLimitTomada();

    @Query("UPDATE fotos SET tomada_foto = 1 WHERE estado_fotos = 0 AND id_visita_foto = :id_visita; ")
    void marcarFotos(int id_visita);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field ")
    List<Fotos> getFotosByField(int field);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha_fotos = :ficha AND id_visita_foto = :idVisita AND id_visita_servidor_foto = :idVisitaServidor AND id_dispo_foto = :idDispo")
    List<Fotos> getFotosByFieldAndView(int field, int view, String ficha, int idVisita, int idVisitaServidor, int idDispo);

    @Query("SELECT  * FROM fotos " +
            "WHERE vista = :view AND id_ficha_fotos = :idFichaFoto AND id_visita_foto = :idVisita " +
            "AND id_visita_servidor_foto = :idVisitaServidor AND id_dispo_foto = :idDispo")
    List<Fotos> getFotosByFieldAndViewVisitas(int view, String idFichaFoto,  int idVisita, int idVisitaServidor, int idDispo);

    @Query("SELECT  * FROM fotos WHERE vista = :view AND id_ficha_fotos = :ficha AND id_visita_foto = :idVisita AND id_visita_servidor_foto = :idVisitaServidor AND id_dispo_foto = :idDispo")
    List<Fotos> getFotosByFieldAndView(int view, String ficha, int idVisita, int idVisitaServidor, int idDispo);

    @Query("SELECT  * FROM fotos WHERE fieldbook  = :field AND nombre_foto != :nonn AND id_ficha_fotos = :ficha AND id_visita_foto = :idVisita")
    List<Fotos> getFotosByFieldAndViewNom(int field, String nonn, String ficha, int idVisita);

    @Query("SELECT  COUNT(id_foto) FROM fotos WHERE fieldbook  = :field AND vista = :view AND id_ficha_fotos = :ficha AND id_visita_foto = :idVisita")
    int getCantAgroByFieldViewAndFicha(int field, int view, String ficha, int idVisita);

    @Query("SELECT * FROM fotos WHERE id_ficha_fotos = :idFicha AND id_visita_foto = :idVisita AND id_visita_servidor_foto = :idVisitaServidor AND id_dispo_foto = :idDispo  AND favorita = 1 ORDER BY fecha DESC LIMIT 1")
    Fotos getFoto(String idFicha, int idVisita, int idVisitaServidor, int idDispo);

    @Query("UPDATE fotos SET id_visita_foto = :idVisita WHERE id_ficha_fotos = :idAnexo AND id_visita_foto = 0")
    void updateFotosWithVisita(int idVisita, String idAnexo);

    @Query("UPDATE fotos SET estado_fotos = 1, cabecera_fotos = :idCab WHERE estado_fotos = 0")
    int updateFotosSubidas(int idCab);

    @Query("UPDATE fotos SET estado_fotos = 1, cabecera_fotos = :idCab, tomada_foto = 0 WHERE estado_fotos = 0 AND tomada_foto = 1; ")
    int updateFotosSubidasTomada(int idCab);

    @Query("UPDATE fotos SET estado_fotos = 0, cabecera_fotos = 0, tomada_foto = 0 WHERE cabecera_fotos = :idCab")
    int updateFotosBack(int idCab);

    @Query("UPDATE fotos SET id_visita_servidor_foto = :idServidor WHERE  id_dispo_foto = :idDispo AND id_visita_foto = :idLocal ")
    int updateFotos(int idServidor, int idLocal, int idDispo);

    @Update
    void updateFavorita(Fotos fotos);

    @Query("SELECT  * FROM fotos WHERE id_foto = :idFoto")
    Fotos getFotosById(int idFoto);

    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha_fotos = :idFicha AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookAndFicha(int fieldbook, String idFicha, int idVisita);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE id_ficha_fotos = :idFicha AND favorita = 1 AND id_visita_foto = :idVisita AND vista = :vista")
    int getCantFavoritasByFieldbookAndFicha( String idFicha, int idVisita, int vista);


    @Query("SELECT COUNT(favorita) FROM fotos WHERE fieldbook = :fieldbook AND id_ficha_fotos = :idFicha AND vista = :view AND favorita = 1 AND id_visita_foto = :idVisita ")
    int getCantFavoritasByFieldbookFichaAndVista(int fieldbook, String idFicha, int view, int idVisita);


    @Query("SELECT  * FROM fotos WHERE id_visita_foto = :id_visita")
    List<Fotos> getFotosByIdVisita(int id_visita);

    @Query("SELECT COUNT(id_foto)  FROM fotos WHERE id_ficha_fotos = :idFicha AND id_visita_foto = :idVisita AND vista = :vista ")
    int getCantFotos(String idFicha, int idVisita, int vista);

    @Delete
    void deleteFotos(Fotos fotos);



    @Query("UPDATE config SET servidorSeleccionado = :servidor ;")
    void updateServidor(String servidor);

    /*FOTOS FICHAS */
    @Insert
    long insertFotosFichas(FotosFichas fotos);

    @Query("SELECT * FROM fotos_fichas WHERE id_ficha_fotos_local = :idFichaLocal AND id_ficha_fotos_servidor = :idFichaServidor AND id_dispo_captura = :idDispo ")
    List<FotosFichas> getFotosFichasByIdes(int idFichaLocal, int idFichaServidor, int idDispo);


    @Query("UPDATE fotos_fichas SET id_ficha_fotos_local = :idFichaLocal , id_ficha_fotos_servidor = :idFichaLocal WHERE id_ficha_fotos_local = 0 AND estado_subida_foto = 0 ")
    int updateFotosFichas(int idFichaLocal);

    @Query("SELECT  * FROM fotos_fichas WHERE estado_subida_foto = 0 AND id_ficha_fotos_local > 0")
    List<FotosFichas> getFotosFichasPorSubir();

    @Query("UPDATE fotos_fichas SET estado_subida_foto = 1, cabecera_subida = :idCab WHERE estado_subida_foto = 0")
    int updateFotosFichasSubidas(int idCab);

    @Query("UPDATE fotos_fichas SET estado_subida_foto = 0, cabecera_subida = 0 WHERE cabecera_subida = :idCab")
    int updateFotosFichasBack(int idCab);


    @Query("UPDATE fotos_fichas SET id_ficha_fotos_servidor = :idServidor WHERE  id_dispo_captura = :idDispo AND id_ficha_fotos_local = :idLocal ")
    int updateFotosFichas(int idServidor, int idLocal, int idDispo);


    /* FICHAS */


    @Insert
    long insertFicha(Fichas fichas);


    @Insert
    List<Long> insertFicha(List<FichasNew> fichas);

    @Insert
    List<Long> insertProsectos(List<Fichas> prospectos);

    @Update
    int updateFicha(Fichas fichas);

    @Query("SELECT * FROM ficha F WHERE F.subida = 0")
    List<Fichas> getFichasPorSubir();

    @Query("UPDATE ficha  SET cabecera_ficha = :cab, subida = 1  WHERE subida = 0")
    int updateFichasSubidas(int cab);

    @Query("UPDATE ficha SET subida = 0 WHERE cabecera_ficha = :cab")
    void updateFichasBack(int cab);


    @Query("SELECT * " +
            "FROM ficha F " +
            "INNER JOIN agricultor A ON (A.id_agricultor = F.id_agricultor_ficha) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_ficha)" +
            "INNER JOIN region R ON (R.id_region = F.id_region_ficha)" +
            "INNER JOIN provincia P ON (P.id_provincia = C.id_provincia_comuna)" +
            "WHERE anno = :year ORDER BY F.activa ASC, F.id_ficha DESC ")
    List<FichasCompletas> getFichasByYear(String year);

    @Query("DELETE FROM ficha_new ; ")
    void deleteFichas();


    @Query("DELETE FROM ficha ; ")
    void deleteProspectos();


    @RawQuery
    List<FichasCompletas> getFichasFilter(SupportSQLiteQuery query);

    @RawQuery
    String getValueResume(SupportSQLiteQuery query);


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

    @Query("SELECT * FROM visita WHERE estado_server_visitas = 0 ORDER BY id_visita ASC LIMIT 3 ")
    List<Visitas> getVisitasPorSubirLimit();

    @Query("SELECT * FROM visita WHERE estado_server_visitas = 0 AND tomadas = 1 ORDER BY id_visita ASC ")
    List<Visitas> getVisitasPorSubirTomadas();



    @Query("UPDATE visita SET tomadas = 1 WHERE estado_server_visitas = 0 AND id_visita = :id_visita ")
    void marcarVisitas(int id_visita);


    @Query("SELECT * FROM visita WHERE estado_server_visitas = 0 ORDER BY fecha_visita ASC")
    List<Visitas> getVisitasPorSubir();


    @Query("SELECT valor_detalle FROM detalle_visita_prop where id_prop_mat_cli_detalle = :idProp AND id_visita_detalle = :idVisita")
    String getDatoDetalle(int idProp, int idVisita);

    @Query("SELECT valor_detalle FROM detalle_visita_prop DVP " +
            "INNER JOIN visita V ON (V.id_visita = DVP.id_visita_detalle ) " +
            "INNER JOIN anexo_contrato  AC ON (AC.id_anexo_contrato = V.id_anexo_visita) " +
            "WHERE id_prop_mat_cli_detalle = :idProp AND AC.id_anexo_contrato = :idAnexo " +
            "ORDER BY DVP.id_det_vis_prop_detalle DESC LIMIT 1;")
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


    @Query("SELECT * FROM visita WHERE id_visita  = :idVisita")
    Visitas getVisitas(int idVisita);
    /* ===================================================================================*/

    @Query("SELECT * FROM card_view_resumen WHERE id_tempo_cardiview = :idTempo")
    List<CardViewsResumen> getResumen(String idTempo);

    @Query("DELETE  FROM card_view_resumen ")
    void deleteResumenes();

    @Insert
    List<Long> insertResumenes(List<CardViewsResumen> cardViewsResumen);


    @Query("SELECT count(V.id_visita) AS total, VA.desc_variedad AS nombre , V.id_visita AS id FROM visita V " +
            "INNER JOIN anexo_contrato  AC ON (V.id_anexo_visita = AC.id_anexo_contrato) " +
            "INNER JOIN materiales VA ON (VA.id_variedad = AC.id_variedad_anexo) " +
            "WHERE temporada = :temp  GROUP BY AC.id_variedad_anexo")
    List<CardViewsResumen> getCantidadVariedadesByVisita(String temp);

    @Query("SELECT count(V.id_visita) AS total, E.desc_especie AS nombre, V.id_visita AS id  FROM visita V " +
            "INNER JOIN anexo_contrato  AC ON (V.id_anexo_visita = AC.id_anexo_contrato) " +
            "INNER JOIN especie E ON (E.id_especie = AC.id_especie_anexo) " +
            "WHERE temporada = :temp  GROUP BY AC.id_especie_anexo")
    List<CardViewsResumen> getCantidadEspeciesByVisita(String temp);

    @Query("SELECT P.nombre AS nombre, COUNT(id_visita) AS total, P.id_pred AS id FROM predio P " +
            "INNER JOIN lote L ON (L.id_pred_lote = P.id_pred) " +
            "INNER JOIN anexo_contrato AC ON  (AC.id_potrero = L.lote) " +
            "INNER JOIN agri_pred_temp APT ON (APT.id_pred = P.id_pred AND APT.id_agric = AC.id_agricultor_anexo) " +
            "LEFT JOIN visita V ON (AC.id_anexo_contrato = V.id_anexo_visita)  WHERE APT.id_tempo = :temp GROUP BY P.id_pred")
    List<CardViewsResumen> getCantidadVisitasByPotrero(String temp);


    @Query("SELECT P.nombre AS nombre, P.id_pred as total, ATT.id_agric as id FROM predio P " +
            "INNER JOIN agricultor A ON (A.id_agricultor = ATT.id_agric)  " +
            "INNER JOIN lote L ON (L.id_pred_lote = P.id_pred)  " +
            "INNER JOIN anexo_contrato AC ON (AC.id_potrero = L.lote) " +
            "INNER JOIN agri_pred_temp ATT ON (ATT.id_pred = P.id_pred AND ATT.id_agric = AC.id_agricultor_anexo)  " +
            "LEFT JOIN visita V ON (V.id_anexo_visita = AC.id_anexo_contrato) " +
            "WHERE ATT.id_tempo = :temp AND V.fecha_visita <= :fecha GROUP BY p.id_pred")
    List<CardViewsResumen> getCantidadPrediosNoVisitados(String temp, String fecha);



    /* ===================================================================================*/


    @Query("UPDATE visita SET estado_server_visitas = 0 WHERE id_user_visita = 2 ")
    void updateVisitaTest();

    @Query("UPDATE fotos SET estado_fotos = 1;")
    void updateFotoTest();


    /* VISITAS */

    @Insert
    long setVisita(Visitas visitas);

    @Insert
    List<Long> setVisita(List<Visitas> visitas);

    @Query("SELECT *, C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna FROM visita V " +
            "INNER JOIN anexo_contrato AC ON (AC.id_anexo_contrato = V.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = AC.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = AC.id_especie_anexo) " +
            "INNER JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo) " +
            "INNER JOIN ficha_new F ON (F.id_ficha_new = AC.id_ficha_contrato) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "INNER JOIN quotation Q ON (Q.id_materiales = M.id_variedad) " +
            "INNER JOIN cliente ON (cliente.id_clientes_tabla = Q.cliente) " +
            "WHERE id_anexo_visita = :idAnexo AND AC.temporada_anexo = :annoDesde " +
            "GROUP BY V.id_visita " +
            "ORDER BY V.fecha_visita, V.hora_visita, V.id_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(String idAnexo, String annoDesde);


    @Query("SELECT *, C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna FROM visita V " +
            "INNER JOIN anexo_contrato AC ON (AC.id_anexo_contrato = V.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = AC.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = AC.id_especie_anexo) " +
            "INNER JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo) " +
            "INNER JOIN ficha_new F ON (F.id_ficha_new= AC.id_ficha_contrato)" +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "INNER JOIN quotation Q ON (Q.id_materiales = M.id_variedad) " +
            "INNER JOIN cliente ON (cliente.id_clientes_tabla = Q.cliente) " +
            "LEFT JOIN fotos Fo ON (Fo.id_visita_foto = V.id_visita_local AND Fo.id_dispo_foto  = V.id_dispo  AND Fo.favorita = 1)" +
            "WHERE id_anexo_visita = :idAnexo " +
            "GROUP BY V.id_visita " +
            "ORDER BY V.fecha_visita DESC, V.hora_visita DESC, V.id_visita DESC")
    List<VisitasCompletas> getVisitasCompletasWithFotos(String idAnexo);

    @Query("SELECT * , C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna " +
            "FROM visita V " +
            "INNER JOIN anexo_contrato AC ON (AC.id_anexo_contrato = V.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = AC.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = AC.id_especie_anexo) " +
            "INNER JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo) " +
            "INNER JOIN ficha_new F ON (F.id_ficha_new = AC.id_ficha_contrato)" +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "INNER JOIN quotation Q ON (Q.id_materiales = M.id_variedad) " +
            "INNER JOIN cliente ON (cliente.id_clientes_tabla = Q.cliente) " +
            "LEFT JOIN fotos Fo ON (Fo.id_visita_foto = V.id_visita_local AND Fo.id_dispo_foto  = V.id_dispo  AND Fo.favorita = 1)" +
            "WHERE id_anexo_visita = :idAnexo AND V.etapa_visitas = :etapa AND AC.temporada_anexo = :annoDesde " +
            "GROUP BY V.id_visita " +
            "ORDER BY V.fecha_visita, V.hora_visita, V.id_visita DESC")
    List<VisitasCompletas> getVisitasCompletasWithFotos(String idAnexo, int etapa, String annoDesde);

    @Query("SELECT *, C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna  " +
            "FROM visita V " +
            "INNER JOIN anexo_contrato AC ON (AC.id_anexo_contrato = V.id_anexo_visita) " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = AC.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = AC.id_especie_anexo) " +
            "INNER JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo) " +
            "INNER JOIN ficha_new F ON (F.id_ficha_new = AC.id_ficha_contrato) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "INNER JOIN quotation Q ON (Q.id_materiales = M.id_variedad) " +
            "INNER JOIN cliente ON (cliente.id_clientes_tabla = Q.cliente) " +
            "WHERE id_anexo_visita = :idAnexo AND V.etapa_visitas = :etapa AND AC.temporada_anexo = :annoDesde " +
            "GROUP BY V.id_visita " +
            "ORDER BY V.fecha_visita, V.hora_visita, V.id_visita DESC")
    List<VisitasCompletas> getVisitasCompletas(String idAnexo, int etapa, String annoDesde);


    @Query("UPDATE visita SET estado_server_visitas = 1, cabecera_visita = :idCab, tomadas = 0  WHERE estado_server_visitas = 0 AND tomadas = 1 ;")
    int updateVisitasSubidasTomadas(int idCab);


    @Query("UPDATE visita SET  tomadas = 0  WHERE  tomadas = 1 ;")
    int updateVisitasSubidasTomadasBack();

    @Query("UPDATE detalle_visita_prop SET  tomada_detalle = 0  WHERE  tomada_detalle = 1 ;")
    int updateDetalleSubidasTomadasBack();

    @Query("UPDATE fotos SET  tomada_foto = 0  WHERE  tomada_foto = 1 ;")
    int updateFotosSubidasTomadasBack();

    @Query("UPDATE visita SET estado_server_visitas = 0, cabecera_visita = 0, tomadas = 0  WHERE cabecera_visita = :idCab ")
    int updateVisitasBack(int idCab);

    @Query("SELECT * FROM anexo_contrato AC WHERE " +
            " id_anexo_contrato IN (SELECT id_anexo_visita FROM visita) AND " +
            " ( id_anexo_contrato IN (SELECT id_ac_corr_fech FROM anexo_correo_fechas WHERE anexo_correo_fechas.inicio_siembra IS NULL OR anexo_correo_fechas.inicio_siembra = '' ) OR " +
            " id_anexo_contrato NOT IN (SELECT id_ac_corr_fech FROM anexo_correo_fechas) ) AND " +
            " AC.temporada_anexo = :tempo")
    List<AnexoContrato> getAnexosSinFechaSiembra(String tempo);

    @Query("SELECT * FROM anexo_contrato AC WHERE " +
            " id_anexo_contrato IN (SELECT id_anexo_visita FROM visita) AND " +
            " ( id_anexo_contrato IN (SELECT id_ac_corr_fech FROM anexo_correo_fechas WHERE anexo_correo_fechas.inicio_siembra IS NULL OR anexo_correo_fechas.inicio_siembra = '') OR " +
            " id_anexo_contrato NOT IN (SELECT id_ac_corr_fech FROM anexo_correo_fechas) ) AND " +
            " id_anexo_contrato = :id_ac LIMIT 1")
    AnexoContrato getAnexosSinFechaSiembraByAc(String id_ac);

    @Query("SELECT *, C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna  " +
            "FROM visita V " +
            "INNER JOIN anexo_contrato AC ON (AC.id_anexo_contrato  = V.id_anexo_visita)" +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = AC.id_agricultor_anexo)" +
            "LEFT  JOIN region ON (region.id_region = agricultor.region_agricultor)" +
            "INNER JOIN comuna C ON (C.id_comuna = agricultor.comuna_agricultor)" +
            "INNER JOIN especie ON (especie.id_especie = AC.id_especie_anexo)" +
            "INNER JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo)" +
            "INNER JOIN ficha_new  F ON (F.id_ficha_new = AC.id_ficha_contrato) " +
            "INNER JOIN predio P ON (P.id_pred = F.id_pred_new) " +
            "INNER JOIN lote ON (lote.lote = F.id_lote_new) " +
            "INNER JOIN quotation Q ON (Q.id_materiales = M.id_variedad) " +
            "INNER JOIN cliente ON (cliente.id_clientes_tabla = Q.cliente) " +
            "WHERE id_anexo_visita = :idAnexo ORDER BY id_visita DESC LIMIT 1;")
    VisitasCompletas getUltimaVisitaByAnexo(String idAnexo);


    @Update
    void updateVisita(Visitas visitas);


    @Query("SELECT  estado_visita FROM visita  WHERE id_visita = :idVisita ")
    int getEstadoVisita(int idVisita);


    @Query("DELETE FROM visita  WHERE estado_server_visitas = 1")
    void deleteVisitas();


    @Query("SELECT COUNT(*) as todos , etapa_visitas, estado_visita FROM visita V WHERE id_anexo_visita = :idAnexo AND V.temporada = :temporada GROUP BY etapa_visitas")
    List<CantidadVisitas> getCantidadVisitas(String idAnexo, String temporada);

    @Query("SELECT COUNT(*) as total , etapa_visitas as nombre, estado_visita AS id FROM visita V WHERE V.temporada = :temporada ")
    CardViewsResumen getCantidadVisitas(String temporada);

    @Query("SELECT SUM(superficie) AS total, E.desc_especie AS nombre, id_especie AS id FROM anexo_contrato AC " +
            "INNER JOIN especie E ON (AC.id_especie_anexo = E.id_especie) " +
            "WHERE AC.temporada_anexo = :temporada " +
            "GROUP BY E.id_especie")
    List<CardViewsResumen> getHaPorEspecie(String temporada);


    @Query("SELECT SUM(superficie) AS total, M.desc_hibrido_variedad AS nombre, id_variedad AS id  FROM anexo_contrato  AC " +
            "INNER JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo)  " +
            "WHERE AC.temporada_anexo = :temporada   " +
            "GROUP BY M.id_variedad")
    List<CardViewsResumen> getHaPorVariedad(String temporada);


    @Query("SELECT COUNT(V.id_visita) AS todos, etapa_visitas, estado_visita FROM visita V WHERE temporada = :temporada GROUP BY estado_visita")
    List<CantidadVisitas> getCantidadVisitasByEstado(String temporada);


    /* CROP ROTATION */

    @Query("SELECT * FROM crop_rotation WHERE id_ficha_crop_rotation = :idFicha AND tipo_crop = 'F' LIMIT 5 ")
    List<CropRotation> getCropRotation(int idFicha);

    @Query("SELECT * FROM crop_rotation WHERE id_ficha_local_cp = :idFicha AND tipo_crop = 'P'  ORDER BY  crop_rotation.id_ficha_crop_rotation DESC, crop_rotation.temporada_crop_rotation ASC LIMIT 5")
    List<CropRotation> getCropRotationLocal(int idFicha);

    @Query("SELECT * FROM crop_rotation WHERE id_ficha_crop_rotation = :idFicha AND tipo_crop = 'P'  ORDER BY  crop_rotation.id_ficha_crop_rotation DESC, crop_rotation.temporada_crop_rotation ASC LIMIT 5")
    List<CropRotation> getCropRotationRemotos(int idFicha);

    @Query("SELECT * FROM crop_rotation")
    List<CropRotation> getCropRotation();

    @Query("DELETE FROM crop_rotation  ")
    void deleteCrops();



    @Query("SELECT * FROM crop_rotation WHERE estado_subida_crop_rotation = 0 ")
    List<CropRotation> getCropsPorSubir();

    @Query("UPDATE crop_rotation SET estado_subida_crop_rotation = 1, cabecera_crop = :cabecera WHERE estado_subida_crop_rotation = 0;")
    int updateCropsSubidos(int cabecera);

    @Query("UPDATE crop_rotation SET estado_subida_crop_rotation = 0 WHERE cabecera_crop = :cabecera ")
    int updateCropsBack(int cabecera);

    @Update
    void updateCrop(CropRotation cropRotation);

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


    @Query("DELETE FROM detalle_visita_prop WHERE estado_detalle = 0 AND id_visita_detalle = :visita ")
    void deleteDetallesByVisita(int visita);

    @Query("DELETE FROM fotos WHERE estado_fotos = 0 AND id_visita_foto = :visita ")
    void deleteFotosByVisita(int visita);

    @Query(" DELETE FROM visita WHERE estado_server_visitas = 0 AND id_visita = :visita ")
    void deleteVisita(int visita);


    /* DETALLE*/
    @Query("DELETE FROM detalle_visita_prop WHERE estado_detalle = 1")
    void deleteDetalle();




    @Query("UPDATE detalle_visita_prop SET estado_detalle = 1, cabecera_detalle = :idCab WHERE estado_detalle = 0")
    int updateDetalleVisitaSubidas(int idCab);

    @Query("UPDATE detalle_visita_prop SET estado_detalle = 1, tomada_detalle = 0,  cabecera_detalle = :idCab WHERE estado_detalle = 0 AND tomada_detalle = 1 ; ")
    int updateDetalleVisitaSubidasTomadas(int idCab);


    @Query("UPDATE detalle_visita_prop SET estado_detalle = 0, cabecera_detalle = 0 , tomada_detalle = 0 WHERE cabecera_detalle = :idCab")
    int updateDetalleVisitaBack(int idCab);

    @Query("SELECT * FROM detalle_visita_prop WHERE estado_detalle = 0 AND id_visita_detalle > 0 AND id_visita_detalle = :id_visita ")
    List<detalle_visita_prop> getDetallesPorSubirLimit(int id_visita);

    @Query("UPDATE detalle_visita_prop SET tomada_detalle = 1 WHERE estado_detalle = 0 AND id_visita_detalle = :id_visita ")
    void marcarDetalle(int id_visita);


    @Query("SELECT * FROM detalle_visita_prop WHERE estado_detalle = 0 AND id_visita_detalle > 0")
    List<detalle_visita_prop> getDetallesPorSubir();

    @Query("SELECT * FROM detalle_visita_prop WHERE estado_detalle = 0 AND tomada_detalle = 1 ")
    List<detalle_visita_prop> getDetallesPorSubirTomadas();
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
            "LEFT JOIN visita V ON (V.id_visita = detalle_visita_prop.id_visita_detalle) " +
            "LEFT JOIN pro_cli_mat ON (pro_cli_mat.id_prop_mat_cli = detalle_visita_prop.id_prop_mat_cli_detalle) " +
            "WHERE (V.id_anexo_visita = :idAnexo OR detalle_visita_prop.id_visita_detalle = 0) AND pro_cli_mat.id_prop = :idProp " +
            "GROUP BY id_visita " +
            "ORDER BY V.id_anexo_visita, detalle_visita_prop.id_prop_mat_cli_detalle ASC")
    List<String> getDetalleCampo(String idAnexo, int idProp);

    /* ===================================================================================*/


    /* USUARIO */

    @Query("DELETE FROM usuarios ")
    void deleteUsuario();

    @Insert
    List<Long> setUsuarios(List<Usuario> usuarios);

    @Query("SELECT  * FROM usuarios U WHERE U.user = :user AND U.pass = :pass")
    Usuario getUsuarioLogin(String user, String pass);

    @Query("SELECT * FROM usuarios WHERE id_usuario = :idUsuario")
    Usuario getUsuarioById(int idUsuario);

    @Query("SELECT * FROM usuarios WHERE tipo_usuario != 5 ORDER BY usuarios.nombre, usuarios.apellido_p ASC ")
    List<Usuario> getUsuarioById();

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

    @Query("UPDATE config SET id = :id WHERE id = 0")
    int updateConfig(int id);

    /* ======================================================================================== */


    /* pro_cli_mat  */
    @Insert
    List<Long> insertInterfaz(List<pro_cli_mat> pro_cli_mats);

    @Query("DELETE FROM pro_cli_mat")
    void deleteProCliMat();

    @Query("SELECT * FROM cli_pcm CCPM INNER JOIN pro_cli_mat PCM USING(id_prop_mat_cli) " +
            "WHERE PCM.id_materiales = :idMaterial AND PCM.id_etapa = :idEtapa AND CCPM.id_cli = :idCli AND CCPM.registrar = 1 AND PCM.id_tempo = :idTempo ORDER BY  orden ASC ")
    List<pro_cli_mat> getProCliMatByMateriales(String idMaterial, int idEtapa, int idCli, String idTempo);

    @Query("SELECT  * FROM cli_pcm CCPM INNER JOIN pro_cli_mat PCM USING(id_prop_mat_cli) " +
            "WHERE PCM.id_prop = :idProp AND PCM.id_materiales = :idMat AND CCPM.id_cli = :idCli AND CCPM.registrar = 1 AND PCM.id_tempo = :idTempo ORDER BY  orden ASC")
    List<pro_cli_mat> getProCliMatByIdProp(int idProp, String idMat, int idCli, String idTempo);

    @Query("SELECT  * FROM cli_pcm CCPM INNER JOIN pro_cli_mat PCM USING(id_prop_mat_cli) " +
            "WHERE PCM.id_prop_mat_cli = :idProp AND CCPM.id_cli = :idCli AND CCPM.registrar = 1 AND PCM.id_tempo = :idTempo ")
    pro_cli_mat getProCliMatByIdProp(int idProp, int idCli, String idTempo);



    @Query("SELECT id_clientes_tabla FROM cliente C " +
            "INNER JOIN quotation Q ON Q.cliente = C.id_clientes_tabla " +
            "INNER JOIN materiales M ON Q.id_materiales = M.id_variedad " +
            "INNER JOIN anexo_contrato  AC ON AC.id_variedad_anexo = M.id_variedad " +
            "WHERE AC.id_anexo_contrato = :idAnexo LIMIT 1" )
    int getIdClienteByAnexo(String idAnexo);

    @Query("SELECT AC.id_variedad_anexo FROM anexo_contrato AC " +
            "INNER JOIN materiales M ON AC.id_variedad_anexo = M.id_variedad WHERE AC.id_anexo_contrato = :idAnexo LIMIT 1" )
    String getIdMaterialByAnexo(String idAnexo);

    @Query("SELECT F.id_pred_new FROM ficha_new F " +
            "INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = F.id_ficha_new WHERE AC.id_anexo_contrato = :idPredio LIMIT 1" )
    int getIdPredioByAnexo(String idPredio);

    @Query("SELECT F.id_lote_new FROM ficha_new F " +
            "INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = F.id_ficha_new WHERE AC.id_anexo_contrato = :idLote LIMIT 1" )
    int getIdLoteByAnexo(String idLote);

    @Query("SELECT v.id_visita FROM visita V " +
            "INNER JOIN anexo_contrato AC ON AC.id_anexo_contrato = V.id_anexo_visita WHERE AC.id_anexo_contrato = :idLote ORDER BY id_visita DESC LIMIT 1" )
    int getIdVisitaByAnexo(String idLote);

    @Query("SELECT F.id_tipo_suelo_new FROM ficha_new F " +
            "INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = F.id_ficha_new WHERE AC.id_anexo_contrato = :idAnexo LIMIT 1" )
    int getIdTipoSueloByAnexo(String idAnexo);

    @Query("SELECT F.id_tipo_riego_new FROM ficha_new F " +
            "INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = F.id_ficha_new WHERE AC.id_anexo_contrato = :idAnexo LIMIT 1" )
    int getIdTipoRiegoByAnexo(String idAnexo);

    @Query("SELECT F.id_usuario_new FROM ficha_new F " +
            "INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = F.id_ficha_new WHERE AC.id_anexo_contrato = :idAnexo LIMIT 1" )
    int getIdusuarioByAnexo(String idAnexo);


    /* ===================================================================================*/

    /* temporadas */
    @Insert
    List<Long> insertTemporada(List<Temporada> temporadas);

    @Query("SELECT  * FROM temporada ")
    List<Temporada> getTemporada();

    @Query("DELETE FROM temporada ")
    void deleteTemporadas();

    @Insert
    List<Long> insertPCM(List<cli_pcm> cli_pcms);
    @Query("SELECT  * FROM cli_pcm ")
    List<cli_pcm> getCliPCM();

    @Query("DELETE FROM cli_pcm ")
    void deleteCliPCM();


    /* =====================================================================*/

    /* UM */
    @Query("SELECT * FROM unidad_medida")
    List<UnidadMedida> getUM();

    @Query("DELETE FROM unidad_medida")
    void deleteUM();

    @Insert
    List<Long> insertUM(List<UnidadMedida> unidadMedidas);

    /* QUOTATIONS */
    @Query("SELECT * FROM quotation")
    List<quotation> getQuotation();

    @Query("DELETE FROM quotation")
    void deleteQuotation();

    @Insert
    List<Long> insertQuotation(List<quotation> quotations);

    /*============================================================================*/


    /* ANEXOS*/

    @Query("SELECT  * , C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna " +
            "FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN materiales ON (materiales.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "LEFT JOIN ficha_new F ON (F.id_ficha_new = anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "LEFT JOIN predio P ON (P.id_pred = F.id_pred_new) " +
            "LEFT JOIN lote ON (lote.lote = F.id_lote_new) " +
            "ORDER BY especie.desc_especie ASC ")
    List<AnexoCompleto> getAnexos();


    @Query("SELECT * FROM anexo_contrato WHERE id_anexo_contrato = :idAnexo ORDER BY anexo_contrato.num_anexo ASC ")
    AnexoContrato getAnexos(String idAnexo);

    @Query("SELECT *, C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna " +
            "FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN materiales ON (materiales.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "LEFT JOIN ficha_new F ON (F.id_ficha_new = anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "LEFT JOIN predio P ON (P.id_pred = F.id_pred_new) " +
            "LEFT JOIN lote ON (lote.lote = F.id_lote_new) " +
            "WHERE F.id_tempo_new = :year ORDER BY especie.desc_especie ASC ")
    List<AnexoCompleto> getAnexosByYear(String year);



    @Query("SELECT *, C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna " +
            "FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN materiales ON (materiales.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "LEFT JOIN ficha_new F ON (F.id_ficha_new = anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "LEFT JOIN predio P ON (P.id_pred = F.id_pred_new) " +
            "LEFT JOIN lote ON (lote.lote = F.id_lote_new) " +
            "WHERE id_anexo_contrato = :idAnexo ORDER BY especie.desc_especie ASC ")
    AnexoCompleto getAnexoCompletoById(String idAnexo);

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

    @Query("SELECT * FROM especie WHERE id_especie = :idEspecie")
    Especie getEspecie(String idEspecie);

    @Insert
    void insertEspecie(Especie especie);
    @Insert
    List<Long> insertEspecie(List<Especie> especie);

    @Query("DELETE FROM especie")
    void deleteEspecie();

    /* ================================================================== */


    /*VARIEDAD*/
    @Query("SELECT  * FROM materiales")
    List<Variedad> getVariedades();

    @Query("SELECT * FROM materiales WHERE id_especie_variedad = :idEspecie")
    List<Variedad> getVariedadesByEspecie(String idEspecie);

    @Insert
    void insertVariedad(Variedad materiales);
    @Insert
    List<Long> insertVariedad(List<Variedad> materiales);

    @Query("DELETE FROM materiales")
    void deleteVariedad();

    /* ====================================================================== */


    /*CLIENTES*/

    @Query("SELECT  * FROM cliente")
    List<Clientes> getClientes();

    @Insert
    void insertClientes(Clientes clientes);
    @Insert
    List<Long> insertClientes(List<Clientes> clientes);

    @Query("DELETE FROM cliente")
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

    @Query("SELECT * FROM predio ")
    List<Predios> getPredios();

    @Query("DELETE FROM predio")
    void deletePredios();

    /* ===================================================================================*/


    /* agri pred temp */
    @Insert
    List<Long> insertAgriPredTemp(List<AgrPredTemp> agris);

    @Query("SELECT * FROM agri_pred_temp ")
    List<AgrPredTemp> getAgripredTemp();

    @Query("DELETE FROM agri_pred_temp")
    void deleteAgriPredTemp();

    /* ===================================================================================*/




    /* TIPO RIEGO */
    @Insert
    List<Long> insertTipoRiego(List<TipoRiego> tipoRiegos);

    @Query("SELECT * FROM tipo_riego ")
    List<TipoRiego> getTipoRiego();

    @Query("SELECT * FROM tipo_riego WHERE id_tipo_riego = :id")
    TipoRiego getTipoRiegoById(String id);

    @Query("SELECT * FROM tipo_riego WHERE vigencia = 'SI' ")
    List<TipoRiego> getTipoRiegoVigente();

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
