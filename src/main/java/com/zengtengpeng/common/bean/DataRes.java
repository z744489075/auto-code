
package com.zengtengpeng.common.bean;


public class DataRes<T> {

	private String code;
	private String message;
	private String time;
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	

	
	public DataRes(String code, Throwable e) {
		this.code = code;
		this.message = e.getMessage();
	}

	public DataRes() {
	}

	public static <T> DataRes error() {
		DataRes platformRes=new DataRes();
		platformRes.setCode(ResponseCode.FAIL.code());
		platformRes.setMessage(ResponseCode.FAIL.desc());
		return platformRes;
	}
	public static <T> DataRes error(ResponseCode codeMsgType) {
		DataRes platformRes=new DataRes();
		platformRes.setCode(codeMsgType.code());
		platformRes.setMessage(codeMsgType.desc());
		return platformRes;
	}

	public static <T> DataRes success(T t) {
		DataRes platformRes=new DataRes();
		platformRes.setCode(ResponseCode.SUCCESS.code());
		platformRes.setMessage(ResponseCode.SUCCESS.desc());
		platformRes.setData(t);
		return platformRes;
	}


	
}
