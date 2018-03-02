package com.ezone.test;

import java.util.HashMap;
import java.util.Map;

import org.coody.framework.context.entity.HttpEntity;
import org.coody.framework.util.FileUtils;
import org.coody.framework.util.HttpHandle;
import org.coody.framework.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class LaiTeJiTest {

	private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153"
			.split(",");

	public static String createMobile() {
		int index = StringUtil.getRanDom(0, telFirst.length - 1);
		String first = telFirst[index];
		String second = String.valueOf(StringUtil.getRanDom(1, 888) + 10000).substring(1);
		String third = String.valueOf(StringUtil.getRanDom(1, 9100) + 10000).substring(1);
		return first + second + third;
	}
	
	
	public static void main(String[] args) {
		for(int i=0;i<999999;i++){
			FileUtils.writeAppendLine("f://mobile.txt", createMobile());
		}
	}
}
