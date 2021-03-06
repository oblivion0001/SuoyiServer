package com.ilanchuang.xiaoi.suoyiserver.ui.fragment;

import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.SYSApplication;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.event.EditSearchEvent;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.LinkListPresenter;
import com.ilanchuang.xiaoi.suoyiserver.ui.adapter.TodayInAdapter;
import com.ilanchuang.xiaoi.suoyiserver.ui.dialog.DialogNote;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.rong.callkit.CustomRongCallKit;
import io.rong.callkit.RongCallKit;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;
import top.jplayer.baseprolibrary.ui.fragment.SuperBaseFragment;
import top.jplayer.baseprolibrary.utils.ToastUtils;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui.fragment
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class LinkListFragment extends SuperBaseFragment {

    private TodayInAdapter mAdapter;
    List<MultiItemEntity> mEntityList;
    private LinkListPresenter mPresenter;
    private DialogNote mDialogNote;

    @Override
    public int initLayout() {
        return R.layout.layout_refresh_white_hasfoot;
    }

    @Override
    protected void initData(View rootView) {
        initRefreshStatusView(rootView);
        mAdapter = new TodayInAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new LinkListPresenter(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        showLoading();
        if (this.bean != null) {
            responseLinkList(this.bean);
        } else {
            mPresenter.requestLinkList(pagNum + "", searchStr);
        }
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            InListBean.ListBean listBean = (InListBean.ListBean) mAdapter.getData().get(position);
            if (view.getId() == R.id.ivHeaderEdit) {
                mDialogNote = new DialogNote(this.getContext()).setFName(listBean.fname).setFAvatar(listBean.favatar);
                mDialogNote.show(R.id.btnSave, view1 -> {
                    EditText editText = (EditText) view1;
                    mDialogNote.dismiss();
                    mPresenter.requestNote(listBean.fid + "", editText.getText().toString());
                });
            } else {
                AndPermission.with(this)
                        .permission(Permission.CAMERA, Permission.RECORD_AUDIO)
                        .onGranted(permissions -> {
                            mPresenter.requestOut(listBean.fid + "");
                        })
                        .onDenied(permissions -> {
                            AndPermission.hasAlwaysDeniedPermission(mActivity, permissions);
                            ToastUtils.init().showQuickToast("请前往应用设置，同意权限");
                        })
                        .start();
            }
            return false;
        });
        mSmartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (isLoaddigMore) {
                ++pagNum;
                mPresenter.requestLinkList(pagNum + "", searchStr);
            } else {
                mSmartRefreshLayout.finishLoadMore(1000);
                ToastUtils.init().showQuickToast("暂无更多数据");
            }
        });
    }

    public String searchStr = null;
    public int pagNum = 1;

    @Subscribe
    public void onEvent(EditSearchEvent event) {
        if (event.pos == 2) {
            searchStr = event.search;
            mPresenter.requestLinkList(pagNum + "", event.search);
        }
    }

    @Override
    public void refreshStart() {
        searchStr = null;
        pagNum = 1;
        mPresenter.requestLinkList("1", null);
    }


    public void responseNote(BaseBean bean) {
        mPresenter.requestLinkList("" + pagNum, searchStr);
    }

    public InListBean bean;
    public boolean isLoaddigMore = false;

    public void responseLinkList(InListBean inListBean) {
        mSmartRefreshLayout.finishLoadMore();
        this.bean = inListBean;
        isLoaddigMore = inListBean.more;
        initLinkList(inListBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initLinkList(InListBean inListBean) {
        responseSuccess();
        if (pagNum > 1) {
            generateAddData(inListBean.list);
            mAdapter.setNewData(mEntityList);
        } else {
            generateData(inListBean.list);
            mAdapter.setNewData(mEntityList);
        }
    }

    private void generateData(List<InListBean.ListBean> listBeans) {
        if (mEntityList != null) {
            mEntityList.clear();
        } else {
            mEntityList = new ArrayList<>();
        }
        Observable.fromIterable(listBeans).subscribe(listBean -> {
            mEntityList.add(listBean);
            Observable.fromIterable(listBean.notes).subscribe(listBean::addSubItem);
            listBean.removeSubItem(0);
        });
    }

    private void generateAddData(List<InListBean.ListBean> listBeans) {
        Observable.fromIterable(listBeans).subscribe(listBean -> {
            mEntityList.add(listBean);
            Observable.fromIterable(listBean.notes).subscribe(listBean::addSubItem);
            listBean.removeSubItem(0);
        });
    }

    public void responseOut(CallOutBean bean) {
        SYSApplication.callOutBean = bean;
        CustomRongCallKit.startSingleCall(mActivity, bean.family.duid, RongCallKit.CallMediaType
                .CALL_MEDIA_TYPE_VIDEO, bean);
    }
}
