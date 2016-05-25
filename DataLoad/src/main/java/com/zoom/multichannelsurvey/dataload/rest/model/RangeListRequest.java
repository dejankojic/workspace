package com.zoom.multichannelsurvey.dataload.rest.model;

import java.util.ArrayList;
import java.util.List;

public class RangeListRequest {

	private List<RangeRequest> rangeRequests = new ArrayList<RangeRequest>();

	public List<RangeRequest> getRangeRequests() {
		return rangeRequests;
	}

	public void setRangeRequests(List<RangeRequest> rangeRequests) {
		this.rangeRequests = rangeRequests;
	}

}
