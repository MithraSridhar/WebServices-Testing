package requests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PostIssueWithDes {

	public static void main(String[] args) {
		//Simple and recommended auth method
		
		RestAssured.baseURI="https://webservicestraining.atlassian.net/rest/api/3/";
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName("mithugopal30@gmail.com");
		authScheme.setPassword("MITHUsri1307*");
		RestAssured.authentication = authScheme;
		
		/*Map<String,String> authmap = new HashMap<String, String>();
		authmap.put("Authorization", "Basic bWl0aHVnb3BhbDMwQGdtYWlsLmNvbTpNSVRIVXNyaTEzMDcq");
		*/
				//POST new change request with priority 2
				Response responsepost = RestAssured
						.given()
						//.headers(authmap)
						.contentType(ContentType.JSON)
						.body(new File("./POST.json"))
						.post("issue");
				responsepost.prettyPrint();
				JsonPath jsonpost = responsepost.jsonPath();
				String id = jsonpost.get("id");
				System.out.println("Issue created with description "+id);
				String key = jsonpost.getString("key");
				System.out.println("Issue created with key "+key);
				
	}

}
