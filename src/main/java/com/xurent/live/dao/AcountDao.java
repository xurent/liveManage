package com.xurent.live.dao;

import com.xurent.live.model.Acounnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AcountDao extends JpaRepository<Acounnt,Integer> {


    public Acounnt getByUid(String uid);


    @Modifying
    @Query("update Acounnt  a set a.acount=a.acount+(?2) where a.uid=?1")
    public void updateAcountByUid(String uid,long money);



}
