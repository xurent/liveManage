package com.xurent.live.dao;

import com.xurent.live.model.FileBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileDao extends JpaRepository<FileBean,Integer> {



    @Query("select  f from  FileBean f where f.userId =?1  order by f.likeNumber desc ,f.createTime desc ")
    public List<FileBean> getByUserId(String uid);



    @Query("select  f from  FileBean f where f.type =?1  order by f.likeNumber desc ,f.createTime desc ")
    public List<FileBean> getByType(String type);


    @Query("select  f from  FileBean f   order by f.likeNumber desc ,f.createTime desc ")
    public List<FileBean> getFileAll();


}
