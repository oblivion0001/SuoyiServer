package com.ilanchuang.xiaoi.suoyiserver.mvpbe.model;

import com.ilanchuang.xiaoi.suoyiserver.BuildConfig;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.AppDownLoadBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallMessageBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.HealthDataBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.LoginBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.RecordUserInfoBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TypeNumBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import top.jplayer.baseprolibrary.mvp.model.BaseModel;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;
import top.jplayer.baseprolibrary.net.retrofit.IoMainSchedule;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.model
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class ServerModel extends BaseModel<SYServer> {
    public ServerModel(Class<SYServer> t) {
        super(t);
    }

    public Observable<UserInfoBean> requestUserInfo() {
        return mServer.requestUserInfo().compose(new IoMainSchedule<>());
    }

    public Observable<BaseBean> requestLogout() {
        return mServer.logout().compose(new IoMainSchedule<>());
    }

    public Observable<TypeNumBean> requestTypeNum() {
        return mServer.requestTypeNum().compose(new IoMainSchedule<>());
    }

    public Observable<InListBean> requestInList(String flag, String fname, String date) {
        return mServer.requestInList(flag, fname, date).compose(new IoMainSchedule<>());
    }

    public Observable<InListBean> requestOutList(String flag, String fname, String date) {
        return mServer.requestOutList(flag, fname, date).compose(new IoMainSchedule<>());
    }

    public Observable<InListBean> requestLinkList(String pageNum, String fname) {
        return mServer.requestLinkList(pageNum, fname).compose(new IoMainSchedule<>());
    }

    public Observable<BaseBean> requestNote(String fid, String note) {
        return mServer.requestSaveNode(fid, note).compose(new IoMainSchedule<>());
    }

    public Observable<CallOutBean> requestOut(String fid) {
        return mServer.requestOut(fid).compose(new IoMainSchedule<>());
    }

    public Observable<AppDownLoadBean> requestDownload() {
        return mServer.download(BuildConfig.APPLICATION_ID).compose(new IoMainSchedule<>());
    }

    public Observable<CallOutBean> requestIn(String duid) {
        return mServer.requestIn(duid).compose(new IoMainSchedule<>());
    }

    public Observable<BaseBean> freebusy(String free) {
        return mServer.freebusy(free).compose(new IoMainSchedule<>());
    }

    public Observable<RecordUserInfoBean> requestRecordUserInfo(String fid) {
        return mServer.requestRecordUserInfo(fid).compose(new IoMainSchedule<>());
    }

    public Observable<CallMessageBean> requestCallMessage(String fid) {
        return mServer.requestUserMessage(fid).compose(new IoMainSchedule<>());
    }

    public Observable<HealthDataBean> requestDate(String fid) {
        return mServer.requestData(fid).compose(new IoMainSchedule<>());
    }

    public Observable<BaseBean> requestOnline(String online) {
        return mServer.requestOnOff(online).compose(new IoMainSchedule<>());
    }

    public Observable<BaseBean> requestSaveLog(Map<String, String> map) {
        return mServer.requestSaveLog(map).compose(new IoMainSchedule<>());
    }

    public Observable<LoginBean> requestLogin(String account, String password) {
        return mServer.requestLogin(account, password).compose(new IoMainSchedule<>());
    }


}
