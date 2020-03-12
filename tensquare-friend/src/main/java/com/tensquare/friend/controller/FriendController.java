package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserClient userClient;

    @DeleteMapping("/{friendid}")
    public Result deleteFriend(@PathVariable("friendid")String friendid){
        //判断时候登陆，拿到当前登陆的用户ID
        Claims claims  = (Claims)request.getAttribute("claims_user");
        if (claims == null){
            return new Result(false, StatusCode.LOGINERROR, "权限不足");
        }
        //判断是添加好友或者添加非好友
        String userId = claims.getId();
        this.friendService.deleteFriend(userId,friendid);
        this.userClient.updateFanscountAndFollowcount(userId, friendid, -1);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PutMapping("/like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){

        //判断时候登陆，拿到当前登陆的用户ID
        Claims claims  = (Claims)request.getAttribute("claims_user");
        if (claims == null){
            return new Result(false, StatusCode.LOGINERROR, "权限不足");
        }
        //判断是添加好友或者添加非好友
        String userId = claims.getId();
        if(type != null){
            if(type.equals("1")){

                //添加好友
                int flag = this.friendService.addFriend(userId,friendid);
                if(flag == 0){
                    return new Result(false, StatusCode.ERROR, "不能重复添加好友");
                }
                if (flag == 1){
                    this.userClient.updateFanscountAndFollowcount(userId, friendid, 1);
                    return new Result(true, StatusCode.OK, "添加成功");
                }
            }else if(type.equals("2")){
                //添加非好友
                int flag = this.friendService.addNoFriend(userId,friendid);
                if(flag == 0){
                    return new Result(false, StatusCode.ERROR, "不能重复添加不喜欢的人");
                }
                if (flag == 1){
                    return new Result(true, StatusCode.OK, "添加成功");
                }
            }
            return new Result(false, StatusCode.ERROR, "参数异常");
        }else {
            return new Result(false, StatusCode.ERROR, "参数异常");
        }

    }
}
