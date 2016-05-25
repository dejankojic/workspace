package com.zoom.multichannelsurvey.dataload.rest.model;

import java.util.ArrayList;
import java.util.List;

public class MsisdnListRequest {

	private List<String> msisdns = new ArrayList<String>();

	public MsisdnListRequest() {

	}

	public MsisdnListRequest(List<String> msisdns) {
		this.msisdns = msisdns;
	}

	public List<String> getMsisdns() {
		return msisdns;
	}

	public void setMsisdns(List<String> msisdns) {
		this.msisdns = msisdns;
	}

}
