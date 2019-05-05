package requests;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetIssues {

	public static void main(String[] args) {
		
		RestAssured.baseURI="https://webservicestraining.atlassian.net/rest/api/3/";

		Map<String,String> authmap = new HashMap<String, String>();
		authmap.put("Authorization", "Basic bWl0aHVnb3BhbDMwQGdtYWlsLmNvbTpNSVRIVXNyaTEzMDcq");
		
		Map<String,String> quer = new HashMap<String, String>();
		quer.put("project", "WEEK");
		quer.put("fields", "created");
		


		Response response = RestAssured
				.given()
				.headers(authmap)
				//.param("sysparm_fields", "created,sys_id")
				.queryParams(quer)
				
				//.log().all()				
				.get("search");
		
		//To get whole response printed
		response.prettyPrint();


		//To convert response o JSON format
		JsonPath json = response.jsonPath();
		
		//To get all incidents numbers
		List<String> id = json.getList("issues.id");
		System.out.println("Issue count is "+id.size());
		Collections.sort(id);
		for (String eachkey : id) {
			System.out.println("ID is " +eachkey);
		}
		String idfirst = id.get(0);
		System.out.println("ID first is "+idfirst);

		/*List<String> issuecreateddates = json.getList("issues.fields.created");
		
		System.out.println("Issue is "+issuecreateddates.size());
Collections.sort(issuecreateddates);
for (String issuecreateddate : issuecreateddates) {
	System.out.println("sorted date is "+issuecreateddate);
}
String datefirst = issuecreateddates.get(0);
System.out.println("date first is "+datefirst);
*/



		Response responsedelete = RestAssured
				.given()
				.headers(authmap)
				.log().all()
				.contentType(ContentType.JSON)
				.pathParam("ID", idfirst)				
				.delete("issue/{ID}");
		responsedelete.prettyPrint();
		int statuscode = responsedelete.getStatusCode();
		System.out.println(statuscode);
if(statuscode==204) {
System.out.println("Success");
}
else {
System.out.println("Fail");
}

	}

}
