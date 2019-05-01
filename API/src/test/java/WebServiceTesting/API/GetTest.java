package WebServiceTesting.API;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetTest
{
	String uri;
	String url = "https://maps.googleapis.com";
	String path = "/maps/api/geocode/json";
	CloseableHttpResponse closeableHttpResponse;
	
	
	protected String addLocationToUrl(String path){
	    if(!path.endsWith("?"))
	        path += "?";

	    List<NameValuePair> params = new LinkedList<NameValuePair>();

	    params.add(new BasicNameValuePair("address", "Mumbai"));
	    params.add(new BasicNameValuePair("key", "AIzaSyDcFk2h1S77eZhtkzM77L5NMZeplhEsBd4"));

	    String paramString = URLEncodedUtils.format(params, "utf-8");

	    path += paramString;
	    return path;
	}
	
	@BeforeMethod
	public void setUp() throws ClientProtocolException, IOException{
		
		GetTest gettest = new GetTest();
		String query = gettest.addLocationToUrl(path);
		
		uri = url + query;
		
	}
	
	@Test
	public void getGeocode() throws ClientProtocolException, IOException
	{
		RestClient restClient = new RestClient();
		closeableHttpResponse = restClient.get(uri);
		
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code:"+ statusCode);
		
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response:"+ responseJson);
	}
	
}
