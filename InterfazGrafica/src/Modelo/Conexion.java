/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class Conexion {
    private String nombre;
    private String usuario;
    private String contraseña;
    private Connection conexion;
    private String servidor;
    
    private Statement instruccion;
    private ResultSet resultado;

    public Conexion() {
    }

    public Conexion(String nombre, String usuario, String contraseña, String servidor) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;        
        this.servidor = servidor;
    }
    
    public boolean conectar(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://"+servidor+"/" + nombre, usuario, contraseña);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }
    public boolean desconectar(){
        try {
            conexion.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public ResultSet consulta(String consulta) {
        try {
            instruccion = conexion.createStatement();
            resultado = instruccion.executeQuery(consulta);

            return resultado;
        } catch (SQLException e) {
        }
        return resultado;
    }
    
}
