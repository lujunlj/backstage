package com.tencent.backstage.modules.monitor.rest;


import com.tencent.backstage.common.utils.RequestHolder;
import com.tencent.backstage.modules.monitor.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:15:08
 */
@RestController
@RequestMapping("api")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @PostMapping(value = "/visits")
    public ResponseEntity create(){
        visitService.count(RequestHolder.getHttpServletRequest());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/visits")
    public ResponseEntity get(){
        return new ResponseEntity(visitService.get(),HttpStatus.OK);
    }

    @GetMapping(value = "/visits/chartData")
    public ResponseEntity getChartData(){
        return new ResponseEntity(visitService.getChartData(),HttpStatus.OK);
    }
}
