package com.baeldung.dashboard;

import com.baeldung.dao.*;
import com.baeldung.dao.entity.*;
import com.baeldung.vo.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service class to send traffic data messages to dashboard ui at fixed interval
 * using web-socket.
 *
 * @author jfonseca
 */
@Service
public class TrafficDataService {
    private static final Logger logger = Logger.getLogger(TrafficDataService.class);
    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private TotalTrafficDataRepository totalRepository;
    @Autowired
    private TotalTweetsFollwersTrafficDataRepository follwersTrafficDataRepository;
    @Autowired
    private TotalTweetsUsuarioTrafficDataRepository usuarioTrafficDataRepository;
    @Autowired
    private WindowTrafficDataRepository windowRepository;
    @Autowired
    private WindowTweetTrafficDataRepository windowTweetRepository;
    @Autowired
    private POITrafficDataRepository poiRepository;

    // Method sends traffic data message in every 5 seconds.
   // @Scheduled(fixedRate = 5000)
    public Response trigger() {
        List<TotalTrafficData> totalTrafficList = new ArrayList<TotalTrafficData>();
        List<TotalTweetsFollwersTrafficData> totalTweetsFollwersTrafficList = new ArrayList<TotalTweetsFollwersTrafficData>();
        List<TotalTweetsUsuarioTrafficData> totalTweetsUsuarioTrafficList = new ArrayList<TotalTweetsUsuarioTrafficData>();
        List<WindowTrafficData> windowTrafficList = new ArrayList<WindowTrafficData>();
        List<WindowTweetTrafficData> windowTweetTrafficList = new ArrayList<WindowTweetTrafficData>();

        List<POITrafficData> poiTrafficList = new ArrayList<POITrafficData>();
        // Call dao methods
        totalRepository.findTrafficDataByDate(sdf.format(new Date())).forEach(e -> totalTrafficList.add(e));
        follwersTrafficDataRepository.findTrafficDataByDate().forEach(e -> totalTweetsFollwersTrafficList.add(e));
        usuarioTrafficDataRepository.findTrafficDataByDate().forEach(e -> totalTweetsUsuarioTrafficList.add(e));
        windowRepository.findTrafficDataByDate(sdf.format(new Date())).forEach(e -> windowTrafficList.add(e));
        windowTweetRepository.findTrafficDataByDate().forEach(e -> windowTweetTrafficList.add(e));
        poiRepository.findAll().forEach(e -> poiTrafficList.add(e));
        // prepare response
        Response response = new Response();
        response.setTotalTraffic(totalTrafficList);
        response.setTotalTweetsFollwersTraffic(totalTweetsFollwersTrafficList);
        response.setUsuarioTrafficList(totalTweetsUsuarioTrafficList);
        response.setWindowTraffic(windowTrafficList);
        response.setWindowTweetTraffic(windowTweetTrafficList);
        response.setPoiTraffic(poiTrafficList);
        logger.info("Sending to UI " + response);
        // send to ui
        this.template.convertAndSend("/topic/trafficData", response);

        return response;
    }

}
