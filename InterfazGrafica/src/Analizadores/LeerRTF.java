/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import org.apache.poi.hmef.extractor.HMEFContentsExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import javax.swing.text.rtf.RTFEditorKit;
import org.tartarus.snowball.Steeming;
/**
 *
 * @author aries
 */
public class LeerRTF {
    public void LeerRTF(File file) {
        
        try {
            RTFEditorKit rtfParser = new RTFEditorKit();
            Document document = rtfParser.createDefaultDocument();
            FileInputStream fi = new FileInputStream(file);
            rtfParser.read(fi, document, 0);
            String text = document.getText(0, document.getLength());
            System.out.println(text);
            
                        
            File archivo=new File("archivos/rtf/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR                  
            writer.write(text);//IMPRIMIMOS EL CONTENIDO
            writer.flush();
            
            StopWords sw = new StopWords();
            Steeming stm=new Steeming();
            archivo = new File("archivos/rtf/" + file.getName() + ".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
            String stopWords="";
            stopWords=sw.remove(sw.remove(text));
            writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
            writer.flush();
            
            archivo = new File("archivos/rtf/"+ file.getName() + ".steeming.txt");            
            writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR           
            writer.write(stm.iniciarSteeming(stopWords));//IMPRIMIMOS EL CONTENIDO
            writer.flush();
            writer.close();//CERRAMOS EL ESCRITOR
                  

        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(LeerRTF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
