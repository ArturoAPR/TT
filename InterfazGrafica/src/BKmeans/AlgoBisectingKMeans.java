package BKmeans;
/* This file is copyright (c) 2008-2015 Philippe Fournier-Viger
 * 
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * 
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

import static Modelo.Algoritmo.fileToPath;
import Vista.InterfazPrincipal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceFunction;
import ca.pfv.spmf.tools.MemoryLogger;
import dbscan.AlgoDBSCAN;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An implementation of the Bisecting K-means algorithm (Steinbach et al, 2000).
 * <br/><br/>
 *
 * "A comparison of document clustering techniques", M. Steinbach, G. Karypis
 * and V. Kumar. Workshop on Text Mining, KDD, 2000.<br/><br/>
 *
 * The Bisecting K-Means algorithm is a variation of the regular K-Means
 * algorithms. It consists of the following steps: (1) pick a cluster, (2) find
 * 2-subclusters using the basic K-Means algorithm (bisecting step), (3) repeat
 * step 2, the bisecting step, for ITER times and take the split that produces
 * the clustering, (4) repeat steps 1,2,3 until the desired number of clusters
 * is reached.
 * <br/><br/>
 *
 * In this implementation, we use the Squared Sum of Errors (SSE) to determine
 * if a split is good. Moreover, we always choose to split the largest cluster
 * as suggested by Steinbach et al. However, note that an alternative way would
 * be to always choose the cluster with the highest SSE. But we have not done
 * that.
 * <br/><br/>
 *
 * @author Philippe Fournier-Viger
 * @see AlgoKMeans
 */
public class AlgoBisectingKMeans extends AlgoKMeans {

    /**
     * the number times a split should be repeated to choose the best one
     */
    int iter = -1;
    String papa;
    List<ClusterWithMeanID> clustersID;

    /**
     * Default constructor
     */
    public AlgoBisectingKMeans() {

    }

    /**
     * Run the K-Means algorithm
     *
     * @param inputFile an input file path containing a list of vectors of
     * double values
     * @param k the parameter k
     * @param distanceFunction a distance function
     * @param iter the number times a split should be repeated to choose the
     * best one
     * @return a list of clusters (some of them may be empty)
     * @throws IOException exception if an error while writing the file occurs
     */
    public List<ClusterWithMeanID> runAlgorithm(String inputFile, int k,
            DistanceFunction distanceFunction, int iter) throws NumberFormatException, IOException {
        this.iter = iter;
        return runAlgorithm(inputFile, k, distanceFunction);
    }

    /**
     * Apply the K-means algorithm
     *
     * @param k the parameter k
     * @param distanceFunction a distance function
     * @param vectors the list of initial vectors
     * @param minValue the min value
     * @param maxValue the max value
     * @param vectorsSize the vector size
     */
    void applyAlgorithm(int k, DistanceFunction distanceFunction,
            List<DoubleArrayName> vectors, double minValue, double maxValue,
            int vectorsSize) {

        clusters = new ArrayList<ClusterWithMeanID>();
        clustersID = new ArrayList<ClusterWithMeanID>();
        List<DoubleArrayName> currentVectors = vectors;
        List<DoubleArrayName> currentVectorsID = vectors;
        clustersID.addAll(applyKMeans(1, distanceFunction, currentVectors, minValue, maxValue, vectorsSize));
        clustersID.get(0).setId("1");
        papa = "1";
        int ka = k;
        while (true) {
            try {
                int biggestClusterSize = -1;
                int biggestClusterSizeID = -1;
                // apply kmeans iter times and keep the best clusters
                List<ClusterWithMeanID> bestClustersUntilNow = null;
                double smallestSSE = Double.MAX_VALUE;

                String input;
                input = fileToPath(System.getProperty("user.home") + "/TT/archivos/peso/DocPesosCluster.txt");
                String separator = " ";
                int minPts = 1;
                double epsilon = 100d;

                        // Apply KMEANS with K = 2  "iter" times
                // and select the partition with the best SSE (Sum of Squared errors)
                
                for (int i = 0; i < iter; i++) {
                    List<ClusterWithMeanID> newClusters = applyKMeans(ka, distanceFunction, currentVectors, minValue, maxValue, vectorsSize);
                    double sse = ClustersEvaluation.calculateSSE(newClusters, distanceFunction);
                    if (sse < smallestSSE) {
                        bestClustersUntilNow = newClusters;
                        smallestSSE = sse;
                    }
                }
                System.out.println("CENTRO: "+bestClustersUntilNow);
                int numclustervacios = 0;
                for (int x = 0; x < bestClustersUntilNow.size(); x++) {
                    if (bestClustersUntilNow.get(x).getVectors().size() <= 0) {
                        bestClustersUntilNow.remove(x);
                        x--;
                        //numclustervacios++;
                    }
                }
                if(bestClustersUntilNow.size()==biggestClusterSize){
                    break;
                }
                // add the best 2 clusters to the list of all clusters until now
                clusters.addAll(bestClustersUntilNow);
                for (int w = 0; w < bestClustersUntilNow.size(); w++) {
                    bestClustersUntilNow.get(w).setId((papa + "/" + (w + 1)).replaceAll("//", "/"));
                }
                        //bestClustersUntilNow.get(1).setId((papa+"/2").replaceAll("//", "/"));

                clustersID.addAll(bestClustersUntilNow);

                        // if we have enough clusters, we stop
               /* if(clusters.size() >= k){
                 break;
                 }*/
                // otherwise, we choose the next cluster to be bisected.
                
                int biggestClusterIndex = -1;
                for (int i = 0; i < clusters.size(); i++) {

                    ClusterWithMeanID cluster = clusters.get(i);
                    // if the biggest cluster until now, we remember it
                    if (cluster.getVectors().size() > biggestClusterSize) {
                        biggestClusterIndex = i;
                        biggestClusterSize = cluster.getVectors().size();
                        currentVectors = cluster.getVectors();
                        //  papa=papa+"."+clusters.get(i).getId();
                    }
                }
                
                // remove the cluster from the list of clusters because we will split it
                papa = papa.replaceAll("//", "/");
                papa = "" + clusters.get(biggestClusterIndex).getId() + "/";
                System.out.println("PAPA: " + papa);
                
                clusters.remove(biggestClusterIndex);//segun yo no lo tiene que borrar aqui sino mas arriba en el codigo
                
                int biggestClusterIndexID = -1;
                for (int i = 0; i < clustersID.size(); i++) {

                    ClusterWithMeanID cluster = clustersID.get(i);
                    // if the biggest cluster until now, we remember it
                    if (cluster.getVectors().size() > biggestClusterSizeID) {
                        biggestClusterIndexID = i;
                        biggestClusterSizeID = cluster.getVectors().size();
                        currentVectorsID = cluster.getVectors();
                        //  papa=papa+"."+clusters.get(i).getId();
                    }
                }
           //     for (DoubleArrayName dan:clustersID.get(biggestClusterIndexID).getVectors())
                    clustersID.get(biggestClusterIndexID).vectors.clear();;
              //  for(int o = 0 ; o < clustersID.get(biggestClusterIndexID).getVectors().size();o++ )                   
                    
                 //
                
                if (clustersID.size() > 0) {
                    File DocPesos = new File(System.getProperty("user.home") + "/TT/archivos/peso/DocPesosCluster.txt");
                    BufferedWriter writer = null;

                    writer = new BufferedWriter(new FileWriter(DocPesos)); //CREAMOS EL ESCRITOR

                       for(int i=0; i<currentVectors.size();i++){
                           String reenglon2 = "[File]" + currentVectors.get(i).toString().replaceAll(",", " ")+"\n";
                           System.out.println(reenglon2);
                           writer.append(reenglon2);// write(renglon);                
                           writer.flush();
                       }
                    writer.close();
                    AlgoDBSCAN algoDBS = new AlgoDBSCAN();
                    InterfazPrincipal.labelStatus1.setText("Ejecutando DBSCAN ");
                    System.out.println("Ejecutando DBSCAN");
                    algoDBS.runAlgorithm(input, minPts, epsilon, separator);
                    //                        algoDBS.saveToFile(output2);
                    algoDBS.printStatistics();
                    // we request 3 clusters
                    ka = algoDBS.obtenerNumClusters();
                    System.out.println(" K de cada cluster= " + ka);
                 /*   if(ka==1){
                        break;
                    }*/

                }
                
                /*for(int i=0;i<clustersID.size();i++){
                  if(clustersID.get(i).getVectors()==currentVectors){
                           
                  clustersID.get(i).vectors=null;
                  }    
                }*/
                
                /*/if (numclustervacios == bestClustersUntilNow.size()) {
                    break;
                }*/
                //clustersID.remove(biggestClusterIndex);
                if (clusters.size()<=1 || ka==1 || clusters.size() >= ka) {
                    break;
                }

                        //   clustersID.remove(biggestClusterIndex);
                //clustersID.get(biggestClusterIndex).setHoja(false);
                        /*  for(int i=0; i< clusters.size(); i++){
                 clustersRoot.add(clusters.get(i));
                 //  System.out.println("Root "+clusters.get(i).getVectors().toString());
                 }*/
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AlgoBisectingKMeans.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AlgoBisectingKMeans.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
/***************************************/

/*
        String hora = new Date().toString().trim().replaceAll(" ", "_").replace(":", ".");
        String ruta = System.getProperty("user.home") + "\\TT\\DENDOGRAMAS\\DENDOGRAMA_" + hora + "\\";
        System.out.println(ruta);
        File c = new File(ruta);
        c.mkdirs();

        for (int i = 0; i < clustersID.size(); i++) {
            System.out.println(clustersID.get(i).getId() + " - " + clustersID.get(i).getVectors());
            System.out.println("" + clustersID.get(i).isHoja());
            // if (clustersID.get(i).isHoja()) 
            for (int j = 0; j < clustersID.get(i).getVectors().size(); j++) {
                System.out.println("-----" + clustersID.get(i).getVectors().get(j).getNombreArchivo());
            }
            c = new File(ruta + clustersID.get(i).getId() + "/");//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
            c.mkdirs();

            if (clustersID.get(i).getVectors().size() >= 1) {

                //  if (clustersID.get(i).isHoja()) {
                for (int w = 0; w < clustersID.get(i).getVectors().size(); w++) {
                    c = new File(ruta + clustersID.get(i).getId() + "/" + clustersID.get(i).getVectors().get(w).getNombreArchivo());//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT
                    //    clustersID.get(i).getVectors().remove(clustersID.get(i).getVectors().size());

                    try {
                        c.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(AlgoBisectingKMeans.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                        //c=new File(ruta+clustersID.get(i).getId()+"/"+clustersID.get(i).getVectors().get(0).getNombreArchivo());//File(ficheros[x]+".txt");//CREAMOS ARCHIVO CON NOMBRE ORIGINAL PERO EN TXT

                //   }
            } else {

            }
        }*/
        /***************************************/
    }
    
    public int getNombreCarpeta(DoubleArrayName vector){
        double max=0;
        String nombreCarpeta="";
        int index=0;
        for(int i=0;i<vector.data.length;i++){
            if(max<vector.data[i]){
                max=vector.data[i];
                index=i;
            }
        }
        
        return index;
    }
  
            
    
    public List<ClusterWithMeanID> getClustersID(){
        return clustersID;
    }

    /**
     * Print statistics of the latest execution to System.out.
     */
    public void printStatistics() {
        System.out.println("========== BISECTING KMEANS - STATS ============");
        System.out.println(" Distance function: " + distanceFunction.getName());
        System.out.println(" Total time ~: " + (endTimestamp - startTimestamp)
                + " ms");
        System.out.println(" SSE (Sum of Squared Errors) (lower is better) : " + ClustersEvaluation.calculateSSE(clusters, distanceFunction));
        System.out.println(" Max memory:" + MemoryLogger.getInstance().getMaxMemory() + " mb ");
        System.out.println(" Number of clusters: " + clusters.size());
        System.out.println("=====================================");
    }

}
