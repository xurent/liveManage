package com.xurent.live.service;

import com.xurent.live.model.FocusAnchor;
import com.xurent.live.model.out.OutFansInfo;
import com.xurent.live.model.out.UserInfo;

import java.util.List;

public interface AnchorService {

    /**
     * 关注
     * @param anchor
     */
    public void Like(FocusAnchor anchor);


    /**
     * 主播获取粉丝
     * @param aid
     * @return
     */
    public List<OutFansInfo> getFansByAid(String aid);

    /**
     * 获取关注的主播
     * @param uid
     * @return
     */
    public List<UserInfo> getAnchorsByUid(String uid);

    /**
     * 取消关注
     * @param uid
     * @param Aid
     */
    public void UnLikeByUidAndAid(String uid, String Aid);


    public boolean isExit(String uid,String aid);


    public List<OutFansInfo> getMoneyFans(String aid);

    public boolean isFoucus(String aid,String uid);


    public long GiveAcount(String aid,String uid,long acount);


}
