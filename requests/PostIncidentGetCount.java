package requests;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PostIncidentGetCount {

	public static void main(String[] args) {
		//Simple and recommended auth method
		
		RestAssured.authentication = RestAssured.basic("admin", "MITHUsri1307*");
		RestAssured.baseURI="https://dev71831.service-now.com/api/now/table/";

		//To get change requests count
		Response response = RestAssured
				.given()
				.get("change_request");
		//To convert response o JSON format
		JsonPath json = response.jsonPath();
		
		//To get all incidents numbers
		List<String> incidentnumbers = json.getList("result.number");
		int incsizeb = incidentnumbers.size();
		System.out.println("Change requests count before POST new issue "+incsizeb);
		
		//POST new change request with priority 2
		Response responsepost = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("{\r\n" + 
						" \"priority\": \"2\"\r\n" + 
						" }")
				.post("change_request");
		//responsepost.prettyPrint();
		JsonPath jsonpost = responsepost.jsonPath();
		String priority = jsonpost.getString("result.priority");
		System.out.println("Change request created with the priority "+priority);
		if(priority.equals("2")) {
			System.out.println("SUCCESS, Created issue with priority 2");
		}
		else {
			System.out.println("FAIL, Created issue not with priority 2");
		}
		
	//Get change requests count after POST	
		Response responseget = RestAssured
				.given()
				.get("change_request");
		
		//To convert response o JSON format
		JsonPath jsonget = responseget.jsonPath();
		
		//To get all incidents numbers
		List<String> incidentnumbersget = jsonget.getList("result.number");
		int incsize = incidentnumbersget.size();
		System.out.println("Change request after POST "+incsize);

	
	
	if(incsizeb==incsize-1) {
		System.out.println("SUCCESS");
	}
	else
	{
		System.out.println("FAIL");
	}

}
}