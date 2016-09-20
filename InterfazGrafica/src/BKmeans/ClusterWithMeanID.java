/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BKmeans;

import BKmeans.ClusterWithMean;


/**
 *
 * @author Alejandro
 */
public class ClusterWithMeanID extends ClusterWithMean{
    private String id;
    boolean hoja;
    public ClusterWithMeanID(int vectorsSize) {
        super(vectorsSize);
        hoja=false;
    }

    /**
     *
     * @return
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHoja() {
        return hoja;
    }

    public void setHoja(boolean hola) {
        this.hoja = hoja;
    }
    
}
