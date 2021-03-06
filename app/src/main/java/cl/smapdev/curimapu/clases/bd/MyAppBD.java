package cl.smapdev.curimapu.clases.bd;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cl.smapdev.curimapu.clases.tablas.CardViewsResumen;
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
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.cli_pcm;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.tablas.quotation;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;


@Database(entities = {
        Fotos.class, Fichas.class, Agricultor.class, Region.class, Comuna.class, AnexoContrato.class,
        Especie.class, Variedad.class, TempVisitas.class,  UnidadMedida.class, Usuario.class, Config.class,
        Visitas.class, CropRotation.class, pro_cli_mat.class, Temporada.class, detalle_visita_prop.class, Provincia.class, Errores.class,
        Lotes.class, Predios.class, TipoRiego.class, TipoSuelo.class, Maquinaria.class, TipoTenenciaMaquinaria.class, TipoTenenciaTerreno.class,
        FichaMaquinaria.class, Clientes.class, CardViewsResumen.class, cli_pcm.class, quotation.class
        }, version = 1)
public abstract class MyAppBD extends RoomDatabase {

        public abstract MyDao myDao();

}
