package com.iot.app.springboot.web.controller;

import com.iot.app.springboot.dashboard.TrafficDataService;
import com.iot.app.springboot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashBoardResource {

    @Autowired
    private TrafficDataService service;

    public DashBoardResource() {
        super();
    }

    // API - read
    @PreAuthorize("#oauth2.hasScope('foo') and #oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/dashboards")
    @ResponseBody
    public Response listarDashboard() {
        return service.trigger();
    }

}
