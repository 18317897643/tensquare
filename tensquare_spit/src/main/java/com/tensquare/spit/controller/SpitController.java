package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit) {
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findAll());
    }

    @RequestMapping(value = "/{spitId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findById(spitId));
    }

    @RequestMapping(value = "/{spitId}", method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId, @RequestBody Spit spit) {
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @RequestMapping(value = "/{spitId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId) {
        spitService.delete(spitId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/thumbup/{spitId}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId) {
        spitService.thumbup(spitId);
        return new Result(true, StatusCode.OK, "点赞成功");
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Spit spit) {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findSearch(spit));
    }

    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result pageQuery(@RequestBody Spit spit, @PathVariable int page, @PathVariable int size) {
        Page<Spit> pagedata = spitService.findSearch(spit, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Spit>(pagedata.getTotalElements(), pagedata.getContent()));
    }

    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result queryComment(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Spit spit = new Spit();
        spit.setParentid(parentid);
        Page<Spit> spitPage = spitService.findSearch(spit, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Spit>(spitPage.getTotalElements(), spitPage.getContent()));
    }

}
