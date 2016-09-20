package BaseDatos;

import java.sql.*;

public class DataBase {
	
	private Connection conexion;
	private Statement instruccion;
	private ResultSet resultado;
	
	private String db;
	private String user;
	private String pass;
	
	public DataBase(String database, String usuario, String password) {
		instruccion = null;//Sentencias SQL
		resultado = null;//Resultados de la consulta
		conexion = null;//conexion	
		
		db = database;
		user = usuario;
		pass = password;
	}
	
	/* Funcion para establecer la conexion a la base de datos 
	 * */
	public void conectar(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/"+db,user,pass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Funcion para terminar la conexion establecida a la base de datos
	 * Para poder hacer uso de este metodo es necesario haber llamado al metodo 'consulta' o 'actualizar'
	 * */
	public void desconectar(){
		try{
			if(resultado != null) 
				resultado.close();
			instruccion.close();
			conexion.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/* Funcion para hacer consultas unicamente del tipo SELECT
	 * Devuelve un ResultSet con el resultado de ejecutar la consulta
	 * */
	public ResultSet consulta(String query){
		try{
			instruccion = conexion.createStatement();
			resultado = instruccion.executeQuery(query);
			
			return resultado;	
		}catch(SQLException e){
			e.printStackTrace();
		}
		return resultado;		
	}
	
	/* Funcion para hacer consultas 'update', 'delete' e 'insert into'
	 * Devuelve true si la consulta se realizó con exito, false de otro modo 
	 * */
	
	public boolean actualizar(String query){
		try{
			instruccion = conexion.createStatement();
			int i = instruccion.executeUpdate(query);
			if(i!=0){
				System.out.println("Actualizado");
				return true;
			}
			else{
				System.out.println("Error");
				return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
}