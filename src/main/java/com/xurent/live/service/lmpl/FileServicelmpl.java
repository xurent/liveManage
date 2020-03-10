package com.xurent.live.service.lmpl;

import com.xurent.live.dao.CollectionDao;
import com.xurent.live.dao.FileDao;
import com.xurent.live.model.CollectionWorks;
import com.xurent.live.model.FileBean;
import com.xurent.live.model.out.UserInfo;
import com.xurent.live.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("fileService")
public class FileServicelmpl implements FileService {


    @Autowired
    private FileDao fileDao;

    @Autowired
    private CollectionDao collectionDao;

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
    public List<FileBean> getFilesByType(Integer type) {

        return fileDao.getByType(type);
    }

    @Override
    public List<FileBean> getAllFiles() {

        return fileDao.getFileAll();
    }

    @Override
    public FileBean getById(Integer id) {
        return fileDao.getOne(id);
    }

    @Override
    public void Collection(CollectionWorks works) {

        if(!isCollect(works.getUid(),works.getWorkId())){
            collectionDao.save(works);
            fileDao.updateLikeNumberByFId(works.getWorkId(),1);
        }

    }

    @Override
    public void UnCollection(String uid, Integer wid) {
        if(isCollect(uid,wid)){
            collectionDao.deleteByWorkIdAndUid(wid,uid);
            fileDao.updateLikeNumberByFId(wid,-1);
        }

    }

    @Override
    public List<FileBean> getCollectionsByUid(String uid) {

      List<CollectionWorks> collectionWorks= collectionDao.getAllByUid(uid);

      List<Integer> ids=new ArrayList<>();
      for(CollectionWorks work:collectionWorks){
          ids.add(work.getWorkId());
      }

        return fileDao.findAllById(ids);
    }



    @Override
    public List<UserInfo> getPeopleByWorkId(Integer wid) {
        return null;
    }

    @Override
    public boolean isCollect(String uid, Integer wid) {

        if(collectionDao.getByUidAndWorkId(uid,wid)!=null){
            return true;
        }
        return false;
    }
}
