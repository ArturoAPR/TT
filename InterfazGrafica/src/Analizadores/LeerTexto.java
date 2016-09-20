/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tartarus.snowball.Steeming;

/**
 *
 * @author Alejandro
 */
public class LeerTexto {
    
    public void LeerTexto(File file){
        try {
            String cadena="";
            String texto="";
            //System.out.println(file.getAbsolutePath());
            FileReader f = new FileReader(file);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                texto+=cadena+" ";
               // System.out.println(cadena);
            }
            if("".equals(texto.trim())){
                    System.out.println("Documento "+file.getName()+" no contiene texto");
                    return;
                }
            //System.out.println(file.getAbsolutePath());
            File archivo=new File("archivos/txt/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR                  
            writer.write(texto);//IMPRIMIMOS EL CONTENIDO

            //System.out.println(contenido);
            // writer.close();//CERRAMOS EL ESCRITOR
            writer.flush();
            StopWords sw = new StopWords();
            Steeming stm=new Steeming();
            archivo = new File("archivos/txt/" + file.getName() + ".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                       String stopWords="";
                  stopWords=sw.remove(sw.remove(texto));
            writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
            //System.out.println(contenido);
           // writer.close();//CERRAMOS EL ESCRITOR
 
                writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
                  writer.flush();
                  archivo = new File("archivos/txt/"+ file.getName() + ".steeming.txt");
                  writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  writer.write(stm.iniciarSteeming(stopWords));//IMPRIMIMOS EL CONTENIDO
                  //System.out.println(contenido);
                  writer.close();//CERRAMOS EL ESCRITOR
                  
            b.close();
        } catch (IOException ex) {
            System.out.println("Error con el archivo");
            
        }
        

    
    
    }
    
}
