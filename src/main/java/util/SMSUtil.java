package util;

import java.io.IOException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;


public class SMSUtil {
	private String id;
	private String key;
	
	public SMSUtil(String id, String key) {
		this.id = id;
		this.key = key;
	}
	
	public void sendSMS(String targetPhoneNumber, String receiverName, String ordersName, String address) {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
		NameValuePair[] data ={ 
				new NameValuePair("Uid", id),
				new NameValuePair("Key", key),
				new NameValuePair("smsMob",  targetPhoneNumber),
				new NameValuePair("smsText", "尊敬的" + receiverName + "，您有新的快件" + ordersName + "到达" + address + "，请及时取件！")
		};
		post.setRequestBody(data);

		try {
			client.executeMethod(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:"+statusCode);
		for(Header h : headers)
		{
		System.out.println(h.toString());
		}
		String result = "";
		try {
			result = new String(post.getResponseBodyAsString().getBytes("gbk"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result); //打印返回消息状态
		post.releaseConnection();
	}
	
}
