package com.baeldung.web.controller;

import com.baeldung.dashboard.TrafficDataService;
import com.baeldung.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashBoardController {

    @Autowired
    private TrafficDataService service;

    public DashBoardController() {
        super();
    }

    // API - read
    @PreAuthorize("#oauth2.hasScope('foo') and #oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "api/v1/dashboards")
    @ResponseBody
    public Response listarDashBoard() {
        Response trigger = service.trigger();
        return trigger;
    }
}
