
package org.tartarus.snowball;

import java.lang.reflect.Method;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.StringReader;
import java.util.ArrayList;

public class Steeming {


    public String iniciarSteeming(String archivo) {


        try {
            Class stemClass = Class.forName("org.tartarus.snowball.ext.spanishStemmer");
            SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

            System.out.println("archivo: "+archivo);
            String stm="";

            String[] palabras=archivo.split(" ");
            for(int i=0;i<palabras.length;i++){                                                       
                    if((palabras[i]).trim().length()>0 && palabras[i]!=null){
                         stemmer.setCurrent(palabras[i]);
                            for (int j = 1; j != 0; j--) {
                            stemmer.stem();
                    }
                        stm+=stemmer.getCurrent()+"\n"; 
                    }
                   // 
                }
            System.out.println("SALIDA STM:("+stm+")");
            return stm;
            //output.flush();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(Steeming.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
        
    }
}
