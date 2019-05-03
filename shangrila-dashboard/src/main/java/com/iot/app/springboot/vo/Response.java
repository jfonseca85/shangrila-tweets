package com.iot.app.springboot.vo;

import java.io.Serializable;
import java.util.List;

import com.iot.app.springboot.dao.entity.POITrafficData;
import com.iot.app.springboot.dao.entity.TotalTrafficData;
import com.iot.app.springboot.dao.entity.TotalTweetsFollwersTrafficData;
import com.iot.app.springboot.dao.entity.TotalTweetsUsuarioTrafficData;
import com.iot.app.springboot.dao.entity.WindowTrafficData;
import com.iot.app.springboot.dao.entity.WindowTweetTrafficData;

/**
 * Response object containing traffic details that will be sent to dashboard.
 * 
 * @author jfonseca
 *
 */
public class Response implements Serializable {
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
