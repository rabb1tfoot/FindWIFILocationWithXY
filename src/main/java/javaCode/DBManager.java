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
        String url = "jdbc:mariadb://127.0.0.1:3306/wifiinfo";
        String dbUserId = "root";
        String dbPassword = "232723";
       
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
        return returnvalue;
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
		 			if(j == 13 || j == 14)
		 			{
		 				String[] temp = obj.get(Keys.arrKey[j]).toString().split("\"");
			 			DB[i].SetParam(j, temp[1]);
		 			}
		 			else
		 			{
		 				DB[i].SetParam(j, obj.get(Keys.arrKey[j]).toString());
		 			}
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
        int OldCount = count;
        
        //커넥션 객체 생성, 스테이먼트 객체 생성
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
	
	//DB 다 가져와서 distance 구한뒤 update해준다. select limit 20개 해준다.
	public static void FindaroundWifi(double x, double y)
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
            String sql = "Select LAT,LNT FROM wifiinfo";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while(rs.next()) // Double.parseDouble시 에러나서 분기를 나눔
            {
            	Double x2 = Double.parseDouble(rs.getString("LNT")); //좌표정보가 거꾸로 되어있어서 반대 킷값을 넣어야 옳은 데이터가 가져옴
                Double y2 = Double.parseDouble(rs.getString("LAT"));
                
                Double distance = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
                
                sql = "update wifiinfo set DISTANCE = ? WHERE LAT = ? AND LNT = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDouble(1, distance);
                preparedStatement.setDouble(2, y2);
                preparedStatement.setDouble(3, x2);
                int affectedRows = preparedStatement.executeUpdate();
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
        isSetdistance = true;
	}
	
	public static void InsertHistoryDB(double x, double y)
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
            preparedStatement.setDouble(1, x);
            preparedStatement.setDouble(2, y);

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
	
	public static List<History> GetHistoryDB()
	{
		List<History> listHis = new ArrayList<History>();
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
        	String sql = "select ID, LAT, LNT, SEARCH_DT " +
                   "from HISTORY";
            preparedStatement = connection.prepareStatement(sql);

            //쿼리실행
            rs = preparedStatement.executeQuery();

            //결과수행
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
		
		return listHis;
	}
	
	public static void DelHistory(String id)
	{
		List<History> listHis = new ArrayList<History>();
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
            System.out.println("삭제 중");
            String sql = "delete from HISTORY where id = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);

            //쿼리실행
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows > 0)
            {
                System.out.println("삭제 성공");
            }
            else
            {
                System.out.println("삭제 실패");
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
