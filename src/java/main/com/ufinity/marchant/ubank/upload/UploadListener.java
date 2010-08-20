// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:
//
// -------------------------------------------------------------------------
// UFINITY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
// THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
// TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UFINITY SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
// MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
// THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
// CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
// PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
// NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
// SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
// SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
// PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). UFINITY
// SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
// HIGH RISK ACTIVITIES.
// -------------------------------------------------------------------------
package com.ufinity.marchant.ubank.upload;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.log4j.Logger;

/**
 * 
 * upload listener
 * 
 * @author liujun
 * @version 1.0
 * @since 2010-8-20
 */
public class UploadListener implements ProgressListener {
    
    private Logger logger = Logger.getLogger(UploadListener.class);

    private static final long UPDATE_THRESHOLD = 100 * 1024L;  //kb

    private long megaBytes = -1;

    private ProgressInfo pi = null;

    public UploadListener() {
    }

    /**
     * UploadListener
     * 
     * @param pi ProgressInfo
     */
    public UploadListener(ProgressInfo pi) {
        this.pi = pi;
    }

    /**
     *
     * @see org.apache.commons.fileupload.ProgressListener#update(long, long, int)
     */
    public void update(long pBytesRead, long pContentLength, int pItems) {
        try {
            //TODO remove
            Thread.sleep(10);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        while(pi.isPause()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}
        }
        
        long mBytes = pBytesRead / UPDATE_THRESHOLD;
        if (megaBytes == mBytes) {
            return;
        }
        megaBytes = mBytes;
        pi.setBytesRead(pBytesRead);
        pi.setFileIndex(pItems);
        pi.setTotalSize(pContentLength);
        pi.setCurrentTime(System.currentTimeMillis());
        
        //TODO remove log
        if (pContentLength == -1) {
            logger.debug("So far, " + pBytesRead
                    + " bytes have been read.");
            
            System.out.println("So far, " + pBytesRead
                    + " bytes have been read.");
        } else {
            logger.debug("So far, " + pBytesRead + " of "
                    + pContentLength + " bytes have been read.");
            
            System.out.println("So far, " + pBytesRead + " of "
                    + pContentLength + " bytes have been read.");
        }
    }

}
