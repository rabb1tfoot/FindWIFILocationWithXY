package javaCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class Keys
{
	public static String[] arrKey = new String[] 
			{"X_SWIFI_MGR_NO"
			,"X_SWIFI_WRDOFC"
			,"X_SWIFI_MAIN_NM"
			,"X_SWIFI_ADRES1"
			,"X_SWIFI_ADRES2"
			,"X_SWIFI_INSTL_FLOOR"
			,"X_SWIFI_INSTL_TY"
			,"X_SWIFI_INSTL_MBY"
			,"X_SWIFI_SVC_SE"
			,"X_SWIFI_CMCWR"
			,"X_SWIFI_CNSTC_YEAR"
			,"X_SWIFI_INOUT_DOOR"
			,"X_SWIFI_REMARS3"
			,"LAT"
			,"LNT"
			,"WORK_DTTM"};
}

public class DBManager {
	
	private static boolean isFillend = false;
	public static WifiInfo[] DB;
	private static int finalPage;
	
	public static int GetfinalPage()
	{
		return finalPage;
	}
	
	public static boolean IsFillend()
	{
		return isFillend;
	}
	
	
	public static void GetAndFillDB()
	{
		String key = "564b6d4257746a773630454e4d4e53";
		String[] startEndPage = {"1", "1000"};
		int count = 0;
		while(true)
		{
			String sURL = "http://openapi.seoul.go.kr:8088/"+key+"/json/TbPublicWifiInfo/" + startEndPage[0] +"/"+ startEndPage[1]+"/";
			URL url = null;
			try {
				url = new URL(sURL);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpURLConnection con = null;
			InputStream is = null;
			try {
				con = (HttpURLConnection)url.openConnection();
				is = con.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String result = new BufferedReader(new InputStreamReader(is))
					   .lines().collect(Collectors.joining("\n"));
			
		 	Gson gson = new Gson();
		 	JsonElement json = gson.fromJson(result, JsonElement.class);
		 	
		 	JsonParser jsonParser = new JsonParser();
		 	JsonObject jsonObj = (JsonObject) jsonParser.parse(json.toString());
		 	json = jsonObj.get("TbPublicWifiInfo");
		 	if(count == 14)
		 	{
		 		int b = 0;
		 	}
		 	System.out.println(count);
		 	
		 	jsonObj = (JsonObject) jsonParser.parse(json.toString());
		 	JsonArray jsonarr = (JsonArray) jsonObj.get("row");
		 	JsonObject objOnly = (JsonObject) jsonarr.get(0);
		 	
		 	if(DB == null)
		 	{
		 		finalPage = jsonObj.get("list_total_count").getAsInt();
		 		DB = new WifiInfo[finalPage];
		 	}
		 	CheckReachEndPage(startEndPage, finalPage);
		 	
		 	for(int i = count; i < jsonarr.size(); ++i)
		 	{
		 		JsonObject obj = (JsonObject) jsonarr.get(i);
		 		DB[i] = new WifiInfo();
		 		for(int j = 0; j < Keys.arrKey.length; ++j)
		 		{
		 			DB[i].SetParam(j, obj.get(Keys.arrKey[j]).toString());
		 		}
		 	}
		 	
		 	if(isFillend)
		 	{
		 		break;
		 	}
		 	count++;
		}
	}
	public static void FindaroundWifi(int x, int y)
	{
		
	}
	private static void CheckReachEndPage(String[] startEndPage, int finalPage)
	{
		int startpage = Integer.parseInt(startEndPage[0]);
		int endpage =  Integer.parseInt(startEndPage[1]);		
		if(endpage > finalPage)
		{
			isFillend = true;
		}
		else if(endpage + 1000 > finalPage)
		{
			endpage = Integer.parseInt(startEndPage[1]) +1000 - finalPage + endpage;
		}
		else
		{
			endpage += 1000;		
		}
		startpage += 1000;
		startEndPage[0] = String.valueOf(startpage);
		startEndPage[1] = String.valueOf(endpage);
			
	}

	
}
