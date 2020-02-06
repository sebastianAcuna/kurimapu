package cl.smapdev.curimapu.clases.bd;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import cl.smapdev.curimapu.Variedad;
import cl.smapdev.curimapu.clases.Agricultor;
import cl.smapdev.curimapu.clases.AnexoContrato;
import cl.smapdev.curimapu.clases.Comuna;
import cl.smapdev.curimapu.clases.Especie;
import cl.smapdev.curimapu.clases.Fichas;
import cl.smapdev.curimapu.clases.Fotos;
import cl.smapdev.curimapu.clases.Region;


@Database(entities = { Fotos.class, Fichas.class, Agricultor.class, Region.class, Comuna.class, AnexoContrato.class, Especie.class, Variedad.class}, version = 1)
public abstract class MyAppBD extends RoomDatabase {

        public abstract MyDao myDao();

}
