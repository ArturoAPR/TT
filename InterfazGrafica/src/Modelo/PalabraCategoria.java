/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Alejandro
 */
public class PalabraCategoria {
    private Palabra palabra;
    private Categoria categoria;
    private Double importaciona;
    
    PalabraCategoria(){
    
    }

    public Palabra getPalabra() {
        return palabra;
    }

    public void setPalabra(Palabra palabra) {
        this.palabra = palabra;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Double getImportaciona() {
        return importaciona;
    }

    public void setImportaciona(Double importaciona) {
        this.importaciona = importaciona;
    }
    
    
    
}
