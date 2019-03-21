package com.zengtengpeng.common.bean;

public enum ResponseCode {
	SUCCESS("0","成功"),
	FAIL("1","失败"),
	WATCHMOBEL("10001","演示地址只能查看.不能生成."),
	;

    private String code;
	private String desc;
	ResponseCode(String code, String desc){
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
