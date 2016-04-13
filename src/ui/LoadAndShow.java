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
package ui;

/**
 *
 * @author diego
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class LoadAndShow extends JPanel {

    BufferedImage image;
    Dimension size = new Dimension();

    public LoadAndShow(BufferedImage image) {
        this.image = image;
        size.setSize(image.getWidth(), image.getHeight());
    }

    /**
     * Drawing an image can allow for more flexibility in processing/editing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Center image in this component.
        int x = (getWidth() - size.width) / 2;
        int y = (getHeight() - size.height) / 2;
        g.drawImage(image, x, y, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

}
