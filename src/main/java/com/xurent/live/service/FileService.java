package com.xurent.live.service;

import com.xurent.live.model.FileBean;

import java.util.List;

public interface FileService {

    public void save(FileBean fileBean);

    public void delete(Integer fid);

    public List<FileBean> getFilesByUid(String uid);

    public List<FileBean> getFilesByType(String type);


    public List<FileBean> getAllFiles();


}
