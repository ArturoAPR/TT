/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Buscador;
import java.io.File;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Alejandro
 */
public class Controlador {
    //private Algoritmo algoritmo;
    private Buscador buscador;
    private Coleccion coleccion;
    private FileSystemView vistaSistema;

    public Controlador() {
        coleccion = new Coleccion();
        
        vistaSistema = FileSystemView.getFileSystemView();
    }
    
    public ArrayList<File> realizaBusqueda(String termino,int tipo,int ext){        
        buscador = new Buscador(termino,tipo,ext);
        return buscador.buscar();        
    }
    public DefaultMutableTreeNode solicitarSugerencia(ArrayList<File> archivos){
    
        return null;
    }
    public boolean solicitarOrganizacionFisica(boolean opcion){
    
        return false;
    }
    public int gestionarColeccion(File file){
        
        if(coleccion.esDirectorio(file)){              
            coleccion.agregarCarpeta(vistaSistema.getFiles(file, true)); 
            return 0;
        }else if(coleccion.esArchivo(file)){
            return coleccion.agregarArchivo(file);                
        }else{
            return -1;
        }               
    }
    public ArrayList<File> retornarColeccion(){
        return coleccion.obtenerColeccionArchivos();
    }    
    
}
