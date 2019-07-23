package cn.com.gnnt.workingdaily.VO.Base;

import com.google.gson.annotations.SerializedName;

public class WDBaseRespVO {

    /**
     * 返回码
     */
    @SerializedName("resultCode")
    public int retCode;
    /**
     * 返回消息
     */
    public String message;

    public WDBaseRespVO(int retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }
}
