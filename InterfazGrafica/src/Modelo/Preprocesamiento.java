package Modelo;


import BKmeans.ClusterWithMeanID;
import Vista.InterfazPrincipal;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.tartarus.snowball.Steeming;

public class Preprocesamiento {
    private ArrayList<File> archivos = new ArrayList<>();
    private ArrayList<File> archivos2 = new ArrayList<>();
    private ArrayList<DocumentoPalabra> docPal = new ArrayList<>();
    private ArrayList<Palabra> palabras = new ArrayList<>();
    private ArrayList<Documento> documentos = new ArrayList<>(); 
    
    int k;
    int minPts;
    double epsilon;
    
    private List<ClusterWithMeanID> clustersID;
    
    

    public Preprocesamiento(ArrayList<File> archivos) {
        this.archivos = archivos;
    }
    
    public int getK(){
        return k;
    }
    public int getMinPts(){
        return minPts;
    }
    public double getEpsilon(){
        return epsilon;
    }
    
    public boolean iniciarPreprocesamiento() throws IOException{
  
    
      InterfazPrincipal.progressBar.setMinimum(0);
      InterfazPrincipal.progressBar.setMaximum(archivos.size());
      InterfazPrincipal.progressBar.setValue(0);
      InterfazPrincipal.progressBar.setStringPainted(true);
      
      InterfazPrincipal.totalArchivos=archivos.size();  
        for (int i = 0; i < archivos.size(); i++){
            
            InterfazPrincipal.progressBar1.setMinimum(0);
            InterfazPrincipal.progressBar1.setMaximum(4);
            InterfazPrincipal.progressBar1.setValue(0);
            InterfazPrincipal.progressBar1.setStringPainted(true);

            InterfazPrincipal.labelStatus.setText("Procesando archivo ("+(i+1)+"/"+(archivos.size())+") "+archivos.get(i).getName());
            
            String type = archivos.get(i).getName();
            type=type.toLowerCase();                        
            if (type.endsWith(".pdf")) {               
                lecturaPDF(archivos.get(i));
                
                //System.out.println("archivo pdf");
            } else if (type.endsWith(".pptx")) {                
                LeerPowerPoint(archivos.get(i));
                //System.out.println("archivo pptx");
            } else if (type.endsWith(".txt")) {                
                LeerTexto(archivos.get(i));
                //System.out.println("archivo txt");
            } else if (type.endsWith(".rtf")) {                
                LeerRTF(archivos.get(i));
                //System.out.println("archivo rtf");
            } else if (type.endsWith(".docx") || type.endsWith(".doc")) {                
                LeerWord(archivos.get(i));
                //System.out.println("archivo docx o doc");
            } 
            
             InterfazPrincipal.progressBar.setValue(i+1);
            InterfazPrincipal.progressBar.setStringPainted(true);
           
        }
        InterfazPrincipal.labelStatus.setText("Calculando IDF (Inverse Document Frequency)");
        calcularIDF();
        //reducirDimensionalidad(50);
        InterfazPrincipal.labelStatus.setText("Calculando Peso");
        calcularPeso();
        InterfazPrincipal.labelStatus.setText("Iniciando Algoritmo ");
        InterfazPrincipal.progressBar1.setIndeterminate(true);
        InterfazPrincipal.progressBar1.setStringPainted(false);
        
        Algoritmo algo = new Algoritmo();
        
        
        clustersID = algo.ejecutaAlgoritmo();
        
        k=algo.getK();
        minPts=algo.getMinPts();
        epsilon=algo.getEpsilon();
        InterfazPrincipal.labelStatus1.setText("");
        InterfazPrincipal.progressBar1.setIndeterminate(true);
        
        
        return true;
    }
    
    public List<ClusterWithMeanID> getClustersID(){
        return clustersID;
    }
    
    public void calcularPeso(){
        InterfazPrincipal.progressBar.setMinimum(0);
        InterfazPrincipal.progressBar.setMaximum(documentos.size());
        InterfazPrincipal.progressBar.setValue(0);
        InterfazPrincipal.progressBar1.setMinimum(0);
        InterfazPrincipal.progressBar1.setMaximum(palabras.size());
        InterfazPrincipal.progressBar1.setValue(0);
        
        HashMap<String,Double>vsm=new HashMap<String,Double>();
        
        double peso = 0;
        System.out.println("Calcular Peso entra");
        BufferedWriter writer = null;
         File c=new File(System.getProperty("user.home")+"/TT/archivos/peso/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
        c.mkdirs();   
       
        String texto="",renglon="",renglon2="";
        File archivopesos = null;
        File archivopesosOk = new File(System.getProperty("user.home")+"/TT/archivos/peso/peso.txt");
        File DocPesos = new File(System.getProperty("user.home")+"/TT/archivos/peso/DocPesos.txt");
        System.out.println("Calcular Peso Print 1");
        /*for(int p=0;p<palabras.size();p++){
            vsm.put(palabras.get(p).getNombre(), Double.valueOf(0));
            
        }*/
        //double acumulador=0;
        int tam=palabras.size();
        InterfazPrincipal.labelStatus.setText("Calculando Peso");
        for(int i = 0;i<documentos.size();i++){
            for(int p=0;p<palabras.size();p++){
            vsm.put(palabras.get(p).getNombre(), Double.valueOf(0));
            }
            InterfazPrincipal.labelStatus1.setText("Documento ("+(i+1)+"/"+(documentos.size()+1)+"):  "+documentos.get(i).getNombre());
            InterfazPrincipal.progressBar1.setValue(0);
            System.out.println("Calcular Peso Print 2");
            try {
                archivopesos =new File(System.getProperty("user.home")+"/TT/archivos/peso/"+documentos.get(i).getNombre()+".peso.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON
                
                for(int k=0;k<docPal.size();k++){
                    
                //    InterfazPrincipal.labelStatus1.setText("Extrayendo palabras  "+palabras.get(i).getDocumento().getNombre());
                    if(documentos.get(i)==docPal.get(k).getDocumento()){
                        
                        for(int j=0;j<palabras.size();j++)
                        {
                            
                            if(docPal.get(k).getPalabra()==palabras.get(j)){
                                //para preobar con el peso descentar las sig 2 lineas
                                peso= (docPal.get(k).getFrecuencia()*palabras.get(j).getIdf());
                                
                                peso*=10000;
                                //int p = (int)peso;
                                //Para probar con la frecencia descomentar la siguiente linea
                               // peso = docPal.get(k).getFrecuencia();
                                vsm.put(palabras.get(j).getNombre(), peso);
                               
                                System.out.println(peso);
                                //acumulador+=peso;
                                //renglon+=¡" ";
                                
                                
                            }
                           
                        }//for palabras
                    }//if
                    InterfazPrincipal.progressBar1.setValue(k+1);
                }//for docpal
                /*double promedio = acumulador / peso;
                for(int p=0;p<tam;p++){                    
                    if(vsm.get(palabras.get(p).getNombre())==0)
                        vsm.put(palabras.get(p).getNombre(), Double.valueOf(promedio));
                }*/
                texto ="";
               Iterator it = vsm.keySet().iterator();
               
               String ren="";
                while(it.hasNext()){
                    //texto +=" :-: ";                  
                  String pal = (String) it.next();
                  texto += pal + " peso = "+vsm.get(pal)+"\n";
                  renglon+=vsm.get(pal)+" "; 
                  ren+=vsm.get(pal)+" ";
                }
                
           //     renglon2+="\n";
                renglon+="\n";
                renglon2+= "["+documentos.get(i).getNombre()+ "]"+ ren+"\n";
                //System.out.println(texto);
                documentos.get(i).vectorspacemodel=vsm;
                writer = new BufferedWriter(new FileWriter(archivopesos)); //CREAMOS EL ESCRITOR
                writer.write(texto);
                writer.flush();
                writer.close();
                 writer = new BufferedWriter(new FileWriter(archivopesosOk)); //CREAMOS EL ESCRITOR
                writer.append(renglon);// write(renglon);
                writer.flush();
                writer.close();
                writer = new BufferedWriter(new FileWriter(DocPesos)); //CREAMOS EL ESCRITOR
                writer.append(renglon2);// write(renglon);
                writer.flush();
                writer.close();
                
            } 
            catch (IOException ex) {
                Logger.getLogger(Preprocesamiento.class.getName()).log(Level.SEVERE, null, ex);
            }
            InterfazPrincipal.progressBar.setValue(i+1);
        }//for documentos
    }
    
    public void reducirDimensionalidad(double porcentaje){
        try {
            BufferedWriter writer = null;
            File archivodimensionalidad;
            double aux=documentos.size()*((100.0-porcentaje)/100.0);
            double aux2=Math.log10(documentos.size()/aux);
            String texto="";
            for(int i=0; i< palabras.size(); i++){
                if(palabras.get(i).getIdf()<=aux2){
                    palabras.remove(i);
                    i--;
                }
            }
            File c=new File(System.getProperty("user.home")+"/TT/archivos/tf/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            c.mkdirs();            
            archivodimensionalidad=new File(System.getProperty("user.home")+"/TT/archivos/tf/dimensionalidad.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON                        
            //archivodimensionalidad = new File("archivos/tf/dimensionalidad.txt");
            writer = new BufferedWriter(new FileWriter(archivodimensionalidad)); //CREAMOS EL ESCRITOR
            for (int j = 0; j < palabras.size(); j++) {
                texto += palabras.get(j).getNombre() + " " + palabras.get(j).getIdf()+ "\n";
            }
            writer.write(texto);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Preprocesamiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    public void calcularIDF(){
        BufferedWriter writer = null;
        try {
           // File archivoidf;
            String texto="";
            InterfazPrincipal.progressBar1.setMinimum(0);
            InterfazPrincipal.progressBar1.setMaximum(palabras.size());
            InterfazPrincipal.progressBar1.setValue(0);
            for(int i=0; i< palabras.size(); i++){
                for(int j=0; j<docPal.size(); j++){
                    if(palabras.get(i).getNombre().equals(docPal.get(j).getPalabra().getNombre())){
                        InterfazPrincipal.labelStatus1.setText("Extrayendo palabras de "+docPal.get(i).getDocumento().getNombre());
                        palabras.get(i).setIdf(palabras.get(i).getIdf()+1);
                        System.out.println("IDF:"+palabras.get(i).getNombre());
                        InterfazPrincipal.progressBar1.setValue(i+1);
                    }
                }//docpal;
                
            }//palabras
            File c=new File(System.getProperty("user.home")+"/TT/archivos/tf/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                c.mkdirs();            
            File archivoidf=new File(System.getProperty("user.home")+"/TT/archivos/tf/idf.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON
            
            //archivoidf = new File("archivos/tf/idf.txt");
            writer = new BufferedWriter(new FileWriter(archivoidf)); //CREAMOS EL ESCRITOR
            for (int j = 0; j < palabras.size(); j++) {
                System.out.println("Ciclo: "+palabras.get(j).getNombre());
                texto += palabras.get(j).getNombre() + " " + palabras.get(j).getIdf();
                palabras.get(j).setIdf(Math.log10(documentos.size()/palabras.get(j).getIdf()));
                texto += " " + palabras.get(j).getIdf() + "\n";
            }
            System.out.println("No se que es: "+texto);
            writer.write(texto);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Preprocesamiento.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preprocesamiento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    
    public void calcularTF(String nombre, String contenido) throws FileNotFoundException, IOException {
        int existenciaPalabra;
        boolean existenciaEnDocumento;
        int numPalabras;
        String texto;
        File archivotf;
        texto = "";
        documentos.add(new Documento(nombre));
        numPalabras = 0;
        for (String cadena : contenido.split("\n")) {
            existenciaPalabra = -1;
            existenciaEnDocumento = false;
            numPalabras = numPalabras + 1;
            
            for (int i = 0; i < palabras.size(); i++) {
                
                if (palabras.get(i).getNombre().equals(cadena)) {
                    existenciaPalabra = i;
                }
            }
            if (existenciaPalabra != -1) {
                for (int j = 0; j < docPal.size(); j++) {
                    if (docPal.get(j).getPalabra().getNombre().equals(cadena) && docPal.get(j).getDocumento().getNombre().equals(nombre)) {//le falta a la condicion lo del documento
                        docPal.get(j).setFrecuencia(docPal.get(j).getFrecuencia() + 1);
                        existenciaEnDocumento = true;
                    }
                }
                if (existenciaEnDocumento == false) {
                    docPal.add(new DocumentoPalabra(documentos.get(documentos.size() - 1), palabras.get(existenciaPalabra), 1));
                }

            } else {
                palabras.add(new Palabra(cadena));
                docPal.add(new DocumentoPalabra(documentos.get(documentos.size() - 1), palabras.get(palabras.size() - 1), 1));
            }
        }//for split
        File c=new File(System.getProperty("user.home")+"/TT/archivos/tf/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                c.mkdirs();            
        archivotf=new File(System.getProperty("user.home")+"/TT/archivos/tf/" + nombre + ".tf.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON
        //archivotf = new File("archivos/tf/" + nombre + ".tf.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(archivotf));//CREAMOS EL ESCRITOR
        for (int j = 0; j < docPal.size(); j++) {
            if (docPal.get(j).getDocumento().getNombre().equals(nombre)) {
                texto += docPal.get(j).getPalabra().getNombre() + " " + docPal.get(j).getFrecuencia();
                docPal.get(j).setFrecuencia(docPal.get(j).getFrecuencia() / numPalabras);
                texto += " " + docPal.get(j).getFrecuencia() + "\n";
            }
        }
        System.out.println(texto);
        writer.write(texto);
        writer.flush();
        writer.close();
        
    }//calcularTF
    
    public void lecturaPDF(File file){
        InterfazPrincipal.labelStatus1.setText("Etapa (1/5) Extrayendo texto de PDF ");
        InterfazPrincipal.progressBar1.setValue(0);
        String ln = System.getProperty("line.separator"); 
        String carpeta = file.getParent();
       // System.out.println(carpeta);
        File dir = new File(carpeta);//CREO UN OBJETO CON TODOS LOS ARCHIVOS QUE CONTIENE LA CARPETA QUE CONTIENE LOS PDFS.
        String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
        
        if (ficheros == null)//EXCEPCION
              System.out.println("No hay archivos en la carpeta especificada");
        else { 
          for (int x=0;x<1/*ficheros.length*/;x++){//RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
            String ruta=new String();//VARIABLE QUE DETERMINARA LA RUTA DEL ARCHIVO A LEER.
            ruta=(carpeta+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER. 
            ruta = file.getAbsolutePath();
              try {
                  PDDocument pd = PDDocument.load(ruta); //CARGAR EL PDF
                  if(pd.isEncrypted()){
                      pd.close();//CERRAMOS OBJETO ACROBAT
                      return;
                  }
                  List l = pd.getDocumentCatalog().getAllPages();//NUMERO LAS PAGINAS DEL ARCHIVO
                  Object[] obj = l.toArray();//METO EN UN OBJETO LA LISTA DE PAGINAS PARA MANIPULARLA
                  String contenido= new String();//CONTENIDO = A LO QUE CONTENGA EL AREA O REGION
                  for (int i = 0; i < obj.length; i++) {
                      PDPage page = (PDPage) obj[i];//PAGE ES LA PAGINA 1 DE LA QUE CONSTA EL ARCHIVO
                      //System.out.println("pagina #" + i);
                      PageFormat pageFormat = pd.getPageFormat(0);//PROPIEDADES DE LA PAGINA (FORMATO)
                      Double d1 = new Double(pageFormat.getHeight());//ALTO
                      Double d2 = new Double(pageFormat.getWidth());//ANCHO
                      int width = d1.intValue();//ANCHO
                      int eigth = 1024;//ALTO
                      PDFTextStripperByArea stripper = new PDFTextStripperByArea();//COMPONENTE PARA ACCESO AL TEXTO
                      Rectangle rect = new Rectangle(0, 0, width, eigth);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
                      stripper.addRegion("area1", rect);//REGISTRAMOS LA REGION CON UN NOMBRE
                      stripper.extractRegions(page);//EXTRAE TEXTO DEL AREA
                      contenido += (rect + stripper.getTextForRegion("area1"))+" ";                  
                  }

                if("".equals(contenido.trim())){
                    System.out.println("Documento "+file.getName()+" no contiene texto");
                    return;
                }     
                InterfazPrincipal.progressBar1.setValue(1);
                  File c=new File(System.getProperty("user.home")+"/TT/archivos/pdf/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                c.mkdirs();
                  File archivo=new File(System.getProperty("user.home")+"/TT/archivos/pdf/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                  BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  //writer.write(ruta);//IMPRIMIMOS LA RUTA
                  writer.write(contenido);//IMPRIMIMOS EL CONTENIDO
                  //System.out.println(contenido);
               //   writer.close();//CERRAMOS EL ESCRITOR
                  writer.flush();
                  Steeming stm=new Steeming();
                //  StopWords sw = new StopWords();
                  archivo = new File("archivos/pdf/" + file.getName() + ".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                  writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  InterfazPrincipal.labelStatus1.setText("Etapa (2/5) Eliminando StopWords ");
                  
                  String stopWords="";
                  stopWords=eliminarStopWords(contenido);
                  InterfazPrincipal.progressBar1.setValue(2);        
                  writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
                  writer.flush();
                  archivo = new File("archivos/pdf/"+ file.getName() + ".steeming.txt");
                  writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  InterfazPrincipal.labelStatus1.setText("Etapa (3/5) Stemming ");
                  
                  String steeming=stm.iniciarSteeming(stopWords);
                  writer.write(steeming);//IMPRIMIMOS EL CONTENIDO
                  InterfazPrincipal.progressBar1.setValue(3);
                  //System.out.println(contenido);
                  InterfazPrincipal.labelStatus1.setText("Etapa (4/5) Calculando TF (Term Frequency) ");
                  
                  calcularTF( file.getName(),steeming);
                  InterfazPrincipal.progressBar1.setValue(4);
                  writer.close();//CERRAMOS EL ESCRITOR
                                   
                  pd.close();//CERRAMOS OBJETO ACROBAT
              } catch (IOException e) {
                  if(e.toString()!=null){
                    File archivo=new File("dañado_"+ficheros[x]+".txt");//SEPARA LOS DAÑADOS
                  }
                  System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
                  e.printStackTrace();
              }//CATCH
          }//FOR
        }//ELSE
        
        InterfazPrincipal.contadorArchivos++;
    }//LECTURAPDF()
    
    public void LeerWord(File file){
            InterfazPrincipal.labelStatus1.setText("Etapa (1/5) Extrayendo texto de Word ");
            InterfazPrincipal.progressBar1.setValue(0);
            try {
                XWPFDocument document = null;
                document = new XWPFDocument(new FileInputStream(file));
                XWPFWordExtractor extract = new XWPFWordExtractor(document);
                if("".equals(extract.getText().trim())){
                    System.out.println("Documento "+file.getName()+" no contiene texto");
                    return;
                }                
                                
                File c=new File(System.getProperty("user.home")+"/TT/archivos/docx/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                c.mkdirs();
                File archivo=new File(System.getProperty("user.home")+"/TT/archivos/docx/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                writer.write(extract.getText());//IMPRIMIMOS EL CONTENIDO               
                writer.flush();
                //StopWords sw=new StopWords();
                InterfazPrincipal.progressBar1.setValue(1);
                Steeming stm=new Steeming();
                archivo=new File("archivos/docx/"+file.getName()+".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                InterfazPrincipal.labelStatus1.setText("Etapa (2/5) Eliminando StopWords ");
                String stopWords=eliminarStopWords(extract.getText());
                writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
                InterfazPrincipal.progressBar1.setValue(2);
                writer.flush();
                archivo = new File("archivos/docx/"+ file.getName() + ".steeming.txt");
                writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                InterfazPrincipal.labelStatus1.setText("Etapa (3/5) Stemming ");
                String steeming=stm.iniciarSteeming(stopWords);
                writer.write(steeming);//IMPRIMIMOS EL CONTENIDO
                InterfazPrincipal.progressBar1.setValue(3);
                InterfazPrincipal.labelStatus1.setText("Etapa (4/5) Calculando TF (Term Frequency) ");
                calcularTF( file.getName(),steeming);
                InterfazPrincipal.progressBar1.setValue(4);
                writer.close();//CERRAMOS EL ESCRITOR                
            } catch (IOException ex) {
               
            }
            InterfazPrincipal.contadorArchivos++;
        }//LeerWord
    
        public void LeerPowerPoint(File file){  
            InterfazPrincipal.progressBar1.setValue(0);
            InterfazPrincipal.labelStatus1.setText("Etapa (1/5) Extrayendo texto de PowerPoint ");
            try {
                XMLSlideShow document = null;
                try {
                    document = new XMLSlideShow(new FileInputStream(file));
                } catch (IOException ex) {
                 
                }
                
                XSLFPowerPointExtractor extract = new XSLFPowerPointExtractor(document);
                if("".equals(extract.getText().trim())){
                    System.out.println("Documento "+file.getName()+" no contiene texto");
                    return;
                }
                InterfazPrincipal.progressBar1.setValue(1);
                File c=new File(System.getProperty("user.home")+"/TT/archivos/pptx/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                c.mkdirs();
                File archivo=new File(System.getProperty("user.home")+"/TT/archivos/pptx/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                writer.write(extract.getText());//IMPRIMIMOS EL CONTENIDO               
                writer.flush();
                //StopWords sw=new StopWords();
                Steeming stm=new Steeming();
                archivo=new File("archivos/pptx/"+file.getName()+".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                InterfazPrincipal.labelStatus1.setText("Etapa (2/5) Eliminando StopWords ");
                String stopWords=eliminarStopWords(extract.getText());
                InterfazPrincipal.progressBar1.setValue(2);
                writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO              
                writer.flush();
                archivo = new File("archivos/pptx/"+ file.getName() + ".steeming.txt");
                writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                InterfazPrincipal.labelStatus1.setText("Etapa (3/5) Stemming ");
                String steeming=stm.iniciarSteeming(stopWords);
                InterfazPrincipal.progressBar1.setValue(3);
                writer.write(steeming);//IMPRIMIMOS EL CONTENIDO
                InterfazPrincipal.labelStatus1.setText("Etapa (4/5) Calculando TF (Term Frequency) ");
                calcularTF( file.getName(),steeming);
                InterfazPrincipal.progressBar1.setValue(4);
                writer.close();//CERRAMOS EL ESCRITOR
                
            } catch (IOException ex) {
        
            }
            InterfazPrincipal.contadorArchivos++;
        }//LeerPowerPoint
        
        public void LeerTexto(File file){
            InterfazPrincipal.labelStatus1.setText("Etapa (1/5) Extrayendo texto de TXT ");
            InterfazPrincipal.progressBar1.setValue(0);
        try {
            String cadena="";
            String texto="";
            //System.out.println(file.getAbsolutePath());
            FileReader f = new FileReader(file);
            BufferedReader b = new BufferedReader(f);
            int i=0;
            while((cadena = b.readLine())!=null || i<1000) {
                texto+=cadena+" ";
                i++;
               // System.out.println(cadena);
            }
            if("".equals(texto.trim())){
                    System.out.println("Documento "+file.getName()+" no contiene texto");
                    return;
                }
            //System.out.println(file.getAbsolutePath());
            File c=new File(System.getProperty("user.home")+"/TT/archivos/txt/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            c.mkdirs();            
            File archivo=new File(System.getProperty("user.home")+"/TT/archivos/txt/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON
            InterfazPrincipal.progressBar1.setValue(1);
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR                  
            writer.write(texto);//IMPRIMIMOS EL CONTENIDO
            writer.flush();

         //   StopWords sw = new StopWords();
            Steeming stm=new Steeming();
            archivo = new File("archivos/txt/" + file.getName() + ".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
            String stopWords="";
            InterfazPrincipal.labelStatus1.setText("Etapa (2/5) Eliminando StopWords ");
            stopWords=eliminarStopWords(texto);
            
            writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
            InterfazPrincipal.progressBar1.setValue(2);
            writer.flush();
            archivo = new File("archivos/txt/" + file.getName() + ".steeming.txt");
            writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
            InterfazPrincipal.labelStatus1.setText("Etapa (3/5) Stemming ");
            String steeming=stm.iniciarSteeming(stopWords);
            InterfazPrincipal.progressBar1.setValue(3);
            writer.write(steeming);//IMPRIMIMOS EL CONTENIDO
            InterfazPrincipal.labelStatus1.setText("Etapa (4/5) Calculando TF (Term Frequency) ");
            calcularTF( file.getName(),steeming);
            InterfazPrincipal.progressBar1.setValue(4);
            writer.close();//CERRAMOS EL ESCRITOR
                  
            b.close();
        } catch (IOException ex) {
            System.out.println("Error con el archivo");
            
        }
        
    InterfazPrincipal.contadorArchivos++;
    
    
    }//LeerTexto
        
    public void LeerRTF(File file) {
        InterfazPrincipal.progressBar1.setValue(0);
        InterfazPrincipal.labelStatus1.setText("Etapa (1/5) Extrayendo texto de RTF ");
        try {
            RTFEditorKit rtfParser = new RTFEditorKit();
            Document document = rtfParser.createDefaultDocument();
            FileInputStream fi = new FileInputStream(file);
            rtfParser.read(fi, document, 0);
            String text = document.getText(0, document.getLength());
            System.out.println(text);
            
                        
            File c=new File(System.getProperty("user.home")+"/TT/archivos/rtf/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            c.mkdirs();            
            File archivo=new File(System.getProperty("user.home")+"/TT/archivos/rtf/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR                  
            writer.write(text);//IMPRIMIMOS EL CONTENIDO
            writer.flush();
            InterfazPrincipal.progressBar1.setValue(1);
//            StopWords sw = new StopWords();
            Steeming stm=new Steeming();
            archivo = new File("archivos/rtf/" + file.getName() + ".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
            String stopWords="";
            InterfazPrincipal.labelStatus1.setText("Etapa (2/5) Eliminando StopWords ");
            stopWords=eliminarStopWords(text);
            InterfazPrincipal.progressBar1.setValue(2);
            writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
            writer.flush();
            
            archivo = new File("archivos/rtf/"+ file.getName() + ".steeming.txt");            
            writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR    
            InterfazPrincipal.labelStatus1.setText("Etapa (3/5) Stemming ");
            String steeming=stm.iniciarSteeming(stopWords);
            writer.write(steeming);//IMPRIMIMOS EL CONTENIDO
            InterfazPrincipal.progressBar1.setValue(3);
            InterfazPrincipal.labelStatus1.setText("Etapa (4/5) Calculando TF (Term Frequency) ");
            calcularTF( file.getName(),steeming);
            InterfazPrincipal.progressBar1.setValue(4);
            writer.flush();
            writer.close();//CERRAMOS EL ESCRITOR
                  

        } catch (BadLocationException | IOException ex) {
         
        }
        InterfazPrincipal.contadorArchivos++;
    }//LeerRTF
    
    public String eliminarStopWords(String contenido) {
       Charset.forName("UTF-8").encode(contenido); 
        ArrayList<String> listaPalabras = new ArrayList<>();
        String contenidoSinSW="";
        
        Set<String> stopWordList;
        stopWordList=new HashSet<String>();
        
        
        String[] stopWrds = {
            "a",
            "acá",
            "ahí", 
            "ajena", 
            "ajeno", 
            "ajenas", 
            "ajenos", 
            "al", 
            "algo", 
            "algún", 
            "algúna", 
            "algúno", 
            "algúnos", 
            "algúnas", 
            "allá", 
            "allí", 
            "ambos", 
            "ante", 
            "antes", 
            "aquel", 
            "aquella", 
            "aquello", 
            "aquellas",
            "aquellos",
            "aquí",
            "arriba",
            "así", 
            "atrás",
            "aun",
            "aunque",
            "bajo",
            "bastante", 
            "bien",
            "cabe",
            "cada", 
            "casi", 
            "cierto",
            "cierta",
            "ciertos", 
            "ciertas",
            "como", 
            "con",
            "conmigo",
            "conseguimos", 
            "conseguir",
            "consigo", 
            "consigue",
            "consiguen",
            "consigues",
            "contigo", 
            "contra", 
            "cual", 
            "cuales", 
            "cualquier",
            "cualquiera",
            "cualquieras",
            "cuan",
            "cuando",
            "cuanto",
            "cuanta",
            "cuantos", 
            "cuantas",
            "de",
            "dejar",
            "del",
            "demás",
            "demasiada",
            "demasiado",
            "demasiados",
            "demasiadas",
            "dentro",
            "desde",
            "donde",
            "dos",
            "el",
            "él",
            "ella",
            "ellas",
            "ellos",
            "ello",
            "empleáis",
            "emplean",
            "emplear",
            "empleas",
            "empleo",
            "en",
            "encima",
            "entonces",
            "entre",
            "era",
            "eras",
            "eramos",
            "eran",
            "eres",
            "es",
            "esa",
            "ese",
            "eso",
            "esas",
            "eses",
            "esos",
            "esta",
            "estas",
            "estaba",
            "estado",
            "estáis", 
            "estamos", 
            "están",
            "estar", 
            "este", 
            "esto",
            "estos", 
            "estoy",
            "etc",
            "fin", 
            "fue",
            "fueron",
            "fui", 
            "fuimos",
            "gueno",
            "ha",
            "hace",
            "haces", 
            "hacéis",
            "hacemos",
            "hacen", 
            "hacer",
            "hacia", 
            "hago", 
            "hasta", 
            "incluso", 
            "intenta", 
            "inteas", 
            "intentáis", 
            "intentamos",
            "intentan", 
            "intentar", 
            "intento",
            "ir",
            "jamás",
            "junto",
            "juntos",
            "la",
            "lo",
            "las",
            "los",
            "largo",
            "más", 
            "me",
            "menos", 
            "mi",
            "mis", 
            "mía",
            "mías",
            "mientras",
            "mío", 
            "míos",
            "misma",
            "mismo",
            "mismas", 
            "mismos", 
            "modo",
            "mucha",
            "muchas",
            "muchísima",
            "muchisimo",
            "muchisimas", 
            "muchisimos",
            "mucho",
            "muchos", 
            "muy", 
            "nada", 
            "ni",
            "ningún",
            "minguna",
            "ninguno",
            "ningunas",
            "ningunos",
            "no", 
            "nos",
            "nosotras",
            "nosotros",
            "nuestra",
            "nuestro",
            "nuestras",
            "nuestros",
            "nunca",
            "os",
            "otra",
            "otro",
            "otras",
            "otros",
            "para",
            "parecer",
            "pero",
            "poca", 
            "pocas", 
            "poco",
            "pocos", 
            "podéis",
            "podemos",
            "poder",
            "podría",
            "podrías",
            "podríais",
            "podríamos",
            "podrían",
            "por",
            "por qué",
            "porque",
            "primero",
            "puede",
            "pueden",
            "puedo", 
            "pues",
            "que",
            "qué",
            "querer",
            "quién",
            "quienes",
            "quienesquiera",
            "quienquiera",
            "quizá",
            "quizás",
            "sabe",
            "sabes",
            "saben",
            "sabéis",
            "sabemos",
            "saber",
            "se", 
            "según",
            "ser",
            "si",
            "sí",
            "siempre",
            "siendo", 
            "sin", 
            "sino", 
            "so", 
            "sobre", 
            "sois", 
            "solamente", 
            "solo", 
            "sólo",
            "somos",
            "soy",
            "sr",
            "sr.",
            "sra",
            "sra.",
            "sres",
            "sres.",
            "sta",
            "sta.",
            "su", 
            "sus", 
            "suya", 
            "suyo", 
            "suyas", 
            "suyos",
            "tal",
            "tales",
            "también",
            "tampoco",
            "tan",
            "tanta", 
            "tanto", 
            "tantas", 
            "tantos", 
            "te",
            "tenéis",
            "tenemos", 
            "tener",
            "tengo", 
            "ti",
            "tiempo",
            "tiene",
            "tienen",
            "toda", 
            "todo", 
            "todas",
            "todos", 
            "tomar",
            "trabaja",
            "trabajo",
            "trabajáis",
            "trabajamos",
            "trabajan", 
            "trabajar",
            "trabajas", 
            "tras", 
            "tú",
            "tu",
            "tus",
            "tuya", 
            "tuyo",
            "tuyos", 
            "tuyas/o/s", 
            "último", 
            "ultimo", 
            "un", 
            "una", 
            "uno", 
            "unas", 
            "unos", 
            "usa", 
            "usas",
            "usáis", 
            "usamos",
            "usan", 
            "usar", 
            "uso",
            "usted",
            "ustedes", 
            "va",
            "van",
            "vais",
            "valor",
            "vamos", 
            "varias",
            "varios",
            "vaya", 
            "verdadera", 
            "vosotras", 
            "vosotros", 
            "voy", 
            "vuestra", 
            "vuestro", 
            "vuestras", 
            "vuestros", 
            "y", 
            "ya", 
            "yo"};
        for(int i=0;i<stopWrds.length;i++){
            stopWordList.add(stopWrds[i]);
        }
    System.out.println("Contenido");    
    System.out.println(contenido);
    
    contenido=contenido.replace('�', ' ');
    contenido=contenido.replace('.', ' ');
    contenido=contenido.replace(',', ' ');
    contenido=contenido.replace(';', ' ');    
    contenido=contenido.replace(':', ' ');
    contenido=contenido.replace('?', ' ');
    contenido=contenido.replace('¿', ' ');
    contenido=contenido.replace('.', ' ');
    
    contenido=contenido.replace('!', ' ');
    contenido=contenido.replace('¡', ' ');
    contenido=contenido.replace('"', ' ');
    contenido=contenido.replace('#', ' ');
    contenido=contenido.replace('$', ' ');
    contenido=contenido.replace('%', ' ');
    contenido=contenido.replace('/', ' ');
    contenido=contenido.replace('&', ' ');
    contenido=contenido.replace('(', ' ');
    contenido=contenido.replace(')', ' ');
    contenido=contenido.replace('=', ' ');
    contenido=contenido.replace('-', ' ');
    contenido=contenido.replace('+', ' ');
    contenido=contenido.replace('*', ' ');
    contenido=contenido.replace('{', ' ');
    contenido=contenido.replace('}', ' ');
    contenido=contenido.replace('[', ' ');
    contenido=contenido.replace(']', ' ');
    contenido=contenido.replace('&', ' ');
    contenido=contenido.replace('_', ' ');
    contenido=contenido.replace('@', ' ');
    contenido=contenido.replace('\'', ' ');
    contenido=contenido.replace('<', ' ');
    contenido=contenido.replace('>', ' ');
    contenido=contenido.replace('^', ' ');
    contenido=contenido.replace('`', ' ');
    contenido=contenido.replace('~', ' ');
    contenido=contenido.replace('|', ' ');
    contenido=contenido.replace('°', ' ');
    contenido=contenido.replace('¬', ' ');
    
    contenido=contenido.replace('1', ' ');
    contenido=contenido.replace('2', ' ');
    contenido=contenido.replace('3', ' ');
    contenido=contenido.replace('4', ' ');
    contenido=contenido.replace('5', ' ');
    contenido=contenido.replace('6', ' ');
    contenido=contenido.replace('7', ' ');
    contenido=contenido.replace('8', ' ');
    contenido=contenido.replace('9', ' ');
    contenido=contenido.replace('0', ' ');
    
    contenido=contenido.replace('—', ' ');
    
    
    System.out.println(contenido);
    List<String> listOfStrings = new LinkedList<String>(Arrays.asList(contenido.split(" ")));
    listOfStrings.removeAll(stopWordList);
    contenidoSinSW = String.join(" ", listOfStrings);
    
    System.out.println(contenidoSinSW);    

        //contenido=contenido.trim().replaceAll("\\s+", " ");
 /*       String[] words = contenido.trim().split(" ");

        for (String word : words) {
        listaPalabras.add(word.toLowerCase());
        }

        //remove stop words here from the temp list
        for (int i = 0; i < listaPalabras.size(); i++) {
        // get the item as string
            for (int j = 0; j < stopWrds.length; j++) {
                if (stopWrds[j].toLowerCase().contains(listaPalabras.get(i).toLowerCase())) {
                    listaPalabras.remove(i);
                }
            }
        }

        for (String str : listaPalabras) {
            contenidoSinSW+=str+" ";
        }        */
        return contenidoSinSW;
    }
    
    
    
    
    
    
    
}
