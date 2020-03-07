package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",this.spitService.findAll());
    }

    @GetMapping("/{spitId}")
    public Result findById(@PathVariable String spitId){
        return new Result(true,StatusCode.OK,"查询成功",this.spitService.findById(spitId));
    }

    @PostMapping
    public Result save(@RequestBody Spit spit){
        this.spitService.save(spit);
        return new Result(true,StatusCode.OK,"保存成功");
    }

    @PutMapping("/{spitId}")
    public Result update(@PathVariable String spitId,@RequestBody Spit spit){
        spit.set_id(spitId);
        this.spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    @DeleteMapping("/{spitId}")
    public Result deleteById(@PathVariable String spitId){
        this.spitService.deleteById(spitId);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @GetMapping("/comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size){
        Page<Spit> page1 = this.spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK, "查询成功",new PageResult<Spit>(page1.getTotalElements(),page1.getContent()));
    }

    @PutMapping("/thumbup/{spitId}")
    public Result thumbup(@PathVariable String spitId){

        String userId = "123";
        //判断当前用户是否已经点赞
        if(redisTemplate.opsForValue().get("thumbup"+userId)!= null){
            return new Result(false, StatusCode.REPERROR, "不能重复点赞");
        }
        this.spitService.thumbup(spitId);
        redisTemplate.opsForValue().set("thumbup"+userId, "1");
        return new Result(true, StatusCode.OK, "点赞成功");
    }

}
