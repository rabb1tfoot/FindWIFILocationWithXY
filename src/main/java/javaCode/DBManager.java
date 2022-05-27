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
import java.util.HashMap;
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

class DistanceDB
{
	double distance;
	WifiInfo DB;
	
	public DistanceDB(double distance, WifiInfo DB)
	{
		this.distance = distance;
		this.DB = DB;
	}
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
	
	//Data값 불러오기
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
		 			DB[i].SetParam(j, obj.get(Keys.arrKey[j]).toString());
		 		}
		 	}
		 	
		 	if(isFillend)
		 	{
		 		break;
		 	} 
		 	count++;
		}
		//DBinsertWifiInfo
 		InsertWifiInfo(DB);
 		
	}
	private static void InsertWifiInfo(WifiInfo[] DBarray)
	{ 
        int count = 0;
		//db접속을 위한 5가지
        //1. ip(domain)
        //2. port
        //3. 계정
        //4. password
        //5. instance
        //jdbc:DB_VENDER://IP_ADDR:IP_PORT/INSTANCE;
        String url = "jdbc:mariadb://127.0.0.1:3306/wifiinfo";
        String dbUserId = "root";
        String dbPassword = "232723";

        //1.드라이버 로드
        //2. 커넥션 객체 생성
        //3. 스테이먼트 객체 생성
        //4. 쿼리 실행
        //5. 결과 수행
        //6. 객체 연결 해제 (close)

        //드라이버 로드
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        while(count < DBarray.length)
        {
        	
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        
        //커넥션 객체 생성, 스테이먼트 객체 생성
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            for(int i = count; i < 900 + count; ++i)
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
                preparedStatement.setDouble(1, 0.0);
                for(int j = 2; j < 18; ++j)
                {              	
                    preparedStatement.setString(j, DBarray[i].GetParam(j-2));
                }
                //쿼리실행
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows > 0)
                {
                    System.out.println(i);
                }
                else
                {
                    System.out.println("실패");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //객체 연결 해제
            try {
                if(rs!= null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(preparedStatement!= null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        	}
        }
	}
	
	//두점 사이의 거리를 구하고 가까운 거리로 정렬 후, index page에 뿌리고 히스토리 DB에 insert한다.
	public static void FindaroundWifi(double x, double y)
	{
		if(DB == null)
		{
			System.out.println("DB is null");
			return;
		}
		
		DistanceDB[] DBarray = new DistanceDB[20];
		
		for(int i = 0; i <DB.length; ++i)
		{
			double dbX = Double.parseDouble(DB[i].GetParam(Keys.mapKey.get("LAT")));
			double dbY = Double.parseDouble(DB[i].GetParam(Keys.mapKey.get("LNT")));
			
			double distance = Math.sqrt(Math.pow(dbX - x, 2) + Math.pow(dbY - y, 2));
			
			for(int j = 0; j < 20; ++j)
			{
				if(DBarray[j] == null)
				{
					DBarray[j] = new DistanceDB(distance, DB[i]);
					
				}
				//바로바로 비교하면서 20개보다 가까운값이면 해당자리에 넣고 한칸씩 민다.
				if(DBarray[j].distance > distance)
				{
					DistanceDB temp = DBarray[j];
					DBarray[j] = new DistanceDB(distance, DB[i]);
					
					for(int k = 18; k > j; --k)
					{
						if(DBarray[k] == null)
						{
							continue;
						}
						DBarray[k+1] = DBarray[k];
					}
					
					break;
				}
			}
			//WifiInfo insert부분
			//InsertWifiInfo(DBarray);
			
			//history insert 부분
			//InsertHistoryDB(x,y);
		}
	}
	
	private static void InsertHistoryDB(double x, double y)
	{
		//db접속을 위한 5가지
        //1. ip(domain)
        //2. port
        //3. 계정
        //4. password
        //5. instance
        //jdbc:DB_VENDER://IP_ADDR:IP_PORT/INSTANCE;
        String url = "jdbc:mariadb://127.0.0.1:3306/wifiinfo";
        String dbUserId = "root";
        String dbPassword = "232723";

        //1.드라이버 로드
        //2. 커넥션 객체 생성
        //3. 스테이먼트 객체 생성
        //4. 쿼리 실행
        //5. 결과 수행
        //6. 객체 연결 해제 (close)

        //드라이버 로드
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;


        //커넥션 객체 생성, 스테이먼트 객체 생성
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "insert into history (lat, lnt, search_dt)\n" +
                    "values(?, ?,now())";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(2, x);
            preparedStatement.setDouble(3, y);

            //쿼리실행
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows > 0)
            {
                System.out.println("실행 성공");
            }
            else
            {
                System.out.println("실패");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //객체 연결 해제
            try {
                if(rs!= null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(preparedStatement!= null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
