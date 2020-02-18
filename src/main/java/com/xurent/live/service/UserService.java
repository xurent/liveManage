package com.xurent.live.service;

import com.xurent.live.model.User;

import java.util.List;

public interface UserService {

    public User getUserById(Integer id);

    public User getUserByUserName(String username);

    public boolean checkPassword(String phone,String password);

    public User getByPhone(String phone);

    public  void updateUserInfoByUsername(User user);

    public List<User> getAllUsers();

    public void Add(User user);



}
