/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import BKmeans.AlgoBisectingKMeans;
import BKmeans.AlgoKMeans;
import BKmeans.ClusterWithMeanID;
import Vista.InterfazPrincipal;
import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceCorrelation;
import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceCosine;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceEuclidian;
import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceFunction;
import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceJaccard;
import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceManathan;
import dbscan.AlgoDBSCAN;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class Algoritmo {
    int k;
    int minPts;
    double epsilon;
    
    public List<ClusterWithMeanID>  ejecutaAlgoritmo() {
        try {
            String input = fileToPath(System.getProperty("user.home") + "/TT/archivos/peso/DocPesos.txt");           
            String output1 = fileToPath(System.getProperty("user.home") + "/TT/archivos/RESULTADOS/outputBisectingKMEans.txt");
            String output2 = fileToPath(System.getProperty("user.home") + "/TT/archivos/RESULTADOS/outputDBSCan.txt");
            String output3 = fileToPath(System.getProperty("user.home") + "/TT/archivos/RESULTADOS/outputKMEans.txt");
            
                minPts=1;
		epsilon = 100d;
		
		// We specify that in the input file, double values on each line are separated by spaces
		String separator = " ";
		
		// Apply the algorithm
		AlgoDBSCAN algoDBS = new AlgoDBSCAN();  
		InterfazPrincipal.labelStatus1.setText("Ejecutando DBSCAN ");
                System.out.println("Ejecutando DBSCAN");
		algoDBS.runAlgorithm(input, minPts, epsilon, separator);
		
		algoDBS.saveToFile(output2);
                algoDBS.printStatistics();
		// we request 3 clusters
		k=algoDBS.obtenerNumClusters();
                System.out.println("K Raiz: "+k);
                //k=3;
		
		// the iter parameter specify how much times the algorithm should
		// repeat a split to keep the best split. If it is set to a high value
		// it should provide better results but it should be more slow.
		// Splits are evaluated using the Squared Sum of Errors (SSE).
		int iter = 1000;
		
		// Here we specify that we want to use the euclidian distance
		DistanceFunction distanceFunction = new DistanceEuclidian(); 
		// Alternative distance functions are also available such as:
//		DistanceFunction distanceFunction = new DistanceManathan(); 
//		DistanceFunction distanceFunction = new DistanceCosine(); 
//		DistanceFunction distanceFunction = new DistanceCorrelation(); 
//		DistanceFunction distanceFunction = new DistanceJaccard(); 

/*
                InterfazPrincipal.labelStatus1.setText("Ejecutando KMeans con una K de "+k);
                System.out.println("Ejecutando KMeans con una K de "+k);
                AlgoKMeans km = new AlgoKMeans();
                km.runAlgorithm(input, k, distanceFunction);                
                km.saveToFile(output3);
                km.printStatistics();	
*/

		// Apply the algorithm
                InterfazPrincipal.labelStatus1.setText("Ejecutando Bisecting KMeans con una K de "+k);
                System.out.println("Ejecutando Bisecting KMeans con una K de "+k);
		AlgoBisectingKMeans algoKM = new AlgoBisectingKMeans();  
		algoKM.runAlgorithm(input, k, distanceFunction, iter);	
                List<ClusterWithMeanID> clustersID=algoKM.getClustersID();
		algoKM.saveToFile(output1);
                algoKM.printStatistics();
                
                
                return clustersID;
                
                

              
                
        } catch (NumberFormatException | IOException ex) {
            Logger.getLogger(Algoritmo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {

        return java.net.URLDecoder.decode(filename, "UTF-8");
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

}
