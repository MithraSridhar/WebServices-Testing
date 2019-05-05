package requests;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteRequestWithPathParam {

	public static void main(String[] args) {
		//Simple and recommended auth method
		
				RestAssured.authentication = RestAssured.basic("admin", "MITHUsri1307*");
				RestAssured.baseURI="https://dev71831.service-now.com/api/now/table/";
				
				//POST new change request with priority 2
				Response responsedelete = RestAssured
						.given()
						.log().all()
						.contentType(ContentType.JSON)
						.pathParam("sys_id", "3d799db6db412300d65aa08a4896193f")
						
						.delete("change_request/{sys_id}");
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
