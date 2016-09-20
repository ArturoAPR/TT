/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BKmeans;

import ca.pfv.spmf.patterns.cluster.DoubleArray;

/**
 *
 * @author Alejandro
 */
public class DoubleArrayName extends DoubleArray{
    
    private String nombreArchivo;
    
    public DoubleArrayName(double[] data) {
        super(data);
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
}
