package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/13.
 */
public class Sdcard {
    /**
     * sdcardName :
     * sdcardPath :
     * blockSizeFormat :
     * availCountFormat :
     */

    private String sdcardName;
    private String sdcardPath;
    private String blockCountFormat;
    private String availCountFormat;

    public void setSdcardName(String sdcardName) {
        this.sdcardName = sdcardName;
    }

    public void setSdcardPath(String sdcardPath) {
        this.sdcardPath = sdcardPath;
    }

    public void setBlockCountFormat(String blockCountFormat) {
        this.blockCountFormat = blockCountFormat;
    }

    public void setAvailCountFormat(String availCountFormat) {
        this.availCountFormat = availCountFormat;
    }

    public String getSdcardName() {
        return sdcardName;
    }

    public String getSdcardPath() {
        return sdcardPath;
    }

    public String getBlockCountFormat() {
        return blockCountFormat;
    }

    public String getAvailCountFormat() {
        return availCountFormat;
    }
}
