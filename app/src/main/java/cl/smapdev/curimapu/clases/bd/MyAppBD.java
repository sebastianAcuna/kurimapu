package cl.smapdev.curimapu.clases.bd;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cl.smapdev.curimapu.clases.bd.daos.DaoAnexosFechas;
import cl.smapdev.curimapu.clases.bd.daos.DaoCheckListCapSiembra;
import cl.smapdev.curimapu.clases.bd.daos.DaoCheckListCosecha;
import cl.smapdev.curimapu.clases.bd.daos.DaoCheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.bd.daos.DaoCheckListSiembra;
import cl.smapdev.curimapu.clases.bd.daos.DaoChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.bd.daos.DaoEstacionFloracion;
import cl.smapdev.curimapu.clases.bd.daos.DaoEvaluaciones;
import cl.smapdev.curimapu.clases.bd.daos.DaoFirmasTemp;
import cl.smapdev.curimapu.clases.bd.daos.DaoMuestraHumedad;
import cl.smapdev.curimapu.clases.bd.daos.DaoPrimeraPrioridad;
import cl.smapdev.curimapu.clases.bd.daos.DaoSitiosNoVisitados;
import cl.smapdev.curimapu.clases.bd.daos.DaoVilab;
import cl.smapdev.curimapu.clases.bd.daos.MyDao;
import cl.smapdev.curimapu.clases.tablas.AgrPredTemp;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.AnexoVilab;
import cl.smapdev.curimapu.clases.tablas.CardViewsResumen;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.tablas.Clientes;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.FichaMaquinaria;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.FichasNew;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;
import cl.smapdev.curimapu.clases.tablas.Lotes;
import cl.smapdev.curimapu.clases.tablas.Maquinaria;
import cl.smapdev.curimapu.clases.tablas.MuestraHumedad;
import cl.smapdev.curimapu.clases.tablas.Predios;
import cl.smapdev.curimapu.clases.tablas.PrimeraPrioridad;
import cl.smapdev.curimapu.clases.tablas.Provincia;
import cl.smapdev.curimapu.clases.tablas.Region;
import cl.smapdev.curimapu.clases.tablas.SitiosNoVisitados;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.TipoRiego;
import cl.smapdev.curimapu.clases.tablas.TipoSuelo;
import cl.smapdev.curimapu.clases.tablas.TipoTenenciaMaquinaria;
import cl.smapdev.curimapu.clases.tablas.TipoTenenciaTerreno;
import cl.smapdev.curimapu.clases.tablas.UnidadMedida;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.cli_pcm;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.tablas.quotation;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;


@Database(entities = {
        Fotos.class, Fichas.class, Agricultor.class, Region.class, Comuna.class, AnexoContrato.class,
        Especie.class, Variedad.class, TempVisitas.class, UnidadMedida.class, Usuario.class,
        Config.class, Visitas.class, CropRotation.class, pro_cli_mat.class, Temporada.class,
        detalle_visita_prop.class, Provincia.class, Errores.class, Lotes.class, Predios.class,
        TipoRiego.class, TipoSuelo.class, Maquinaria.class, TipoTenenciaMaquinaria.class,
        TipoTenenciaTerreno.class, FichaMaquinaria.class, Clientes.class, CardViewsResumen.class,
        cli_pcm.class, quotation.class, FotosFichas.class, AgrPredTemp.class, FichasNew.class,
        AnexoCorreoFechas.class, Evaluaciones.class, CheckListSiembra.class, TempFirmas.class,
        CheckListCosecha.class, CheckListCapacitacionSiembra.class,
        CheckListCapacitacionSiembraDetalle.class, CheckListLimpiezaCamiones.class,
        ChecklistLimpiezaCamionesDetalle.class, ChecklistDevolucionSemilla.class, AnexoVilab.class,
        EstacionFloracion.class, EstacionFloracionDetalle.class, EstacionFloracionEstaciones.class, MuestraHumedad.class,
        PrimeraPrioridad.class, SitiosNoVisitados.class
}, version = 17)
public abstract class MyAppBD extends RoomDatabase {
    public abstract MyDao myDao();

    public abstract DaoEvaluaciones DaoEvaluaciones();

    public abstract DaoCheckListSiembra DaoClSiembra();

    public abstract DaoFirmasTemp DaoFirmas();

    public abstract DaoAnexosFechas DaoAnexosFechas();

    public abstract DaoCheckListCapSiembra DaoCheckListCapSiembra();

    public abstract DaoCheckListCosecha DaoCheckListCosecha();

    public abstract DaoCheckListLimpiezaCamiones DaoCheckListLimpiezaCamiones();

    public abstract DaoChecklistDevolucionSemilla DaoCheckListDevolucionSemilla();

    public abstract DaoVilab DaoVilab();

    public abstract DaoEstacionFloracion DaoEstacionFloracion();

    public abstract DaoMuestraHumedad DaoMuestraHumedad();

    public abstract DaoPrimeraPrioridad DaoPrimeraPrioridad();

    public abstract DaoSitiosNoVisitados DaoSitiosNoVisitados();

}
