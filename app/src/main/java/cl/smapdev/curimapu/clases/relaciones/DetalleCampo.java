package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;

public class DetalleCampo {

    @Embedded
    private detalle_visita_prop detalle_visita_prop;

    @Embedded
    private pro_cli_mat pro_cli_mat;

    public cl.smapdev.curimapu.clases.tablas.detalle_visita_prop getDetalle_visita_prop() {
        return detalle_visita_prop;
    }

    public void setDetalle_visita_prop(cl.smapdev.curimapu.clases.tablas.detalle_visita_prop detalle_visita_prop) {
        this.detalle_visita_prop = detalle_visita_prop;
    }

    public cl.smapdev.curimapu.clases.tablas.pro_cli_mat getPro_cli_mat() {
        return pro_cli_mat;
    }

    public void setPro_cli_mat(cl.smapdev.curimapu.clases.tablas.pro_cli_mat pro_cli_mat) {
        this.pro_cli_mat = pro_cli_mat;
    }
}
