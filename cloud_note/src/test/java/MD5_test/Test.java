package MD5_test;

import org.apache.commons.codec.digest.DigestUtils;

public class Test {
	
	@org.junit.Test
	public void test() {
		String str = "123456";
		String md5 = DigestUtils.md5Hex(str);
		System.out.println(md5);
		//加盐摘要
		String salt = "吃饭了么";
		md5 = DigestUtils.md5Hex(salt+str);
		System.out.println(md5);
	}
	
}
