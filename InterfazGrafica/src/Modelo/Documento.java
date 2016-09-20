package Modelo;

import java.util.HashMap;


public class Documento {
    private String nombre;
    private String rutaReal;
    private String fechaModificacion;//falta checar el tipo de dato
    private int idDocumento;
    private String rutaVirtual;
    HashMap<String, Double> vectorspacemodel;

    public Documento(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRutaReal() {
        return rutaReal;
    }

    public void setRutaReal(String rutaReal) {
        this.rutaReal = rutaReal;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getRutaVirtual() {
        return rutaVirtual;
    }

    public void setRutaVirtual(String rutaVirtual) {
        this.rutaVirtual = rutaVirtual;
    }
    
}
