package com.xurent.live.service.lmpl;


import com.xurent.live.dao.AcountDao;
import com.xurent.live.model.Acounnt;
import com.xurent.live.service.AcountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("acountService")
public class AcountServicelmpl implements AcountService {

    @Autowired
    private AcountDao acountDao;


    @Override
    public long getAcount(String uid) {

        Acounnt acounnt= acountDao.getByUid(uid);
        if(acounnt==null){
            acounnt=new Acounnt();
            acounnt.setUid(uid);
            acounnt.setAcount((long)50000);
            acountDao.save(acounnt);
            return 0;
        }

        return acounnt.getAcount();
    }
}
