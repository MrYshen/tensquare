package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FreindDao extends JpaRepository<Friend,String> {

    public Friend findByUseridAndFriendid(String userid,String friendid);

    @Modifying
    @Query(value = "UPDATE tb_friend SET islike=? WHERE userid=? AND friendid=?",nativeQuery = true)
    public void updateIsLike(String islike,String userid,String friendid);

    @Modifying
    @Query(value = "delete from tb_friend where userid=? and friendid=?",nativeQuery = true)
    public void deleteFriend(String userId, String friendid);
}
