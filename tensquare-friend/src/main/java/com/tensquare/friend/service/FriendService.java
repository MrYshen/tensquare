package com.tensquare.friend.service;

import com.tensquare.friend.dao.FreindDao;
import com.tensquare.friend.dao.NoFreindDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FreindDao freindDao;

    @Autowired
    private NoFreindDao noFreindDao;

    public int addFriend(String userId, String friendid) {

        //先判断userid到friendid中是否有数据，有就重复添加好友，返回0
        Friend friend = this.freindDao.findByUseridAndFriendid(userId, friendid);
        if(friend!=null){
            return 0;
        }

        //直接添加好友，让好友表中userid到friendid方向的type为0
        friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        this.freindDao.save(friend);

        //判断friendid到userid是否有数据,有就把双方的状态都改成1
        if (this.freindDao.findByUseridAndFriendid(friendid, userId) != null){
            //把双方的islike都改成1
            this.freindDao.updateIsLike("1", userId, friendid);
            this.freindDao.updateIsLike("1", friendid, userId);
        }
        return 1;
    }

    public int addNoFriend(String userId, String friendid) {

        //先判断是否已经是不喜欢的人
        NoFriend noFriend = noFreindDao.findByUseridAndFriendid(userId, friendid);
        if(noFriend != null){
            return 0;
        }

        noFriend = new NoFriend();
        noFriend.setFriendid(friendid);
        noFriend.setUserid(userId);
        this.noFreindDao.save(noFriend);
        return 1;
    }

    public void deleteFriend(String userId, String friendid) {

        //删除好友表中userid到friendid这条数据
        this.freindDao.deleteFriend(userId,friendid);

        //更新frieid到userid的islike=0
        this.freindDao.updateIsLike("0", friendid, userId);

        //非好友表中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userId);
        noFriend.setFriendid(friendid);
        this.noFreindDao.save(noFriend);
    }
}
