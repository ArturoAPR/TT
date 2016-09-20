/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class Buscador {
    private String termino;
    private int tipo;
    private int extension;
    private Conexion conexion;
    ArrayList<File> resulBusqueda;

    public Buscador(String termino,int tipo,int extension) {
        this.termino = termino;
        this.tipo = tipo;
        this.extension =  extension;
        conexion=new Conexion("bdtt","root","root","localhost");
        resulBusqueda = new ArrayList<File>();
    }
    public ArrayList<File> buscar(){
        if(tipo==1)
            return buscarContenido();
        else if (tipo==2)
            return buscarNombre();
        else if (tipo==3)
            return buscarExtension();
        else 
            return null;
    }
    
    public ArrayList<File> buscarExtension(){        
        String ext="";
        switch (extension) {
            case 0:
                ext = "pdf";
                break;
            case 1:
                ext = "ppt";
                break;
            case 2:
                ext = "doc";
                break;
            case 3:
                ext = "txt";
                break;

        }
        try {
            conexion.conectar();
            ResultSet rs=conexion.consulta("select distinct rutaDocumentoReal from documento where nombreDocumento like \"%"+ext+"%\"");
            while (rs.next()) {
                resulBusqueda.add(new File(rs.getString("rutaDocumentoReal")));
            }
            conexion.desconectar();
            return resulBusqueda;
        } catch (SQLException ex) {
            Logger.getLogger(Buscador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        
        
    }
    public ArrayList<File> buscarNombre() {
        try {
            conexion.conectar();
            ResultSet rs=conexion.consulta("select distinct d.rutaDocumentoReal from documento d where nombreDocumento like \"%"+termino+"%\"");
            while (rs.next()) {
                resulBusqueda.add(new File(rs.getString("rutaDocumentoReal")));
            }
            conexion.desconectar();
            return resulBusqueda;
        } catch (SQLException ex) {
            Logger.getLogger(Buscador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
    
    public ArrayList<File> buscarContenido(){
        try {
            conexion.conectar();
            ResultSet rs = conexion.consulta("select distinct d.rutaDocumentoReal from documento d, documento_palabra dp, palabra p where nombrePalabra like \"" + termino + "\"");
            while (rs.next()) {
                resulBusqueda.add(new File(rs.getString("rutaDocumentoReal")));
            }
            conexion.desconectar();
            return resulBusqueda;
        } catch (SQLException ex) {
            Logger.getLogger(Buscador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public ArrayList<File> buscarNombreContenido(){
        return null;
    }
    public boolean existeOrganizacionPrevia(){
        return false;
    }
    
}
