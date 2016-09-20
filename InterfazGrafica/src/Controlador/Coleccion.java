/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Vista.InterfazPrincipal;
import java.io.File;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;


/**
 *
 * @author Alejandro
 */

public class Coleccion {
    private ArrayList<File> seleccion;
    private FileSystemView vistaSistema;

    public Coleccion() {
        seleccion = new ArrayList<File>();
        vistaSistema = FileSystemView.getFileSystemView();
    }
    
    public boolean validaRuta(File archivo){
        return archivo.getAbsolutePath().contains(System.getProperty("user.home"));
    }
    public boolean esArchivo(File archivo){
        return archivo.isFile();        
    }
    public boolean esDirectorio(File archivo){
        return archivo.isDirectory();
    }
    public boolean esArchivoValido(File archivo){
        String type = archivo.getName();
        type=type.toLowerCase();
        
        if (archivo.canRead() && !archivo.isHidden()) {
            if  (   type.endsWith(".pdf")|| //pdf
                    type.endsWith(".txt")|| //txt                                                           
                    type.endsWith(".rtf")|| 
                    type.endsWith(".ppt")|| //rtf
                    type.endsWith(".doc")|| // type.contains("Microsoft PowerPoint 97-2003 Presentation")|| //ppt
                    type.endsWith(".docx")
                 ){                                             
                    return true;                
            }
        }                                 
        return false;
    }
    public int agregarArchivo(File archivo){           
            if (validaRuta(archivo) && esArchivoValido(archivo) && esArchivo(archivo)) { 
                InterfazPrincipal.labelStatus.setText("Agregando archivo: "+ archivo.getName());
                añadirArchivo(archivo);                
            } else {
                return 1;
            }
        return 0;
    }
    public void agregarCarpeta(File[] archivos){
        for (int i = 0; i < archivos.length; i++) {
            File[] f = vistaSistema.getFiles(archivos[i], true);
            if (validaRuta(archivos[i])){
                agregarCarpeta(f);
                if(esArchivoValido(archivos[i]) && esArchivo(archivos[i])) {
                    añadirArchivo(archivos[i]);
                }
            } 
        }
        
    }
    public void limpiarColeccion(){
        seleccion.clear();
    }
    public boolean obtenerArchivos(ArrayList<File> archivos){
        return false;
    }
    public boolean descartarArchivos(ArrayList<File> archivos){
        return false;
    }
    public boolean estaVacio(){
        return false;       
    }
    public boolean existeArchivo(){
        return false;
    }
    public ArrayList<File> obtenerColeccionArchivos(){
        return seleccion;
    }
    public void añadirArchivo(File archivo){
        seleccion.add(archivo);;
    }
    
    
}
