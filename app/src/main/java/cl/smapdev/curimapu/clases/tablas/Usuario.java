package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "usuarios")
public class Usuario {


    @PrimaryKey
    @SerializedName("id_usuario")
    @Expose
    private int id_usuario;

    @SerializedName("rut")
    @Expose
    private String rut_usuario;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("apellido_p")
    @Expose
    private String apellido_p;


    @SerializedName("apellido_m")
    @Expose
    private String apellido_m;

    @SerializedName("telefono")
    @Expose
    private String telefono;


    @SerializedName("id_region")
    @Expose
    private int id_region;

    @SerializedName("id_pais")
    @Expose
    private int id_pais;

    @SerializedName("id_comuna")
    @Expose
    private int id_comuna;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("fieldman")
    @Expose
    private String fieldman;

    @SerializedName("supervisa_otro")
    @Expose
    private String supervisa_otro;

    @SerializedName("pass")
    @Expose
    private String pass;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("tipo_usuario")
    @Expose
    private int tipo_usuario;

    public int getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(int tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getRut_usuario() {
        return rut_usuario;
    }

    public void setRut_usuario(String rut_usuario) {
        this.rut_usuario = rut_usuario;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_p() {
        return apellido_p;
    }

    public void setApellido_p(String apellido_p) {
        this.apellido_p = apellido_p;
    }

    public String getApellido_m() {
        return apellido_m;
    }

    public void setApellido_m(String apellido_m) {
        this.apellido_m = apellido_m;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getId_region() {
        return id_region;
    }

    public void setId_region(int id_region) {
        this.id_region = id_region;
    }

    public int getId_pais() {
        return id_pais;
    }

    public void setId_pais(int id_pais) {
        this.id_pais = id_pais;
    }

    public int getId_comuna() {
        return id_comuna;
    }

    public void setId_comuna(int id_comuna) {
        this.id_comuna = id_comuna;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFieldman() {
        return fieldman;
    }

    public void setFieldman(String fieldman) {
        this.fieldman = fieldman;
    }

    public String getSupervisa_otro() {
        return supervisa_otro;
    }

    public void setSupervisa_otro(String supervisa_otro) {
        this.supervisa_otro = supervisa_otro;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
