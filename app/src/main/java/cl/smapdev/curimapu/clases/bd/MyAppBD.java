package cl.smapdev.curimapu.clases.bd;



import androidx.room.Database;
import androidx.room.RoomDatabase;

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
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.temporales.TempFlowering;
import cl.smapdev.curimapu.clases.temporales.TempHarvest;
import cl.smapdev.curimapu.clases.temporales.TempSowing;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;


@Database(entities = {
        Fotos.class, Fichas.class, Agricultor.class, Region.class, Comuna.class, AnexoContrato.class,
        Especie.class, Variedad.class, TempVisitas.class, TempFlowering.class, TempHarvest.class, TempSowing.class,
        Visitas.class, Sowing.class, Flowering.class, Harvest.class, CropRotation.class, pro_cli_mat.class, Temporada.class
        }, version = 1)
public abstract class MyAppBD extends RoomDatabase {

        public abstract MyDao myDao();

}
