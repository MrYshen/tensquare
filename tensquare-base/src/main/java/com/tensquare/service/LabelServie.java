package com.tensquare.service;

import com.tensquare.dao.LabelDao;
import com.tensquare.pojo.Label;
import util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelServie {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }

    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    public void save(Label label){

        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    public void update(Label label){

        labelDao.save(label);
    }

    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(new Specification<Label>() {
            /**
             * @param root  跟对象，也就是要把条件封装到那个对象汇总。where类名=label.getid
             * @param query  封装的都是查询关键字，比如group by order by
             * @param cb   用来封装条件对象的
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                //new一个list用来存放所有的条件
                List<Predicate> list = new ArrayList<>();

                if(label.getLabelname()!=null &&!"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");//labelname like "%小明%"
                    list.add(predicate);
                }


                if(label.getState()!=null &&!"".equals(label.getState())){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());// state = "1"
                    list.add(predicate);
                }

                //new一个数字作为最终返回值的条件
                Predicate[] predicates = new Predicate[list.size()];
                //把list直接转化成数组
                predicates = list.toArray(predicates);
                return cb.and(predicates); //where labelname like "%小明%" and state = "1"
            }
        });
    }

    public Page<Label> pageQuery(Label label, int page, int size) {

        //封装一个分页对象
        Pageable pageable = PageRequest.of(page-1, size);

        return labelDao.findAll(new Specification<Label>() {
            /**
             * @param root  跟对象，也就是要把条件封装到那个对象汇总。where类名=label.getid
             * @param query  封装的都是查询关键字，比如group by order by
             * @param cb   用来封装条件对象的
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                //new一个list用来存放所有的条件
                List<Predicate> list = new ArrayList<>();

                if(label.getLabelname()!=null &&!"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");//labelname like "%小明%"
                    list.add(predicate);
                }


                if(label.getState()!=null &&!"".equals(label.getState())){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());// state = "1"
                    list.add(predicate);
                }

                //new一个数字作为最终返回值的条件
                Predicate[] predicates = new Predicate[list.size()];
                //把list直接转化成数组
                predicates = list.toArray(predicates);
                return cb.and(predicates); //where labelname like "%小明%" and state = "1"
            }
        },pageable);
    }
}
