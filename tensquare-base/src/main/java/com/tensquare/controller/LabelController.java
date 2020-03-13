package com.tensquare.controller;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import com.tensquare.pojo.Label;
import com.tensquare.service.LabelServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/label")
@RefreshScope
public class LabelController {

    @Autowired
    private LabelServie labelServie;

//    //自定义配置需要加上@RefreshScope注解然后这样就可以更新配置后将自定义的配置也更新
//    @Value("${sms.ip}")
//    private String ip;

    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelServie.findAll());
    }

    @GetMapping("/{labelId}")
    public Result findById(@PathVariable("labelId")String labelId){
        return new Result(true, StatusCode.OK,"查询成功",labelServie.findById(labelId));
    }

    @PostMapping
    public Result save(@RequestBody Label label){
        labelServie.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{labelId}")
    public Result update(@PathVariable("labelId")String labelId,@RequestBody Label label){
        label.setId(labelId);
        labelServie.update(label);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{labelId}")
    public Result delete(@PathVariable("labelId")String labelId){
        labelServie.deleteById(labelId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label){
        List<Label> labels = this.labelServie.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",labels);
    }

    @PostMapping("/search/{page}/{size}")
    public Result pageQuery(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label> pageData = this.labelServie.pageQuery(label,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }
}
