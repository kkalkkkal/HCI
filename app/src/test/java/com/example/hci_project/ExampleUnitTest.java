package com.example.hci_project;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    // 주소 검색
    @Test
    public void getAddress_DAUM() {

        String data = "서울특별시 광진구 아차산로44길 26";
        final String Client_ID = "o82z0vth6u"; // 인증용 클라이언트 아이디
        final String Client_Secret = "LjN7euyVUTJlH2haz5RmIMmwwTVq77I6ODNipNrE"; // 클라이언트 인증키

        final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";

        try {
            String addr = URLEncoder.encode(data, "UTF-8");  // data : 주소입력
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr; //json
            //String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml

            // http 프로토콜로 api 호출
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", Client_ID);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", Client_Secret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 응답코드가 200이면 정상
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            //return response.toString(); // 응답 반환
        } catch (Exception e) {
            System.out.println(e);
            //return null; // 오류면 주소값 없이 반환
        }
    }


}