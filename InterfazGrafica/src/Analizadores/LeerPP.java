/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;
import java.io.BufferedWriter;
import java.io.File;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.tartarus.snowball.Steeming;
/**
 *
 * @author ARTUROA
 */
public class LeerPP {

    /**
     * @param args the command line arguments
     */
    public void LeerPowerPoint(File file){

            
            try {

                
                XMLSlideShow document = null;
                try {
                    document = new XMLSlideShow(new FileInputStream(file));
                } catch (IOException ex) {
                    Logger.getLogger(LeerPP.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                XSLFPowerPointExtractor extract = new XSLFPowerPointExtractor(document);
                
               // System.out.println(extract.getText());
                if("".equals(extract.getText().trim())){
                    System.out.println("Documento "+file.getName()+" no contiene texto");
                    return;
                }
                File archivo=new File("archivos/pptx/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                writer.write(extract.getText());//IMPRIMIMOS EL CONTENIDO
                //System.out.println(contenido);                
                writer.flush();
                StopWords sw=new StopWords();
                Steeming stm=new Steeming();
                archivo=new File("archivos/pptx/"+file.getName()+".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                String contenido=sw.remove(extract.getText());
                writer.write(sw.remove(contenido));//IMPRIMIMOS EL CONTENIDO
                //System.out.println(contenido);
               // writer.close();//CERRAMOS EL ESCRITOR
                String stopWords="";
                  stopWords=sw.remove(contenido);
                writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
                  writer.flush();
                  archivo = new File("archivos/pptx/"+ file.getName() + ".steeming.txt");
                  writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  writer.write(stm.iniciarSteeming(stopWords));//IMPRIMIMOS EL CONTENIDO
                  //System.out.println(contenido);
                  writer.close();//CERRAMOS EL ESCRITOR
                
            } catch (IOException ex) {
                Logger.getLogger(LeerPP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        
        
    
    
}
