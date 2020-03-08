package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.AtricleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private AtricleService atricleService;

    @PostMapping
    public Result save(@RequestBody Article article){
        this.atricleService.save(article);
        return new Result(true, StatusCode.OK, "保存成功");
    }


    @GetMapping("/{key}/{page}/{size}")
    public Result findBykey(@PathVariable String key,@PathVariable int page,@PathVariable int size){
        Page<Article> page1 = this.atricleService.findBykey(key,page,size);
        return new Result(true, StatusCode.OK, "查询成功",new PageResult<Article>(page1.getTotalElements(),page1.getContent()));
    }
}
