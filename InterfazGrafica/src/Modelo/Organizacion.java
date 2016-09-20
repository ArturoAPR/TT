/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Alejandro
 */
public class Organizacion {
    private ArrayList<Documento> documentos;
    private ArrayList<Categoria> categorias;
    private ArrayList<Palabra> palabras;
    private ArrayList<CategoriaDocumento> categoriaDocumentos;
    private ArrayList<PalabraCategoria> palabraCategorias;
    private ArrayList<DocumentoPalabra> documetoPalabras;
    private DefaultMutableTreeNode suerencia;
    
    public Organizacion(){
    
    }
    public boolean copiarArchivos(){
        return false;
    }
    public boolean moverArchivos(){
        return false;
    }
    public boolean borrarOrganizacion(){
        return false;
    }

    public ArrayList<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(ArrayList<Documento> documentos) {
        this.documentos = documentos;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    public ArrayList<Palabra> getPalabras() {
        return palabras;
    }

    public void setPalabras(ArrayList<Palabra> palabras) {
        this.palabras = palabras;
    }

    public ArrayList<CategoriaDocumento> getCategoriaDocumentos() {
        return categoriaDocumentos;
    }

    public void setCategoriaDocumentos(ArrayList<CategoriaDocumento> categoriaDocumentos) {
        this.categoriaDocumentos = categoriaDocumentos;
    }

    public ArrayList<PalabraCategoria> getPalabraCategorias() {
        return palabraCategorias;
    }

    public void setPalabraCategorias(ArrayList<PalabraCategoria> palabraCategorias) {
        this.palabraCategorias = palabraCategorias;
    }

    public ArrayList<DocumentoPalabra> getDocumetoPalabras() {
        return documetoPalabras;
    }

    public void setDocumetoPalabras(ArrayList<DocumentoPalabra> documetoPalabras) {
        this.documetoPalabras = documetoPalabras;
    }
    public DefaultMutableTreeNode obtenerSugerencia(){
        return null;
    }            
}
