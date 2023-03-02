package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;

@Dao
public interface DaoAnexosFechas {

    @Query("SELECT * FROM anexo_correo_fechas WHERE id_ac_corr_fech = :id_anexo; ")
    AnexoCorreoFechas getAnexoCorreoFechasByAnexo(int id_anexo);

    @Query("SELECT * FROM anexo_correo_fechas WHERE " +
            "(correo_inicio_despano <= 0 AND (inicio_despano IS NOT NULL AND inicio_despano != '0000-00-00')) OR  " +
            "(correo_cinco_porciento_floracion <= 0 AND( cinco_porciento_floracion IS NOT NULL AND cinco_porciento_floracion != '0000-00-00')) OR " +
            "(correo_inicio_corte_seda <= 0 AND (inicio_corte_seda IS NOT NULL AND inicio_corte_seda != '0000-00-00')) OR " +
            "(correo_inicio_cosecha <= 0 AND (inicio_cosecha IS NOT NULL AND inicio_cosecha != '0000-00-00')) OR " +
            "(correo_termino_cosecha <= 0 AND (termino_cosecha IS NOT NULL AND termino_cosecha != '0000-00-00')) OR " +
            "(correo_termino_labores_post_cosechas <= 0 AND (termino_labores_post_cosechas IS NOT NULL AND termino_labores_post_cosechas != '0000-00-00'))")
    List<AnexoCorreoFechas> getAnexoCorreoFechas();


    @Query("SELECT * FROM anexo_correo_fechas WHERE estado_sincro_corr_fech = 0;")
    List<AnexoCorreoFechas> getAnexoCorreosFechasSincro();


    @Update
    int UpdateFechasAnexos(AnexoCorreoFechas anexoCorreoFechas);

    @Insert
    long insertFechasAnexos(AnexoCorreoFechas anexoCorreoFechas);

    @Query("SELECT " +
            "ACF.* , " +
            "AC.num_anexo, " +
            "U.user as usu_user, " +
            "A.razon_social, " +
            "L.nombre_lote, " +
            "P.nombre, " +
            "M.desc_variedad, " +
            "E.desc_especie, " +
            "AC.id_anexo_contrato, " +
            "C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna, " +
            "V.fecha_visita, " +
            "AC.condicion " +
            "FROM anexo_contrato AC  " +
            " LEFT JOIN anexo_correo_fechas ACF  ON (AC.id_anexo_contrato = ACF.id_ac_corr_fech) " +
            " LEFT JOIN usuarios U ON U.id_usuario = ACF.id_fieldman " +
            " LEFT JOIN agricultor A ON (A.id_agricultor = AC.id_agricultor_anexo) " +
            " LEFT JOIN lote L ON (L.lote = AC.id_potrero) " +
            " LEFT JOIN predio P ON (P.id_pred = L.id_pred_lote) " +
            " LEFT JOIN ficha_new FN ON (FN.id_ficha_new = AC.id_ficha_contrato) " +
            " LEFT JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo) " +
            " LEFT JOIN especie E ON (E.id_especie = M.id_especie_variedad)" +
            " LEFT JOIN comuna C ON (C.id_comuna = FN.id_comuna_new )" +
            " LEFT JOIN visita V ON (V.id_anexo_visita = AC.id_anexo_contrato) " +
            " WHERE FN.id_tempo_new = :tempo " +
            " GROUP BY AC.id_anexo_contrato " +
            " ORDER BY E.desc_especie ASC ")

    List<AnexoWithDates> getFechasSag(String tempo);


    @Query("SELECT " +
            "ACF.* , " +
            "AC.num_anexo, " +
            "U.user as usu_user, " +
            "A.razon_social, " +
            "L.nombre_lote, " +
            "P.nombre, " +
            "M.desc_variedad, " +
            "E.desc_especie, " +
            "AC.id_anexo_contrato, " +
            "C.desc_comuna as c_desc_comuna, C.id_comuna AS c_id_comuna, C.id_api AS c_id_api, C.id_provincia_comuna AS c_id_provincia_comuna, " +
            "V.fecha_visita, " +
            "AC.condicion " +
            "FROM anexo_contrato AC  " +
            " LEFT JOIN anexo_correo_fechas ACF  ON (AC.id_anexo_contrato = ACF.id_ac_corr_fech) " +
            " LEFT JOIN usuarios U ON U.id_usuario = ACF.id_fieldman " +
            " LEFT JOIN agricultor A ON (A.id_agricultor = AC.id_agricultor_anexo) " +
            " LEFT JOIN lote L ON (L.lote = AC.id_potrero) " +
            " LEFT JOIN predio P ON (P.id_pred = L.id_pred_lote) " +
            " LEFT JOIN ficha_new FN ON (FN.id_ficha_new = AC.id_ficha_contrato) " +
            " LEFT JOIN materiales M ON (M.id_variedad = AC.id_variedad_anexo) " +
            " LEFT JOIN especie E ON (E.id_especie = M.id_especie_variedad)" +
            " LEFT JOIN comuna C ON (C.id_comuna = FN.id_comuna_new )" +
            " LEFT JOIN visita V ON (V.id_anexo_visita = AC.id_anexo_contrato) " +
            " WHERE FN.id_tempo_new = :tempo AND " +
            " ( AC.num_anexo LIKE :query OR  A.razon_social LIKE :query OR L.nombre_lote LIKE :query OR C.desc_comuna LIKE :query)" +
            " GROUP BY AC.id_anexo_contrato " +
            " ORDER BY E.desc_especie ASC ")
    List<AnexoWithDates> getFechasSag(String tempo, String query);

}
