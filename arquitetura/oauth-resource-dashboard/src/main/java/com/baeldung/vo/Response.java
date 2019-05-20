package com.baeldung.vo;


import com.baeldung.dao.entity.*;

import java.io.Serializable;
import java.util.List;


/**
 * Response object containing traffic details that will be sent to dashboard.
 *
 * @author jfonseca
 */
public class Response<TotalTrafficData> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<TotalTrafficData> totalTraffic;

    private List<TotalTweetsFollwersTrafficData> totalTweetsFollwersTraffic;

    private List<TotalTweetsUsuarioTrafficData> usuarioTrafficList;

    private List<WindowTrafficData> windowTraffic;

    private List<WindowTweetTrafficData> windowTweetTraffic;

    private List<POITrafficData> poiTraffic;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<TotalTrafficData> getTotalTraffic() {
        return totalTraffic;
    }

    public void setTotalTraffic(List<TotalTrafficData> totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public List<WindowTrafficData> getWindowTraffic() {
        return windowTraffic;
    }

    public void setWindowTraffic(List<WindowTrafficData> windowTraffic) {
        this.windowTraffic = windowTraffic;
    }

    public List<POITrafficData> getPoiTraffic() {
        return poiTraffic;
    }

    public void setPoiTraffic(List<POITrafficData> poiTraffic) {
        this.poiTraffic = poiTraffic;
    }

    public List<WindowTweetTrafficData> getWindowTweetTraffic() {
        return windowTweetTraffic;
    }

    public void setWindowTweetTraffic(List<WindowTweetTrafficData> windowTweetTraffic) {
        this.windowTweetTraffic = windowTweetTraffic;
    }

    public List<TotalTweetsFollwersTrafficData> getTotalTweetsFollwersTraffic() {
        return totalTweetsFollwersTraffic;
    }

    public void setTotalTweetsFollwersTraffic(List<TotalTweetsFollwersTrafficData> totalTweetsFollwersTraffic) {
        this.totalTweetsFollwersTraffic = totalTweetsFollwersTraffic;
    }

    public List<TotalTweetsUsuarioTrafficData> getUsuarioTrafficList() {
        return usuarioTrafficList;
    }

    public void setUsuarioTrafficList(List<TotalTweetsUsuarioTrafficData> usuarioTrafficList) {
        this.usuarioTrafficList = usuarioTrafficList;
    }

}
