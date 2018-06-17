package kr.or.kpew.kieas.common;

	import java.io.BufferedReader;
	import java.io.DataOutputStream;
	import java.io.InputStreamReader;
	import java.net.HttpURLConnection;
	import java.net.URL;
	import java.net.URLEncoder;
	 
	public class Papago {
	    public static final int ko_to_en = 1;
	    public static final int en_to_ko = 2;
	  
	    private String clientId;      //애플리케이션 클라이언트 아이디값";
	    private String clientSecret;  //애플리케이션 클라이언트 시크릿값";
	     
	    public Papago () {
	     this.clientId = "XQh0Tr3kIX20FaPE8bqT";
	     this.clientSecret = "bjYRQfMn95";
	    }
	     
	  public String getTranslate(String text, int TranslateType){
	 
	   if (TranslateType == ko_to_en)
	    text = this.Translate(text,"ko","en");
	  else if (TranslateType == en_to_ko)
	    text = this.Translate(text,"en","ko");
	   
	  text = getTranslatedText(text);
	   return text;
	  }
	   
	  //API에 접속하여 문자열을 반환 (Java 기본 예제 변경)
	  private String Translate(String text, String sourceLang, String targetLang) {
	   if(clientId == null || clientSecret == null){
	    System.out.println("lsNull : ClientId OR ClientSecret");
	    return null;
	   }
	    
	      try {
	          text = URLEncoder.encode(text, "UTF-8");
	          String apiURL = "https://openapi.naver.com/v1/language/translate";
	          URL url = new URL(apiURL);
	          HttpURLConnection con = (HttpURLConnection)url.openConnection();
	          con.setRequestMethod("POST");
	          con.setRequestProperty("X-Naver-Client-Id", clientId);
	          con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	          // post request
	          String postParams = "source=" + sourceLang + "&target=" + targetLang + "&text=" + text;
	          con.setDoOutput(true);
	          DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	          wr.writeBytes(postParams);
	          wr.flush();
	          wr.close();
	          int responseCode = con.getResponseCode();
	          BufferedReader br;
	          if(responseCode==200) { // 정상 호출
	              br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
	          } else {  // 에러 발생
	              br = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
	          }
	          String inputLine;
	          String firstLine = new String("br.readLine() : " + br.readLine());
	          System.out.println(firstLine);
	          //inputLine = "br.readLine() : " + br.readLine();
	          StringBuffer response = new StringBuffer();
	          while ((inputLine = br.readLine()) != null) {
	              System.out.println(response.append(inputLine));
	          }
	          br.close();
	           
	          return firstLine;
	      } catch (Exception e) {
	          System.out.println(e);
	      }
	  return clientSecret;
	  }
	   
	  private String getTranslatedText(String text) {
	         String resultText = "translatedText";
	         int startIndex = text.indexOf(resultText) + resultText.length() + 3;
	         int endIndex = startIndex + text.substring(startIndex).indexOf('"');
	          
	         return text.substring(startIndex, endIndex);
	  }
	}

