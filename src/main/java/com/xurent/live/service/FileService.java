package com.xurent.live.service;

import com.xurent.live.model.CollectionWorks;
import com.xurent.live.model.FileBean;
import com.xurent.live.model.out.UserInfo;

import java.util.List;

public interface FileService {

    public void save(FileBean fileBean);

    public void delete(Integer fid);

    public List<FileBean> getFilesByUid(String uid);

    public List<FileBean> getFilesByType(String type);


    public List<FileBean> getAllFiles();

    public FileBean getById(Integer id);
    /**
     * 收藏视频
     * @param works
     */
    public void Collection(CollectionWorks works);


    public void UnCollection(String uid,Integer wid);


    public List<FileBean> getCollectionsByUid(String uid);


    /**
     * 获取视频关注者Id
     * @param wid
     * @return
     */
    public List<UserInfo> getPeopleByWorkId(Integer wid);

}
