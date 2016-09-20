/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class StopWords {

    public String remove(String contenido) {
        

        ArrayList<String> listaPalabras = new ArrayList<>();
        String contenidoSinSW="";
        String[] stopWrds = {"a","acá","ahí","ajena","ajeno","ajenas","ajenos","al","algo","algún","algúna","algúno","algúnos","algúnas","allá","allí","ambos","ante","antes","aquel","aquella","aquello","aquellas","aquellos","aquí","arriba","así","atrás","aun","aunque","bajo","bastante","bien","cabe","cada","casi","cierto","cierta","ciertos","ciertas","como","con","conmigo","conseguimos","conseguir","consigo","consigue","consiguen","consigues","contigo","contra","cual","cuales","cualquier","cualquiera","cualquieras","cuan","cuando","cuanto","cuanta","cuantos","cuantas","de","dejar","del","demás","demasiada","demasiado","demasiados","demasiadas","dentro","desde","donde","dos","el","él","ella","ellas","ellos","ello","empleáis","emplean","emplear","empleas","empleo","en","encima","entonces","entre","era","eras","eramos","eran","eres","es","esa","ese","eso","esas","eses","esos","esta","estas","estaba","estado","estáis","estamos","están","estar","este","esto","estos","estoy","etc","fin","fue","fueron","fui","fuimos","gueno","ha","hace","haces","hacéis","hacemos","hacen","hacer","hacia","hago","hasta","incluso","intenta","inteas","intentáis","intentamos","intentan","intentar","intento","ir","jamás","junto","juntos","la","lo","las","los","largo","más","me","menos","mi","mis","mía","mías","mientras","mío","míos","misma","mismo","mismas","mismos","modo","mucha","muchas","muchísima","muchisimo","muchisimas","muchisimos","mucho","muchos","muy","nada","ni","ningún","minguna","ninguno","ningunas","ningunos","no","nos","nosotras","nosotros","nuestra","nuestro","nuestras","nuestros","nunca","os","otra","otro","otras","otros","para","parecer","pero","poca","pocas","poco","pocos","podéis","podemos","poder","podría","podrías","podríais","podríamos","podrían","por","por qué","porque","primero","puede","pueden","puedo","pues","que","qué","querer","quién","quienes","quienesquiera","quienquiera","quizá","quizás","sabe","sabes","saben","sabéis","sabemos","saber","se","según","ser","si","sí","siempre","siendo","sin","sino","so","sobre","sois","solamente","solo","sólo","somos","soy","sr","sr.","sra","sra.","sres","sres.","sta","sta.","su","sus","suya","suyo","suyas","suyos","tal","tales","también","tampoco","tan","tanta","tanto","tantas","tantos","te","tenéis","tenemos","tener","tengo","ti","tiempo","tiene","tienen","toda","todo","todas","todos","tomar","trabaja","trabajo","trabajáis","trabajamos","trabajan","trabajar","trabajas","tras","tú","tu","tus","tuya","tuyo","tuyos","tuyas/o/s","último","ultimo","un","una","uno","unas","unos","usa","usas","usáis","usamos","usan","usar","uso","usted","ustedes","va","van","vais","valor","vamos","varias","varios","vaya","verdadera","vosotras","vosotros","voy","vuestra","vuestro","vuestras","vuestros","y","ya","yo"};
        
        //contenido=contenido.trim().replaceAll("\\s+", " ");
        String[] words = contenido.trim().split(" ");

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
        }        
        return contenidoSinSW;
    }
}
