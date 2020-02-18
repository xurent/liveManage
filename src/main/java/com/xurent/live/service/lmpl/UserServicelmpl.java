package com.xurent.live.service.lmpl;

import com.xurent.live.dao.UserDao;
import com.xurent.live.model.User;
import com.xurent.live.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServicelmpl implements UserService {


    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer id) {

        return userDao.getOne(id);
    }

    @Override
    public User getUserByUserName(String username) {
        return userDao.getUserByUserName(username);
    }

    @Override
    public boolean checkPassword(String phone, String password) {

        User u=userDao.getByPhoneAndPassword(phone,password);
        if(u!=null){
            return  true;
        }
        return false;
    }

    @Override
    public User getByPhone(String phone) {
        return userDao.getByPhone(phone);
    }

    @Override
    public void updateUserInfoByUsername(User user) {

        userDao.save(user);

    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void Add(User user) {
        userDao.save(user);
    }
}
