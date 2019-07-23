package cn.com.gnnt.workingdaily.Util;

import cn.com.gnnt.workingdaily.VO.Base.WDBaseRespVO;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

public class ResponseTransformer<R extends WDBaseRespVO> implements SingleTransformer<R,R> {
    @Override
    public SingleSource<R> apply(Single<R> upstream) {
        return upstream.onErrorResumeNext(Single.error(new Exception("网络繁忙,请稍后重试")))
                .map(new Function<R, R>() {
                    @Override
                    public R apply(R r) throws Exception {
                        if (r.retCode < 0){
                            throw new Exception(r.message);
                        }
                        return r;
                    }
                });
    }
}
