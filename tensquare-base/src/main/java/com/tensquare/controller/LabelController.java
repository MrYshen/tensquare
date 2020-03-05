package com.tensquare.controller;

import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import com.tensquare.pojo.Label;
import com.tensquare.service.LabelServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelServie labelServie;

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
}
