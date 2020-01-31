package cl.smapdev.curimapu.clases.bd;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import cl.smapdev.curimapu.clases.Fotos;


@Database(entities = { Fotos.class }, version = 1)
public abstract class MyAppBD extends RoomDatabase {

        public abstract MyDao myDao();

}
