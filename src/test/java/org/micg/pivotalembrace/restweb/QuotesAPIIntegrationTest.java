package org.micg.pivotalembrace.restweb;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.micg.pivotalembrace.springapp.SpringAppContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.path.json.JsonPath.from;
import static org.micg.pivotalembrace.util.IntegrationTestConstants.BASE_URL;
import static org.micg.pivotalembrace.util.IntegrationTestConstants.SERVER_PORT;
import static org.micg.pivotalembrace.util.IntegrationTestConstants.APP_BASE_PATH;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * End-to-end (REST to DB) Integration Test for the <code>QuotesAPI</code> REST API.
 *
 * @author fsmicdev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringAppContext.class})
@ActiveProfiles("integration-test")
public class QuotesAPIIntegrationTest {

    public QuotesAPIIntegrationTest() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.basePath = APP_BASE_PATH + "quotes";
    }

    @Test
    public void getAllQuotes_success_200StatusAndAllQuotesReturned() {
        Response response = get("/").
                then().
                contentType(ContentType.JSON).
                assertThat().
                statusCode(200).
                extract().
                response();

        String jsonResponseAsStr = response.asString();

        ArrayList<Map<String,?>> jsonAsArrayList = from(jsonResponseAsStr).get("");

        assertThat(jsonAsArrayList.size(), greaterThan(0));
    }

    @Test
    public void getQuote_nonPlausibleQuoteId_400Status() {
        given().
                pathParam("id", -1).
                when().
                get("/quote/{id}").
                then().
                statusCode(400);
    }

    @Test
    public void getQuote_nonExistingQuoteId_404Status() {
        given().
                pathParam("id", 1892341873).
                when().
                get("/quote/{id}").
                then().
                statusCode(404);
    }

    @Test
    public void getQuote_existingQuoteId_200StatusAndQuoteReturned() {
        Response response = given().
                pathParam("id", 1).
                when().
                get("/quote/{id}").
                then().
                contentType(ContentType.JSON).
                statusCode(200).
                body("person", equalTo("Albert Einstein")).
                body("quote", equalTo("Anyone who has never made a mistake has never tried anything new.")).
                extract().
                response();

        String jsonResponseAsStr = response.asString();

        Map<String, ?> jsonAsMap = from(jsonResponseAsStr).get("");

        assertThat(jsonAsMap.size(), equalTo(3));
        assertThat(jsonAsMap.get("person"), equalTo("Albert Einstein"));
        assertThat(jsonAsMap.get("quote"), equalTo("Anyone who has never made a mistake has never tried anything new."));
    }

}
