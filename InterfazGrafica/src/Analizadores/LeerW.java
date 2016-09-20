/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;
import java.io.BufferedWriter;
import java.io.File;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.*;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.apache.poi.hmef.extractor.HMEFContentsExtractor;
import org.tartarus.snowball.Steeming;
/**
 *
 * @author aries
 */
public class LeerW {
    public void LeerWord(File file){
            try {
                XWPFDocument document = null;
               // System.out.println(file.getAbsolutePath());
                
                document = new XWPFDocument(new FileInputStream(file));
                    
                
                XWPFWordExtractor extract = new XWPFWordExtractor(document);
                if("".equals(extract.getText().trim())){
                    System.out.println("Documento "+file.getName()+" no contiene texto");
                    return;
                }                

                //System.out.println(extract.getText());                
                File archivo=new File("archivos/docx/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                writer.write(extract.getText());//IMPRIMIMOS EL CONTENIDO
                //System.out.println(contenido);                
                writer.flush();
                StopWords sw=new StopWords();
                Steeming stm=new Steeming();
                archivo=new File("archivos/docx/"+file.getName()+".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
               // writer.write(sw.remove(extract.getText()));//IMPRIMIMOS EL CONTENIDO
                //System.out.println(contenido);
               // writer.close();//CERRAMOS EL ESCRITOR
                String stopWords="";
                stopWords=sw.remove(sw.remove(extract.getText()));
                writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
                //System.out.println(contenido);
              //  writer.close();//CERRAMOS EL ESCRITOR
 
              //  writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
                  writer.flush();
                  archivo = new File("archivos/docx/"+ file.getName() + ".steeming.txt");
                  writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  writer.write(stm.iniciarSteeming(stopWords));//IMPRIMIMOS EL CONTENIDO
                  //System.out.println(contenido);
                  writer.close();//CERRAMOS EL ESCRITOR                
            } catch (IOException ex) {
                Logger.getLogger(LeerW.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
}