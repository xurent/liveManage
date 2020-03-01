package com.xurent.live.service.lmpl;

import com.xurent.live.dao.FileDao;
import com.xurent.live.model.FileBean;
import com.xurent.live.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fileService")
public class FileServicelmpl implements FileService {


    @Autowired
    private FileDao fileDao;

    @Override
    public void save(FileBean fileBean) {

        if(fileBean!=null){
            fileDao.save(fileBean);
        }
    }

    @Override
    public void delete(Integer fid) {

        fileDao.deleteById(fid);
    }

    @Override
    public List<FileBean> getFilesByUid(String uid) {


        return fileDao.getByUserId(uid);
    }

    @Override
    public List<FileBean> getFilesByType(String type) {

        return fileDao.getByType(type);
    }

    @Override
    public List<FileBean> getAllFiles() {

        return fileDao.getFileAll();
    }
}
