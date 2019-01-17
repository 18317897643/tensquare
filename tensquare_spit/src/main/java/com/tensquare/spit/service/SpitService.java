package com.tensquare.spit.service;

import com.mongodb.BasicDBObject;
import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {
    @Autowired
    private SpitDao spitDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     *
     * @return
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }

    /**
     * 按条件查询
     *
     * @return
     */
    public List<Spit> findSearch(Spit spit) {
        Query query = createQuery(spit);
        List<Spit> spits = mongoTemplate.find(query, Spit.class);
        return spits;
    }

    /**
     * 按条件查询 返回分页数据
     *
     * @param spit
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findSearch(Spit spit, int page, int size) {
        Query query = createQuery(spit);
        Pageable pageable = PageRequest.of(page - 1, size);
        //查询结果集
        List<Spit> list = mongoTemplate.find(query.with(pageable), Spit.class);
        //查询总数
        long count = mongoTemplate.count(query, Spit.class);
        //封装分页对象
        Page<Spit> spitPage = new PageImpl<Spit>(list, pageable, count);
        return spitPage;
    }

    /**
     * 保存数据
     *
     * @param spit
     */
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId() + "");
        spit.setComment(0);
        spit.setPublishtime(new Date());
        spit.setShare(0);
        spit.setState("0");
        spit.setThumbup(0);
        spit.setVisits(0);
        spitDao.save(spit);
    }

    /**
     * 修改
     */
    public void update(Spit spit) {
        boolean b = spitDao.existsById(spit.get_id());
        if(b){
//            spitDao.save(spit);  这样操作会导致未被修改字段丢失
            BasicDBObject dbObject = createUpdate(spit);
            Update update = new BasicUpdate(dbObject.toJson());
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.get_id()));
            mongoTemplate.updateFirst(query,update,"spit");
        }else{
            throw new RuntimeException("这条信息找不到了");
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id) {
        boolean b = spitDao.existsById(id);
        if(b){
            spitDao.deleteById(id);
        }else{
            throw new RuntimeException("这条信息找不到了");
        }

    }

    /**
     * 点赞
     */
    public void thumbup(String id) {
        String userid = "111";
        if (null != redisTemplate.opsForValue().get("thumbup_" + userid + id)) {
            throw new RuntimeException("不能重复点赞！");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, Spit.class);
        redisTemplate.opsForValue().set("thumbup_" + userid + id, 1);
    }

    /**
     * 动态构建条件对象
     */
    public Query createQuery(Spit spit) {
        Query query = new Query();
        if (StringUtils.isNotBlank(spit.get_id())) {
            query.addCriteria(Criteria.where("_id").is(spit.get_id()));
        }
        if (StringUtils.isNotBlank(spit.getContent())) {
            query.addCriteria(Criteria.where("content").is(spit.getContent()));
        }
        if (StringUtils.isNotBlank(spit.getNickname())) {
            query.addCriteria(Criteria.where("nickname").is(spit.getNickname()));
        }
        if (StringUtils.isNotBlank(spit.getParentid())) {
            query.addCriteria(Criteria.where("parentid").is(spit.getParentid()));
        }
        if (StringUtils.isNotBlank(spit.getState())) {
            query.addCriteria(Criteria.where("state").is(spit.getState()));
        }
        if (StringUtils.isNotBlank(spit.getUserid())) {
            query.addCriteria(Criteria.where("userid").is(spit.getUserid()));
        }
        if (null != spit.getComment()) {
            query.addCriteria(Criteria.where("comment").is(spit.getComment()));
        }
        if (null != spit.getShare()) {
            query.addCriteria(Criteria.where("share").is(spit.getShare()));
        }
        if (null != spit.getThumbup()) {
            query.addCriteria(Criteria.where("thumbup").is(spit.getThumbup()));
        }
        if (null != spit.getPublishtime()) {
            query.addCriteria(Criteria.where("publishtime").is(spit.getPublishtime()));
        }
        if (null != spit.getVisits()) {
            query.addCriteria(Criteria.where("visits").is(spit.getVisits()));
        }
        return query;
    }

    /**
     * 构造修改对象
     * @param spit
     * @return
     */
    public BasicDBObject createUpdate(Spit spit){
        BasicDBObject basicDBObject = new BasicDBObject();
        if (StringUtils.isNotBlank(spit.get_id())) {
            basicDBObject.put("$set",new BasicDBObject("_id",spit.get_id()));
        }
        if (StringUtils.isNotBlank(spit.getContent())) {
            basicDBObject.put("$set",new BasicDBObject("contenet",spit.getContent()));
        }
        if (StringUtils.isNotBlank(spit.getNickname())) {
            basicDBObject.put("$set",new BasicDBObject("nickname",spit.getNickname()));
        }
        if (StringUtils.isNotBlank(spit.getParentid())) {
            basicDBObject.put("$set",new BasicDBObject("parentid",spit.getParentid()));
        }
        if (StringUtils.isNotBlank(spit.getState())) {
            basicDBObject.put("$set",new BasicDBObject("state",spit.getState()));
        }
        if (StringUtils.isNotBlank(spit.getUserid())) {
            basicDBObject.put("$set",new BasicDBObject("userid",spit.getUserid()));
        }
        if (null != spit.getComment()) {
            basicDBObject.put("$set",new BasicDBObject("comment",spit.getComment()));
        }
        if (null != spit.getShare()) {
            basicDBObject.put("$set",new BasicDBObject("share",spit.getShare()));
        }
        if (null != spit.getThumbup()) {
            basicDBObject.put("$set",new BasicDBObject("thumbup",spit.getThumbup()));
        }
        if (null != spit.getPublishtime()) {
            basicDBObject.put("$set",new BasicDBObject("publishtime",spit.getPublishtime()));
        }
        if (null != spit.getVisits()) {
            basicDBObject.put("$set",new BasicDBObject("visits",spit.getVisits()));
        }
        return basicDBObject;
    }
}
