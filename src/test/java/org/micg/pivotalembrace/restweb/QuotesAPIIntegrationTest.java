package org.micg.pivotalembrace.restweb;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.micg.pivotalembrace.springapp.SpringAppContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.micg.pivotalembrace.util.IntegrationTestConstants.BASE_URL;
import static org.micg.pivotalembrace.util.IntegrationTestConstants.SERVER_PORT;
import static org.micg.pivotalembrace.util.IntegrationTestConstants.APP_BASE_PATH;

/**
 *
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
        // get("/").then().assertThat().statusCode(200);
    }


}
