package Modelo;

public class DocumentoPalabra {
    private Documento documento;
    private Palabra palabra;

    public DocumentoPalabra(Documento documento, Palabra palabra, double frecuencia) {
        this.documento = documento;
        this.palabra = palabra;
        this.frecuencia = frecuencia;
    }
    private double frecuencia;
    private boolean tipoPrincipal;

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Palabra getPalabra() {
        return palabra;
    }

    public void setPalabra(Palabra palabra) {
        this.palabra = palabra;
    }

    public double getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(double frecuencia) {
        this.frecuencia = frecuencia;
    }

    public boolean isTipoPrincipal() {
        return tipoPrincipal;
    }

    public void setTipoPrincipal(boolean tipoPrincipal) {
        this.tipoPrincipal = tipoPrincipal;
    }
}
