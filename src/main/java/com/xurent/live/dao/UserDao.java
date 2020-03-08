package com.xurent.live.dao;

import com.xurent.live.model.FocusAnchor;
import com.xurent.live.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {


    public User getUserByUserName(String username);


    public User getByPhoneAndPassword(String phone,String password);



    @Modifying
    @Query("update User  u set  u.password=?1 where u.userName=?2")
    public void updatePasswordByUserName(String password,String username);

    public User getByPhone(String phone);


    @Query(value = "select * from tb_user where user_name in (?1)",nativeQuery = true)
    List<User> findAllByUserName(List<FocusAnchor> ids);
}
