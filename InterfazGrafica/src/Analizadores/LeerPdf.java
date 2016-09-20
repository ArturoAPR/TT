package Analizadores;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.tartarus.snowball.Steeming;

public class LeerPdf {
 /*   public static void main(String[] args) {
       LeerPdf leerPDF =new LeerPdf();
       //leerPDF.lecturaPDF();
    }
   */ 
    public void lecturaPDF(File file){
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
                  File archivo=new File("archivos/pdf/"+file.getName()+".txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                  BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  writer.write(ruta);//IMPRIMIMOS LA RUTA
                  writer.write(contenido);//IMPRIMIMOS EL CONTENIDO
                  //System.out.println(contenido);
               //   writer.close();//CERRAMOS EL ESCRITOR
            writer.flush();
                  Steeming stm=new Steeming();
                  StopWords sw = new StopWords();
                  archivo = new File("archivos/pdf/" + file.getName() + ".stopWords.txt");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                  writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  String stopWords="";
                  stopWords=sw.remove(contenido);
                          
                  writer.write(stopWords);//IMPRIMIMOS EL CONTENIDO
                  writer.flush();
                  archivo = new File("archivos/pdf/"+ file.getName() + ".steeming.txt");
                  writer = new BufferedWriter(new FileWriter(archivo));//CREAMOS EL ESCRITOR
                  writer.write(stm.iniciarSteeming(stopWords));//IMPRIMIMOS EL CONTENIDO
                  //System.out.println(contenido);
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
    }//LECTURAPDF()
}//CLASS
