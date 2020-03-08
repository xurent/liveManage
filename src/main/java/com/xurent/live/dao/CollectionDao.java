package com.xurent.live.dao;

import com.xurent.live.model.CollectionWorks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionDao extends JpaRepository<CollectionWorks,Integer> {


    @Query(value = "select * from tb_record_works where uid=?1 order by create_time desc ",nativeQuery = true)
    public List<CollectionWorks> getAllByUid(String uid);

    @Query(value = "select * from tb_record_works where work_id in (?1)",nativeQuery = true)
    public List<CollectionWorks> getAllByWorkId(List<Integer> ids);


    public void deleteByWorkIdAndUid(Integer wid,String uid);

}
