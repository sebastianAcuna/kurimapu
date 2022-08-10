package cl.smapdev.curimapu.clases.bd.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import cl.smapdev.curimapu.clases.temporales.TempFirmas;

@Dao
public interface DaoFirmasTemp {

    @Query("DELETE FROM temp_firmas WHERE tipo_documento = :tipo_documento")
    void deleteFirmasByDoc(int tipo_documento);

    @Insert
    long  insertFirma(TempFirmas tempFirmas);


    @Query("SELECT * FROM temp_firmas WHERE tipo_documento = :tipo_documento")
    List<TempFirmas> getFirmasByDocum(int tipo_documento);


}
