package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;

@Dao
public interface DaoPrimeraPrioridad {
    @Query("SELECT * FROM anexo_contrato " +
            "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
            "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
            "INNER JOIN materiales ON (materiales.id_variedad = anexo_contrato.id_variedad_anexo) " +
            "LEFT JOIN ficha_new F ON (F.id_ficha_new = anexo_contrato.id_ficha_contrato) " +
            "INNER JOIN comuna C ON (C.id_comuna = F.id_comuna_new ) " +
            "LEFT JOIN predio P ON (P.id_pred = F.id_pred_new) " +
            "LEFT JOIN lote ON (lote.lote = F.id_lote_new) " +
            "WHERE temporada_anexo = :temporada AND entra_primera_prioridad = 1 ORDER BY fechaUltimaVisitaPP ASC")
    List<AnexoCompleto> getPPByTemporada(int temporada);
}
