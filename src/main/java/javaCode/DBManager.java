package javaCode;

import java.io.BufferedReader;
import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	public static Map<String, Integer> mapKey = new HashMap<>() {{
		put("X_SWIFI_MGR_NO",0);
		put("X_SWIFI_WRDOFC",1);
		put("X_SWIFI_MAIN_NM",2);
		put("X_SWIFI_ADRES1",3);
		put("X_SWIFI_ADRES2",4);
		put("X_SWIFI_INSTL_FLOOR",5);
		put("X_SWIFI_INSTL_TY",6);
		put("X_SWIFI_INSTL_MBY",7);
		put("X_SWIFI_SVC_SE",8);
		put("X_SWIFI_CMCWR",9);
		put("X_SWIFI_CNSTC_YEAR",10);
		put("X_SWIFI_INOUT_DOOR",11);
		put("X_SWIFI_REMARS3",12);
		put("LAT",13);
		put("LNT",14);
		put("WORK_DTTM",15);
	}};
}

public class DBManager {
	
	private static boolean isFillend = false;
	public static WifiInfo[] DB;
	private static int finalPage;
	private static List<WifiInfo> wifiInfos;
	private static boolean isSetdistance = false;
	
	//SQL_DATABASE
	private static String  url = "jdbc:mariadb://127.0.0.1:3306/wifiinfo";
	private static String dbUserId = "root";
	private static String dbPassword = "232723";
	
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet rs = null;
	
    private static void InitDB()
    {
    	try{
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    	
    	connection = null;
        preparedStatement = null;
        rs = null;
    }
    
    private static void ReleaseDB()
    {
            //?????? ?????? ??????
            try {
                if(rs!= null && !rs.isClosed()){
                    rs.close();
                    rs = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(preparedStatement!= null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                    preparedStatement = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection != null && !connection.isClosed()){
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
	public static boolean IsSetDistance()
	{
		return isSetdistance;
	}
	
	public static List<WifiInfo> GetwifiInfos()
	{
		return wifiInfos;
	}
	
	public static int GetfinalPage()
	{
		return finalPage;
	}
	
	public static boolean IsSaved()
	{
		boolean returnvalue = false;
        InitDB();
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);          
            String sql = "select COUNT(*) as cnt from wifiinfo";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while(rs.next())
            {
            	Long cnt = rs.getLong("cnt");
                if(cnt > 0)
                {
                	returnvalue = true;
                }
            }
        	}catch (SQLException e) {
            e.printStackTrace();
            } 
        ReleaseDB();
        return returnvalue;
	}
	
	//Data??? ????????????
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
		 	
		 	jsonObj = (JsonObject) jsonParser.parse(json.toString());
		 	JsonArray jsonarr = (JsonArray) jsonObj.get("row");
		 	JsonObject objOnly = (JsonObject) jsonarr.get(0);
		 	
		 	if(DB == null)
		 	{
		 		finalPage = jsonObj.get("list_total_count").getAsInt();
		 		DB = new WifiInfo[finalPage];
		 	}
		 	CheckReachEndPage(startEndPage, finalPage);
		 	
		 	for(int i = 0; i < jsonarr.size(); ++i)
		 	{
		 		JsonObject obj = (JsonObject) jsonarr.get(i);
		 		DB[i + count * 1000] = new WifiInfo();
		 		for(int j = 0; j < Keys.arrKey.length; ++j)
		 		{
		 			if(j == 13 || j == 14)
		 			{
		 				String[] temp = obj.get(Keys.arrKey[j]).toString().split("\"");
			 			DB[i + count * 1000].SetParam(j, temp[1]);
		 			}
		 			else
		 			{
		 				DB[i + count * 1000].SetParam(j, obj.get(Keys.arrKey[j]).toString());
		 			}
		 		}
		 	}
		 	
		 	if(isFillend)
		 	{
		 		break;
		 	} 
		 	count++;
		}
		//DB??????
 		InsertWifiInfo(DB);
 		
	}
	private static void InsertWifiInfo(WifiInfo[] DBarray)
	{ 
        int count = 0;
		InitDB();
        while(count < DBarray.length)
        {
        int OldCount = count;
        
        //????????? ?????? ??????, ??????????????? ?????? ??????
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            for(int i = count; i < 1000 + OldCount; ++i)
            {
            	if(count == DBarray.length)
            	{
            		break;
            	}
            	count++;
            	String sql = "insert into wifiinfo (DISTANCE, MGR_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, "+
                		"INSTL_FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, CNSTC_YEAR, INOUT_DOOR, REMARS3, "+
                		"LAT, LNT, WORK_DTTM"+
                        ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDouble(1, 0.0); //????????? ?????????????????? ??????
                for(int j = 2; j < 18; ++j)
                {              	
                    preparedStatement.setString(j, DBarray[i].GetParam(j-2));
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        ReleaseDB();
        }
	}
	
	//DB ??? ???????????? distance ????????? update?????????. select limit 20??? ?????????.
	public static void FindaroundWifi(double x, double y)
	{
		InitDB();
        //????????? ?????? ??????, ??????????????? ?????? ??????
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);          
            String sql = "Select LAT,LNT FROM wifiinfo";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while(rs.next()) // Double.parseDouble??? ???????????? ????????? ??????
            {
            	Double x2 = Double.parseDouble(rs.getString("LNT")); //??????????????? ????????? ??????????????? ?????? ????????? ????????? ?????? ???????????? ?????????
                Double y2 = Double.parseDouble(rs.getString("LAT"));
                
                Double distance = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
                
                sql = "update wifiinfo set DISTANCE = ? WHERE LAT = ? AND LNT = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDouble(1, distance);
                preparedStatement.setDouble(2, y2);
                preparedStatement.setDouble(3, x2);
                preparedStatement.executeUpdate();
            }
            sql = "Select * FROM wifiinfo ORDER BY DISTANCE ASC LIMIT 20";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            wifiInfos = new ArrayList<WifiInfo>();
            while(rs.next())
            {
            	WifiInfo db = new WifiInfo();
            	db.SetParam(0, String.valueOf(rs.getDouble(1)));
            	for(int i = 1; i < db.ParamSize(); ++i)
            	{
            		db.SetParam(i, String.valueOf(rs.getString(i+1)));
            	}
            	wifiInfos.add(db);
            } 
            
            
        	}catch (SQLException e) {
            e.printStackTrace();
            } 
        ReleaseDB();
        isSetdistance = true;
	}
	
	public static void InsertHistoryDB(double x, double y)
	{
		InitDB();
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "insert into history (lat, lnt, search_dt)\n" +
                    "values(?, ?,now())";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, x);
            preparedStatement.setDouble(2, y);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        ReleaseDB();
    }
	
	public static List<History> GetHistoryDB()
	{
		List<History> listHis = new ArrayList<History>();
		InitDB();
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);
        	String sql = "select ID, LAT, LNT, SEARCH_DT from HISTORY";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();

            while(rs.next())
            {
            	History arrHis = new History();
            	arrHis.SetParam(0, rs.getString("ID"));
            	arrHis.SetParam(1, rs.getString("LAT"));
            	arrHis.SetParam(2, rs.getString("LNT"));
            	arrHis.SetParam(3, rs.getString("SEARCH_DT"));
            	listHis.add(arrHis);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } ReleaseDB();
		
		return listHis;
	}
	
	public static void DelHistory(String id)
	{
		List<History> listHis = new ArrayList<History>();
		InitDB();
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);
            System.out.println("?????? ???");
            String sql = "delete from HISTORY where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } ReleaseDB();
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
