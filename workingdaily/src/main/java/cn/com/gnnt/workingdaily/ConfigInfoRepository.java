package cn.com.gnnt.workingdaily;

import android.content.Context;

import androidx.annotation.NonNull;
import cn.com.gnnt.workingdaily.VO.WDConfigInfoReqVO;
import cn.com.gnnt.workingdaily.VO.WDConfigInfoRespVO;
import gnnt.mebs.base.http.LoadException;
import gnnt.mebs.base.model.FileRepository;
import gnnt.mebs.common.CommonApp;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class ConfigInfoRepository extends FileRepository<WDConfigInfoRespVO> {

    private static String RESPOSITORY_NAME = "config_info";


    protected WDApi api;
    protected Context mContext;

    public ConfigInfoRepository(@NonNull Context context, WDApi api) {
        super(context, RESPOSITORY_NAME);
        this.mContext = context;
        this.api = api;
    }

    @Override
    public Single<WDConfigInfoRespVO> loadRemoteData() {
        WDConfigInfoReqVO reqVO = new WDConfigInfoReqVO();
        reqVO.sessionID = ((CommonApp)mContext).getSessionID();
        return api.queryConfig(reqVO)
                .map(new Function<WDConfigInfoRespVO, WDConfigInfoRespVO>() {
                    @Override
                    public WDConfigInfoRespVO apply(WDConfigInfoRespVO wdConfigInfoRespVO) throws Exception {
                        if (wdConfigInfoRespVO.retCode >= 0){
                            return wdConfigInfoRespVO;
                        }
                        throw new LoadException(wdConfigInfoRespVO.message);
                    }
                });
    }
}
