package WebServiceTesting.API;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class GetTest
{
	GetTest gettest;
	public static String uri;
	String url = "https://maps.googleapis.com";
	String path = "/maps/api/geocode/json";
	CloseableHttpResponse closeableHttpResponse;
	public static String cityName;
	public static int count=0;
	
	@Test
	public void readExcel() throws EncryptedDocumentException, IOException, InvalidFormatException, InvocationTargetException
	{
		FileInputStream fis = new FileInputStream("C:\\Users\\Lenovo\\git\\API\\API\\testdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheetAt(0);
		
		int rowStart = sh.getFirstRowNum();
		int rowEnd = sh.getLastRowNum();

		for(int i=rowStart+1; i<= rowEnd; i++)
		{
			
				Row row = sh.getRow(i);
				int j = 0;
				
				Cell cell = row.getCell(j);
				
				cityName = cell.getStringCellValue();
				
				gettest = new GetTest();
				gettest.addLocationToUrl(cityName);
									
				gettest.getGeocode(uri);
			
		}
	}
	

	public void writeExcel(int statusCode, String responseString) throws EncryptedDocumentException, IOException, InvalidFormatException, InvocationTargetException
	{
		FileInputStream fis = new FileInputStream("C:\\Users\\Lenovo\\git\\API\\API\\testdata.xlsx");
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheetAt(0);
		int rowEnd = sh.getLastRowNum();

		for(int i=count+1; i<= rowEnd;)
		{
				Row row = sh.getRow(i);
				int j = 1;			
				
				Cell statusCell = row.createCell(j);
				statusCell.setCellValue(statusCode);
				
				Cell cellResponse = row.createCell(j+1);
				cellResponse.setCellValue(responseString);
				
				FileOutputStream fos = new FileOutputStream("C:\\Users\\Lenovo\\git\\API\\API\\testdata.xlsx");
				wb.write(fos);
				fos.flush();
				fos.close();
				
				count++;
				
				break;
		}
			
		
	}

	public String addLocationToUrl(String city)
	{
	    if(!path.endsWith("?"))
	        path += "?";

	    List<NameValuePair> params = new LinkedList<NameValuePair>();

	    params.add(new BasicNameValuePair("address", city));
	    params.add(new BasicNameValuePair("key", "AIzaSyDcFk2h1S77eZhtkzM77L5NMZeplhEsBd4"));

	    String paramString = URLEncodedUtils.format(params, "utf-8");

	    String query = path + paramString;
	     		
		uri = url + query;
		
		return uri;
	}
	
	
	
	public void getGeocode(String uri) throws ClientProtocolException, IOException, EncryptedDocumentException, InvalidFormatException, InvocationTargetException
	{
		RestClient restClient = new RestClient();
		closeableHttpResponse = restClient.get(uri);
		
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		
		JSONObject responseJson = new JSONObject(responseString);
		gettest = new GetTest();
		gettest.writeExcel(statusCode, responseString);
	}	
}
