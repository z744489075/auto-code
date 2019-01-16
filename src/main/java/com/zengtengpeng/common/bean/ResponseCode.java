package com.zengtengpeng.common.bean;

public enum ResponseCode {
	SUCCESS("0","成功"),
	FAIL("1","失败"),
	;

    private String code;
	private String desc;
	private ResponseCode(String code, String desc){
		this.code=code;
		this.desc=desc;
	}

	public String code(){
		return this.code;
	}

	public String desc(){
		return this.desc;
	}

}
