
package com.zengtengpeng.common.bean;

/**
 * 返回数据包装类
 * @param <T>
 */
public class DataRes<T> {
	/**
	 * 返回码 详见 ResponseCode
	 */
	private String code;
	/**
	 * 提示消息
	 */
	private String message;
	/**
	 * 返回时间
	 */
	private String time;
	/**
	 * 具体的数据
	 */
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

	public static  DataRes error() {
		DataRes platformRes=new DataRes();
		platformRes.setCode(ResponseCode.FAIL.code());
		platformRes.setMessage(ResponseCode.FAIL.desc());
		return platformRes;
	}
	public static  DataRes error(ResponseCode codeMsgType) {
		DataRes platformRes=new DataRes();
		platformRes.setCode(codeMsgType.code());
		platformRes.setMessage(codeMsgType.desc());
		return platformRes;
	}
	public static DataRes error(String code,String desc) {
		DataRes platformRes=new DataRes();
		platformRes.setCode(code);
		platformRes.setMessage(desc);
		return platformRes;
	}
	public static <T>  DataRes error(String code,String desc,T t) {
		DataRes platformRes=new DataRes();
		platformRes.setCode(code);
		platformRes.setMessage(desc);
		platformRes.setData(t);
		return platformRes;
	}
	public static <T>  DataRes error(ResponseCode codeMsgType,T t) {
		DataRes platformRes=new DataRes();
		platformRes.setCode(codeMsgType.code());
		platformRes.setMessage(codeMsgType.desc());
		platformRes.setData(t);
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
