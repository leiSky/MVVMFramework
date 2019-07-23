package cn.com.gnnt.workingdaily.VO;

import com.google.gson.annotations.SerializedName;

import cn.com.gnnt.workingdaily.VO.Base.WDBaseRespVO;

public class WDVerifyImageRespVO extends WDBaseRespVO {

    /**
     * sessionID(cookie)
     */
    @SerializedName("cookie")
    public String sessionID;

    /**
     * 验证码Base64字符串
     */
    @SerializedName("imageBase64")
    public String verifyImgString;

    public WDVerifyImageRespVO(int retCode, String message) {
        super(retCode, message);
    }
}
