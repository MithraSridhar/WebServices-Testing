package requests;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class GetIncidentsContentType {

	public static void main(String[] args) {
		//Simple and recommended auth method
		
		RestAssured.authentication = RestAssured.basic("admin", "MITHUsri1307*");
		RestAssured.baseURI="https://dev71831.service-now.com/api/now/table/";
		
		Response response = RestAssured
				.given()
				
				//Auth for particular req
				//.auth()
				//.basic("admin", "MITHUsri1307*")
				
				//Get All incidents with urgency 2
				//.param("urgency", "2")
				
				//Giving multiple parameters
				//.params("priority", "3", "urgency","2")
				.accept(ContentType.XML)
				.get("incident");
		
		//To get whole response printed
		//response.prettyPrint();
		
		//To convert response o JSON format
		XmlPath xml = response.xmlPath();
		
		//To get all incidents numbers
		List<String> incidentnumbers = xml.getList("response.result.number");
		System.out.println("Incident count is "+incidentnumbers.size());
		for (String eachNumber : incidentnumbers) {
			System.out.println("Incident number is " +eachNumber);
		}
		//To get response time
Long time = response.getTime();
System.out.println("Response time is " +time);

//To get response code
int rescode = response.getStatusCode();
System.out.println("Response code is " +rescode);

//To get header
Headers headers =response.getHeaders();

for (Header header : headers) {
	System.out.println(header);
	
}

//To get cookies
Map<String,String> cookies =response.getCookies();
for (Entry<String, String> eachcookie : cookies.entrySet()) {
	System.out.println(eachcookie);
	
}
	}

}
