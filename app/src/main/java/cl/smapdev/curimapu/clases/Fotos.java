package cl.smapdev.curimapu.clases;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


//https://github.com/alexvasilkov/GestureViews
@Entity(tableName = "fotos")
public class Fotos {


    @PrimaryKey(autoGenerate = true)
    private int id_foto;
    private int id_ficha;
    private String nombre_foto;
    private int fieldbook; /*0: visit form, 1: resumen, 2:siembra, 3:floarcion, 4:cosecha */
    private int vista; /*0: cliente, 1: interna 2: agronomo*/
    private int plano; /* 0: general, 1: detalle*/
    private String fecha;
    private String hora;
    private boolean favorita;




    public int getId_foto() {
        return id_foto;
    }

    public void setId_foto(int id_foto) {
        this.id_foto = id_foto;
    }

    public int getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(int id_ficha) {
        this.id_ficha = id_ficha;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public int getFieldbook() {
        return fieldbook;
    }

    public void setFieldbook(int fieldbook) {
        this.fieldbook = fieldbook;
    }

    public int getVista() {
        return vista;
    }

    public void setVista(int vista) {
        this.vista = vista;
    }

    public int getPlano() {
        return plano;
    }

    public void setPlano(int plano) {
        this.plano = plano;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isFavorita() {
        return favorita;
    }

    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }
}
