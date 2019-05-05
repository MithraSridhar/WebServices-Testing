package requests;

import java.util.List;
import java.util.Set;

import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetAllIssuesCreatedDate {

	public static void main(String[] args) {
		//Simple and recommended auth method
		
		RestAssured.authentication = RestAssured.basic("admin", "MITHUsri1307*");
		RestAssured.baseURI="https://dev71831.service-now.com/api/now/table/";
	
		Response response = RestAssured
				.given()
			//Giving multiple parameters
				//.params("priority", "3", "urgency","2")
				.queryParam("sysparm_fields", "sys_created_on")
				.get("incident");
		
		//To get whole response printed
		response.prettyPrint();
		
		//To convert response o JSON format
		JsonPath json = response.jsonPath();
		
		//To get all incidents numbers
		Set<String> createddate = json.get("result.sys_created_on");
		System.out.println(createddate);
		System.out.println("Incident count is "+createddate.size());
		
	}

}
