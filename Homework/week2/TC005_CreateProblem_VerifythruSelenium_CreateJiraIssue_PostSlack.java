package tests.rest;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lib.rest.RESTAssuredBase;
import pages.selenium.LoginPage;
import lib.selenium.PreAndPost;

public class TC005_CreateProblem_VerifythruSelenium_CreateJiraIssue_PostSlack extends PreAndPost {

	@BeforeTest
	public void setValues() {

		testCaseName = "Create a Problem: Check through UI selenium : Create Jira issue and Post on Slack";
		testDescription = "Week2 Assignments";
		nodes = "Problem";
		authors = "Mithra";
		category = "smoke";
		dataSheetName = "TC005";
		// dataFileType = "Excel";
	}

	@Test(dataProvider = "fetchData")
	public void createProblem(String uName, String pwd) {
		
		// Creating problem on ServiceNow through API
		Response response = RestAssured.given().auth().basic("admin", "MITHUsri1307*").log().all()
				.contentType(ContentType.JSON).post("https://dev71831.service-now.com/api/now/table/problem");

		RESTAssuredBase.verifyResponseCode(response, 201);

		// Verify the Content by Specific Key
		problemNumber = RESTAssuredBase.getContentWithKey(response, "result.number");
		problemDescription = RESTAssuredBase.getContentWithKey(response, "result.description");
		problemUrgency = RESTAssuredBase.getContentWithKey(response, "result.urgency");
		System.out.println("Problem number is " + problemNumber);
		System.out.println("Problem description is " + problemDescription);
		System.out.println("Problem urgency is " + problemUrgency);

		// Check problem created through selenium API
		new LoginPage(driver, test).typeUserName(uName).typePassword(pwd).clickLogIn().searchUsingFilter("problem")
				.clickOpen().typeAndEnterSearch(problemNumber);
		Map<String, String> authmap = new HashMap<String, String>();
		authmap.put("Authorization", "Basic bWl0aHVnb3BhbDMwQGdtYWlsLmNvbTpNSVRIVXNyaTEzMDcq");

		// Creating Jira issue from above info
		Response responseJira = RestAssured.given().headers(authmap).log().all().contentType(ContentType.JSON)
				.body("{\r\n" + "  \r\n" + "    \"fields\": {\r\n" + "        \"project\": {\r\n"
						+ "            \"id\": \"10001\"\r\n" + "        },\r\n"
						+ "        \"summary\": \"Service now problem description is " + problemDescription
						+ " and Urgency is " + problemUrgency + "\",\r\n" + "        \"issuetype\": {\r\n"
						+ "            \"id\": \"10008\"\r\n" + "        },\r\n" + "        \"description\": {\r\n"
						+ "                    \"version\": 1,\r\n" + "                    \"type\": \"doc\",\r\n"
						+ "                    \"content\": [\r\n" + "                        {\r\n"
						+ "                            \"type\": \"paragraph\",\r\n"
						+ "                            \"content\": [\r\n" + "                                {\r\n"
						+ "                                    \"type\": \"text\",\r\n"
						+ "                                    \"text\": \"" + problemDescription
						+ " Service now problem number is " + problemNumber + "\"\r\n"
						+ "                                }\r\n" + "                            ]\r\n"
						+ "                        }\r\n" + "                    ]\r\n" + "                }\r\n"
						+ "}\r\n" + "\r\n" + "	\r\n" + "}")
				.post("https://webservicestraining.atlassian.net/rest/api/3/issue");

		RESTAssuredBase.verifyResponseCode(responseJira, 201);

		// Get the created issue number
		issueNumber = RESTAssuredBase.getContentWithKey(responseJira, "id");
		System.out.println("Jira created issue ID is" + issueNumber);

		// Get Jira created issue details
		Response responseget = RestAssured.given().headers(authmap)

				.log().all().pathParam("issueid", issueNumber)
				.get("https://webservicestraining.atlassian.net/rest/api/3/issue/{issueid}");

		//responseget.prettyPrint();
		issueDescription = RESTAssuredBase.getContentWithKey(responseget, "fields.issuetype.description");
		issuePriority = RESTAssuredBase.getContentWithKey(responseget, "fields.priority.id");
		issueAssignee = RESTAssuredBase.getContentWithKey(responseget, "fields.assignee");
		issueContent = RESTAssuredBase.getContentWithKey(responseget, "fields.description.content[0].content[0].text");
		issueProjectID = RESTAssuredBase.getContentWithKey(responseget, "fields.project.id");
		issueProjectKey = RESTAssuredBase.getContentWithKey(responseget, "fields.project.key");
		issueCreator = RESTAssuredBase.getContentWithKey(responseget, "fields.creator.emailAddress");

		System.out.println("Issue description is: " + issueDescription);
		System.out.println("Issue urgency is: " + issuePriority);
		System.out.println("Issue assignee is: " + issueAssignee);
		System.out.println("Issue content is: " + issueContent);
		System.out.println("Issue Project ID is: " + issueProjectID);
		System.out.println("Issue Project Key is: " + issueProjectKey);
		System.out.println("Issue Creator is: " + issueCreator);

		// Posting issue details on Slack
		Response responsepostslack = RestAssured.given().log().all().post(
				"https://slack.com/api/chat.postMessage?token=xoxp-459312459686-459144083287-458161253186-972a4525f9dcf43271717f289f1ee9b7&channel=CDF9ZEWFK&text=Jira Created details as follows, Issue description: "
						+ issueDescription + " , Issue content: " + issueContent + ", Issue Priority: " + issuePriority
						+ " , Issue assignee: " + issueAssignee + " , Issue creator: " + issueCreator
						+ " , Issue project ID: " + issueProjectKey + " , Issue project key: " + issueProjectID
						+ "- Posted through REST Assured &pretty=1&as_user=gopal30");

		// responsepostslack.prettyPrint();

		System.out.println(+responsepostslack.getStatusCode());

	}
}
