package javaCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Test{

    public static void DBSelect()
    {
        //db접속을 위한 5가지
        //1. ip(domain)
        //2. port
        //3. 계정
        //4. password
        //5. instance
        //jdbc:DB_VENDER://IP_ADDR:IP_PORT/INSTANCE;
        String url = "jdbc:mariadb://127.0.0.1:3306/testdbkor";
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

            String sql = "select name, email, password, register_date " +
                   "from zerobase_member ";
            preparedStatement = connection.prepareStatement(sql);

//            String sql = "select name, email, password, register_date " +
//                    "from zerobase_member " +
//                    "where password = ?";
//
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, "1234"); //?에다가 인덱스1에 1234를 넣음
            //쿼리실행
            rs = preparedStatement.executeQuery();

            //결과수행
            while(rs.next())
            {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String register_date = rs.getString("register_date");

                System.out.println(name + ", " + email+", "+password+", "+register_date);
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

    public static List<String> DBSelectWeb()
    {
    	List<String> member = new ArrayList<>();
    	
    	//db접속을 위한 5가지
        //1. ip(domain)
        //2. port
        //3. 계정
        //4. password
        //5. instance
        //jdbc:DB_VENDER://IP_ADDR:IP_PORT/INSTANCE;
        String url = "jdbc:mariadb://127.0.0.1:3306/testdbkor";
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

            String sql = "select name, email, password, register_date " +
                   "from zerobase_member ";
            preparedStatement = connection.prepareStatement(sql);

//            String sql = "select name, email, password, register_date " +
//                    "from zerobase_member " +
//                    "where password = ?";
//
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, "1234"); //?에다가 인덱스1에 1234를 넣음
            //쿼리실행
            rs = preparedStatement.executeQuery();

            //결과수행
            while(rs.next())
            {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String register_date = rs.getString("register_date");

                System.out.println(name + ", " + email+", "+password+", "+register_date);
                member.add(name);
                member.add(email);
                member.add(password);
                member.add(register_date);
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
        return member;
    }
    
    public static void DBInsert()
    {
        {
            //db접속을 위한 5가지
            //1. ip(domain)
            //2. port
            //3. 계정
            //4. password
            //5. instance
            //jdbc:DB_VENDER://IP_ADDR:IP_PORT/INSTANCE;
            String url = "jdbc:mariadb://127.0.0.1:3306/testdbkor";
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

            String name ="준선 서";
            String email = "tjwns909@nate.com";
            String mobile_no = "01031695773";
            String password = "4321";
            boolean marketing = true;


            //커넥션 객체 생성, 스테이먼트 객체 생성
            try {
                connection = DriverManager.getConnection(url, dbUserId, dbPassword);

                String sql = "insert into zerobase_member (name, email, mobile_no, password, marketing_yn, register_date)\n" +
                        "values(?, ?, ?, ?, ?, now())";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, mobile_no);
                preparedStatement.setString(4, password);
                preparedStatement.setBoolean(5, marketing);

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
    }

    public static void DBUpdate()
    {
        {
            //db접속을 위한 5가지
            //1. ip(domain)
            //2. port
            //3. 계정
            //4. password
            //5. instance
            //jdbc:DB_VENDER://IP_ADDR:IP_PORT/INSTANCE;
            String url = "jdbc:mariadb://127.0.0.1:3306/testdbkor";
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

            /*String name ="준선 서";
            String email = "tjwns909@nate.com";
            String mobile_no = "01031695773";
            String password = "4321";
            boolean marketing = true;*/


            //커넥션 객체 생성, 스테이먼트 객체 생성
            try {
                connection = DriverManager.getConnection(url, dbUserId, dbPassword);

                String sql = "update zerobase_member " +
                        "set password = ? " +
                        "where email = ?";
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, "1111");
                preparedStatement.setString(2, "test@gmail.com");

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
    }

    public static void DBDelete()
    {
        {
            //db접속을 위한 5가지
            //1. ip(domain)
            //2. port
            //3. 계정
            //4. password
            //5. instance
            //jdbc:DB_VENDER://IP_ADDR:IP_PORT/INSTANCE;
            String url = "jdbc:mariadb://127.0.0.1:3306/testdbkor";
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

            String email = "test2@gmail.com";


            //커넥션 객체 생성, 스테이먼트 객체 생성
            try {
                connection = DriverManager.getConnection(url, dbUserId, dbPassword);

                String sql = "delete from zerobase_member " +
                        "where email = ?";
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, email);

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
    }

    public static void main(String[] args) {
        DBManager.GetAndFillDB();
        System.out.println(DBManager.DB.length);
        System.out.println(DBManager.GetfinalPage());

    }
}
