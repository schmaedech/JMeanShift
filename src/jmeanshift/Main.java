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

//package jmeanshift;
//
//import java.util.Vector;
//
///**
// * Created on Oct 27, 2010, 11:43:06 PM
// * @author Diego Schmaedech
// */
//public class Main {
//
//
//    private static Vector<Double> data1 = new Vector<Double>();
//    private static Vector<Double> data2 = new Vector<Double>();
//    private static Vector<Double> data3 = new Vector<Double>();
//    private static Vector<Double> data4 = new Vector<Double>();
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//
////        for(int y = 0; y < 50; y++){
////             data1.add(Math.random()*10);
////             data2.add(Math.random()*10);
////        }
////        for(int y = 50; y < 100; y++){
////             data1.add(Math.random()*70);
////             data2.add(Math.random()*70);
////        }
//        data1.add(2d);
//        data1.add(3d);
//        data1.add(4d);
//        data1.add(8d);
//        data1.add(9d);
//        data1.add(10d);
//
//
//        data2.add(3d);
//        data2.add(1d);
//        data2.add(4d);
//        data2.add(8d);
//        data2.add(9d);
//        data2.add(8d);
//
//
//        Vector<Vector> dataset = new Vector<Vector>();
//        dataset.add(data1);
//        dataset.add(data2);
//       // dataset.add(data3);
//        //dataset.add(data4);
//        MeanShift domean = new MeanShift();
//        domean.doMeanShift(dataset, 2d, 0.002, false);
//        for(int y = 0; y < domean.clustersMembers.size(); y++){
//             //System.out.println("index "+ y +" cluster: " + domean.getIndexedClusters().get(y));
//        }
//    }
//
//
//}
