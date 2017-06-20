package com.water.roll.request;

import com.alibaba.fastjson.JSON;

import in.srain.cube.request.JsonAble;

public class RequestBean<T> implements JsonAble {
	
	private int mid;
	private String method;
	private T param;
	
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public T getParam() {
		return param;
	}
	public void setParam(T param) {
		this.param = param;
	}
	
	@Override
	public String toJsonString(){
		return JSON.toJSONString(this);
	}
	
}
