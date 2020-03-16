package com.xurent.live.service.lmpl;

import com.xurent.live.dao.AcountDao;
import com.xurent.live.dao.AnchorDao;
import com.xurent.live.dao.UserDao;
import com.xurent.live.model.Acounnt;
import com.xurent.live.model.FocusAnchor;
import com.xurent.live.model.User;
import com.xurent.live.model.out.OutFansInfo;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.AnchorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Transactional
@Service("anchorService")
public class AnchorServicelmpl implements AnchorService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnchorDao anchorDao;


    @Autowired
    private AcountDao acountDao;

    @Override
    public void Like(FocusAnchor anchor) {

        if(isExit(anchor.getUid(),anchor.getAid())){
            anchor.setType(1);
            anchorDao.updateByUidandAid(anchor.getUid(),anchor.getAid(),1);
        }else{
            anchor.setType(1);
            anchorDao.save(anchor);
        }

    }

    @Override
    public List<OutFansInfo> getFansByAid(String aid) {

        List<FocusAnchor> ids= anchorDao.getAllByAid(aid);

        return getInfo(ids);
    }

    @Override
    public List<UserInfo> getAnchorsByUid(String uid) {

        List<FocusAnchor> ids= anchorDao.getAllByUid(uid);

        return getAidInfo(ids);
    }

    @Override
    public void UnLikeByUidAndAid(String uid, String Aid) {
        if(isExit(uid,Aid)){
            anchorDao.updateByUidandAid(uid,Aid,0);
        }else{
            FocusAnchor anchor=new FocusAnchor();
            anchor.setDate(new Date());
            anchor.setUid(uid);
            anchor.setAid(Aid);
            anchor.setType(0);
            anchorDao.save(anchor);
        }


    }

    @Override
    public boolean isExit(String uid, String aid) {
        if(anchorDao.getByUidAndAid(uid,aid)!=null){
            return  true;
        }
        return false;
    }

    @Override
    public List<OutFansInfo> getMoneyFans(String aid) {

        List<FocusAnchor> ids= anchorDao.getGiveMoneyByAid(aid);
        return getInfo(ids);
    }

    @Override
    public boolean isFoucus(String aid, String uid) {
        if(anchorDao.isFoucus(uid,aid)!=null){
            return true;
        }
        return false;
    }

    @Override
    public long GiveAcount(String aid, String uid, long acount) {

         Acounnt money= acountDao.getByUid(uid);
            if(anchorDao.getByUidAndAid(uid,aid)==null){
                FocusAnchor anchor=new FocusAnchor();
                anchor.setAid(aid);
                anchor.setUid(uid);
                anchor.setDate(new Date());
                anchor.setAcoount(0);
                anchor.setType(0);
                anchorDao.saveAndFlush(anchor);
            }

         if(money.getAcount()>acount){


             anchorDao.updateAcountByUidAndAid(uid,aid,acount);

             acountDao.updateAcountByUid(uid,-acount);

             return  (money.getAcount()-acount);
         }

        return -1;
    }


    private List<OutFansInfo> getInfo( List<FocusAnchor> ids){

        List<OutFansInfo> list=new ArrayList<>();
        for(FocusAnchor anchor:ids){
            User u= userDao.getUserByUserName(anchor.getUid());
            OutFansInfo fans=new OutFansInfo();
            fans.setUid(u.getUserName());
            fans.setAcount(anchor.getAcoount());
            fans.setHeadImg(u.getHeadImg());
            fans.setNickName(u.getNickName());
            list.add(fans);
        }

        return list;
    }



    private List<UserInfo> getAidInfo( List<FocusAnchor> ids){

        List<UserInfo> list=new ArrayList<>();
        for(FocusAnchor anchor:ids){
            User u= userDao.getUserByUserName(anchor.getUid());
            UserInfo fans=new UserInfo();
            fans.setHeadImg(u.getHeadImg());
            fans.setNickName(u.getNickName());
            fans.setIntroduction(u.getIntroduction());

            list.add(fans);
        }
        return list;
    }

}
