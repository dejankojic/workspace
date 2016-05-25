package com.zoom.multichannelsurvey.dataload.rest.model;

public class RangeRequest {

	private Long msisdnFrom;

	private Long msisdnTo;

	public RangeRequest() {

	}

	public RangeRequest(Long msisdnFrom, Long msisdnTo) {
		this.msisdnFrom = msisdnFrom;
		this.msisdnTo = msisdnTo;
	}

	public Long getMsisdnFrom() {
		return msisdnFrom;
	}

	public void setMsisdnFrom(Long msisdnFrom) {
		this.msisdnFrom = msisdnFrom;
	}

	public Long getMsisdnTo() {
		return msisdnTo;
	}

	public void setMsisdnTo(Long msisdnTo) {
		this.msisdnTo = msisdnTo;
	}

}
