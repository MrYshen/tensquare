package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query(value = "SELECT * FROM tb_problem a,tb_pl b WHERE a.id = b.problemid AND b.labelid=? ORDER BY a.replytime",nativeQuery = true)
    public Page<Problem> newlist(String labelId, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem a,tb_pl b WHERE a.id = b.problemid AND b.labelid=? ORDER BY a.reply DESC",nativeQuery = true)
    public Page<Problem> hotlist(String labelId, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem a,tb_pl b WHERE a.id = b.problemid AND b.labelid=? AND reply=0 ORDER BY a.createtime",nativeQuery = true)
    public Page<Problem> waitlist(String labelId, Pageable pageable);
}
