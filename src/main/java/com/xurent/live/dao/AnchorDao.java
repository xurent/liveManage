package com.xurent.live.dao;

import com.xurent.live.model.FocusAnchor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnchorDao extends JpaRepository<FocusAnchor,Integer> {



    @Query("select  f from  FocusAnchor f where f.uid=?1 and f.type=1")
    public List<FocusAnchor> getAllByUid(String uid);

    @Query("select  f from  FocusAnchor f where f.aid=?1 and f.type=1  order by f.acoount desc ")
    public List<FocusAnchor> getAllByAid(String aid);

    /**
     * 获取打赏的用户
     * @param aid
     * @return
     */
    @Query("select f from FocusAnchor f where  f.aid=?1 and f.acoount>0  order by f.acoount desc")
    public List<FocusAnchor> getGiveMoneyByAid(String aid);

    public FocusAnchor getByUidAndAid(String uid, String aid);


    @Query("update  FocusAnchor an set an.type=?3 where an.uid=?1 and an.aid=?2")
    @Modifying
    public void updateByUidandAid(String uid,String aid,Integer type);


    @Query("select f from  FocusAnchor  f where f.uid=?1 and f.aid=?2 and f.type=1")
    public FocusAnchor isFoucus(String uid, String aid);

}
