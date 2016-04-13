/*
 * Copyright(c) 2010-2011, Diego Schmaedech Martins (UFSM, Federal University of Santa Maria, Brazil).
 *
 *
 * All rights reserved.
 *
 * COMMERCIAL USE:
 * This library is part of Mothorus Eye Tracker developed under
 *                  GNU LESSER GENERAL PUBLIC LICENSE
 *                   Version 3, 29 June 2007 License
 * If you have any commercial interest in this work please contact schmadech@gmail.com
 *
 *


 IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.

 By downloading, copying, installing or using the software you agree to this license.
 If you do not agree to this license, do not download, install, copy or use the software.


 Intel License Agreement
 For Open Source Computer Vision Library

 Copyright (C) 2000, Intel Corporation, all rights reserved.
 Third party copyrights are property of their respective owners.

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:

 * Redistribution's of source code must retain the above copyright notice,
 this list of conditions and the following disclaimer.

 * Redistribution's in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * The name of Intel Corporation may not be used to endorse or promote products
 derived from this software without specific prior written permission.

 This software is provided by the copyright holders and contributors "as is" and
 any express or implied warranties, including, but not limited to, the implied
 warranties of merchantability and fitness for a particular purpose are disclaimed.
 In no event shall the Intel Corporation or contributors be liable for any direct,
 indirect, incidental, special, exemplary, or consequential damages
 (including, but not limited to, procurement of substitute goods or services;
 loss of use, data, or profits; or business interruption) however caused
 and on any theory of liability, whether in contract, strict liability,
 or tort (including negligence or otherwise) arising in any way out of
 the use of this software, even if advised of the possibility of such damage.
 *
 */

package jmeanshift;

import java.awt.color.ColorSpace;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster; 
import java.util.Iterator;
import java.util.Vector;

/**
 * Created on Oct 27, 2010, 11:43:06 PM
 * @author Diego Schmaedech
 */
public class MeanShift {

    private Vector<Vector> dataset = new Vector<Vector>();
    public Vector<double[]> clusterCenter = new Vector<double[]>();
    private Vector<Integer> indexToVisit = new Vector<Integer>();
    private Vector<Integer> visitedPoints = new Vector<Integer>();
    private Vector<Integer> indexedClusters = new Vector<Integer>();
    private Vector<Vector> clusterVotes = new Vector<Vector>();
    private Vector<Integer> thisClusterVotes = new Vector<Integer>();
    
     
    private Vector<Integer> indexOnSquaredDistToAll = new Vector<Integer>();
    private Vector<Integer> kMembers = new Vector<Integer>();
    public Vector<Integer> clustersMembers = new Vector<Integer>();

    private double bandwidth = 0;
    private double breakCriteria = 0;
    private int numDimension = 0;
    private int numPoints = 0;
    private int kClusters = 0;
    private int pointsToVotes = 0;

    private double[] lastKMean = new double[0];
    private double[] currentKMean = new double[0];
    private boolean LOGGER = false;
    private boolean randomMode = false;
    private boolean mergeClusters = true;

    public MeanShift(){

    }

    private void init(){
        //impa as variavaies caso o metodo seja chamado novamente

        clusterCenter = new Vector<double[]>();
        indexToVisit = new Vector<Integer>();
        visitedPoints = new Vector<Integer>();
        indexedClusters = new Vector<Integer>();
        clusterVotes = new Vector<Vector>();
        thisClusterVotes = new Vector<Integer>();

        indexOnSquaredDistToAll = new Vector<Integer>();
        kMembers = new Vector<Integer>(); 

        kClusters = 0;

        //inicializa as coisas
        numPoints = dataset.get(0).size();
        numDimension = dataset.size();
        pointsToVotes = dataset.get(0).size();

        for(int x = 0; x < numPoints; x++){
            indexToVisit.add(x);
            thisClusterVotes.add(0);
            visitedPoints.add(0);
            indexedClusters.add(0);
        }
        currentKMean = new double[numDimension];
        lastKMean = new double[numDimension];
    }
    public BufferedImage doMeanShiftWithImages(Vector<BufferedImage> images, double bandwidth){
        setBandwidth(bandwidth);
        Vector<Integer> set = new Vector<Integer>();
        int w = images.get(0).getWidth();
        int h = images.get(0).getHeight();
        Iterator<BufferedImage> it = images.iterator();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        while(it.hasNext()){
            BufferedImage src = it.next();
            ColorModel cm =  src.getColorModel(); 
            if( cm.getPixelSize() != 8 ){
        	//JOptionPane.showMessageDialog( null, "A imagem será convertida 24 bits->8 bits!" );
                ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                ColorConvertOp op = new ColorConvertOp(cs, null);
                src = op.filter((BufferedImage)it.next(), null);
            }

            for( int c = 0; c < src.getHeight(); c++){
                for( int r = 0; r < src.getWidth(); r++){
            	  set.add(src.getData().getSample(r,c,0));
                }
            }
            dataset.add(set);
        }
         float[][] matrix = {
      { 1, 0, 0, 0 },
      {  0, 1, 0, 0 },
      {  0, 0, 1, 0 }
    };
    BandCombineOp op = new BandCombineOp(matrix, null);


    Raster source = result.getRaster();
    WritableRaster destination = op.filter(source, null);

    // Create a destination image using the processed
    //   raster and the same color model as the source image.
    
         doMeanShift(dataset,bandwidth,bandwidth/1000f, false);
         int k = 0;

         for( int c = 0; c < result.getHeight(); c++){
            for( int r = 0; r < result.getWidth(); r++){                
               // result.setRGB(r,c, 255/(indexedClusters.get(k)+1));
               // result.getData().(r,c, (int) (255 * Math.random()));
               destination.setSample(r, c, 0, (int) (255/(indexedClusters.get(k)+1)));
               destination.setSample(r, c, 1, (int) (255-(255/(indexedClusters.get(k)+1))));
               destination.setSample(r, c, 2, (int) (100/(indexedClusters.get(k)+1)));
               k++;
            }
         }
        BufferedImage dst = new BufferedImage(result.getColorModel(), destination, false, null);
        return dst;
        
    }

    public void doMeanShift(Vector<Vector> dataset, double bandwidth, double breakCriteria, boolean randomMode ){
        setDataset(dataset);
        setBandwidth(bandwidth);
        setBreakCriteria(breakCriteria);
        setRandomMode(randomMode);
        init();
        long start = System.currentTimeMillis();

        while (pointsToVotes!=0){
            logger("\n points to votes: " + pointsToVotes);
            for(int x = 0; x < numPoints; x++){ 
                thisClusterVotes.setElementAt(0,x);
            }

            int randomix = 0;
            if(isRandomMode()){
                randomix = (int)Math.ceil( (indexToVisit.size()-1)*Math.random() );
                logger("\n random mode: ON");
            }
            int startIndex = indexToVisit.get(randomix);
            logger("\n current k mean:");
            for(int y = 0; y < numDimension; y++){
                currentKMean[y] = Double.parseDouble(dataset.get(y).get(startIndex).toString());
                logger(" "+ currentKMean[y]);
            }

            while(true){//loop untill convergence
                indexOnSquaredDistToAll = new Vector<Integer>();

                for(int x = 0; x < numPoints; x++){
                    double sum = 0d;                     
                    for(int y = 0; y < numDimension; y++){
                        sum += Math.pow(currentKMean[y] - Double.parseDouble(dataset.get(y).get(x).toString()),2);
                    }
                     
                    if(Math.sqrt(sum) < bandwidth){
                         indexOnSquaredDistToAll.add(x);
                    }
                    
                }
                logger("\n indexOnSquaredDistToAll: "+ indexOnSquaredDistToAll);
                for(int i = 0; i < indexOnSquaredDistToAll.size(); i++){
                    int votes = thisClusterVotes.get(indexOnSquaredDistToAll.get(i));
                    thisClusterVotes.setElementAt(votes+1,indexOnSquaredDistToAll.get(i));
                }
                logger("\n thisClusterVotes: "+ thisClusterVotes);
                System.arraycopy(currentKMean, 0, lastKMean, 0, numDimension);
                logger("\n current K mean: ");
                for(int y = 0; y < numDimension; y++){
                    double sum = 0d;
                    for(int d = 0; d < indexOnSquaredDistToAll.size(); d++){
                        sum += Double.parseDouble(dataset.get(y).get(indexOnSquaredDistToAll.get(d)).toString());
                        indexedClusters.setElementAt( getkClusters(), indexOnSquaredDistToAll.get(d));
                    }
                    currentKMean[y] = sum/indexOnSquaredDistToAll.size(); 
                    logger(" "+ currentKMean[y]);
                }

                for(int i = 0; i < indexOnSquaredDistToAll.size(); i++){
                    kMembers.add(indexOnSquaredDistToAll.get(i));
                }

                logger("\n kMembers: "+ kMembers);
                for(int i = 0; i < kMembers.size(); i++){
                     visitedPoints.setElementAt(1, kMembers.get(i));
                }
                if(norm(currentKMean,lastKMean,numDimension) < breakCriteria){
                    int mergeKCluster =-1;
                    if(isMergeClusters()){
                        for(int k = 0; k < getkClusters(); k++){
                            double distanceToOther = norm(currentKMean,clusterCenter.get(k),numDimension);
                            logger("\n distance to other: "+ distanceToOther);
                            if(distanceToOther < bandwidth/2d){
                                mergeKCluster = k;
                                logger("\n merge cluster: "+ mergeKCluster);
                                break;
                            }
                        }
                    }
                    if(mergeKCluster > -1){
                        clusterCenter.setElementAt(biased(currentKMean,clusterCenter.get(mergeKCluster),numDimension ),mergeKCluster);
                        Vector<Integer> biasedClusterVotes = new Vector<Integer>();
                        for(int d = 0; d < thisClusterVotes.size(); d++){
                            biasedClusterVotes.add((int)Double.parseDouble(clusterVotes.get(mergeKCluster).get(d).toString())+thisClusterVotes.get(d));
                        }
                        clusterVotes.setElementAt(biasedClusterVotes, mergeKCluster);
                    }else{
                        kClusters++;
                        double[] tempCurrentKMean = new double[numDimension];
                        System.arraycopy(currentKMean, 0, tempCurrentKMean, 0, numDimension);
                        clusterCenter.add(tempCurrentKMean);
                        Vector<Integer> copyThisClusterVotes = new Vector<Integer>(thisClusterVotes);
                        clusterVotes.add(copyThisClusterVotes);
                    }
                    break;
                }

            }//end loop untill convergence
            indexToVisit = new Vector<Integer>();
            for(int f = 0; f < visitedPoints.size(); f++){
                if(visitedPoints.get(f)==0){
                    indexToVisit.add(f);
                }
            }
            pointsToVotes = indexToVisit.size();
        }

        clustersMembers = new Vector<Integer>();
        for(int t = 0; t < numPoints; t++){
            int[] max = new int[clusterVotes.size()];
            for(int f = 0; f < clusterVotes.size(); f++){
                max[f] =  (int) Math.ceil(Double.parseDouble(clusterVotes.get(f).get(t).toString())) ;
            }
            int indexMaxVotes = indexMax(max);
            clustersMembers.add(indexMaxVotes);
        }
        System.out.println("\n\n");
        logger("\n\n clusters members: " + clustersMembers);
        logger("\n indexed clusters: "+ indexedClusters);

        long now = System.currentTimeMillis();
        logger("\n total time: " + (now-start) );
    }

    private void logger(String log){
        if(LOGGER) System.out.print(log);
    }
    public static int indexMax(int[] a) {
        int max = Integer.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
                index = i;
            }
        }
        return index;
    }
    
    private double norm(double[] a, double[] b, int dimension){
        double result = 0d;
        for(int i = 0; i < dimension; i++){
             result += Math.pow(a[i]-b[i],2);
        }

        return Math.sqrt(result);
    }

    private double[] biased(double[] a, double[] b, int dimension){
        double[] result = new double[dimension];
        for(int i = 0; i < dimension; i++){
            result[i]= 0.5*(a[i]+b[i]);
        }

        return result;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(Vector<Vector> dataset) {
        this.dataset = dataset;
    }

    /**
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }


    /**
     * @param breakCriteria the breakCriteria to set
     */
    public void setBreakCriteria(double breakCriteria) {
        this.breakCriteria = breakCriteria;
    }

    /**
     * @return the bandwidth
     */
    public double getBandwidth() {
        return bandwidth;
    }

    /**
     * @return the breakCriteria
     */
    public double getBreakCriteria() {
        return breakCriteria;
    }

    /**
     * @return the kClusters
     */
    public int getkClusters() {
        return kClusters;
    }

    /**
     * @return the randomMode
     */
    public boolean isRandomMode() {
        return randomMode;
    }

    /**
     * @param randomMode the randomMode to set
     */
    public void setRandomMode(boolean randomMode) {
        this.randomMode = randomMode;
    }

    /**
     * @return the mergeClusters
     */
    public boolean isMergeClusters() {
        return mergeClusters;
    }

    /**
     * @param mergeClusters the mergeClusters to set
     */
    public void setMergeClusters(boolean mergeClusters) {
        this.mergeClusters = mergeClusters;
    }
}
