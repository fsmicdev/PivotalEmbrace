package org.micg.pivotalembrace.restweb;

import org.hamcrest.junit.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.micg.pivotalembrace.model.document.Quotes;
import org.micg.pivotalembrace.service.QuotesService;
import org.micg.pivotalembrace.service.ServiceException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import static org.micg.pivotalembrace.model.apirest.ErrorCode.INVALID_PARAMS;

import static org.micg.pivotalembrace.model.apirest.ErrorCode.NOT_FOUND;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 *
 * @author fsmicdev
 */
@RunWith(MockitoJUnitRunner.class)
public class QuotesAPITest {

    @Mock
    private QuotesService quotesService;

    @InjectMocks
    private QuotesAPI quotesAPI;

    private List quotes;

    private Quotes quoteOne;
    private Quotes quoteTwo;
    private Quotes quoteThree;

    private Map<String, AtomicInteger> quotesAuthorsToQuoteCountMap = new HashMap<>();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        quotes = new ArrayList();

        quoteOne = new Quotes();
        quoteOne.setId(1L);
        quoteOne.setPerson("Albert Einstein");
        quoteOne.setQuote("Anyone who has never made a mistake has never tried anything new.");

        quoteTwo = new Quotes();
        quoteTwo.setId(2L);
        quoteTwo.setPerson("Martin Fowler");
        quoteTwo.setQuote("Any fool can write code that a computer can understand. Good programmers write code that humans can understand.");

        quoteThree = new Quotes();
        quoteThree.setId(3L);
        quoteThree.setPerson("Albert Einstein");
        quoteThree.setQuote("Life is like riding a bicycle. To keep your balance, you must keep moving.");

        quotes.add(quoteOne);
        quotes.add(quoteTwo);

        quotesAuthorsToQuoteCountMap = new HashMap<>();
        quotesAuthorsToQuoteCountMap.put("Albert Einstein", new AtomicInteger(2));
        quotesAuthorsToQuoteCountMap.put("Martin Fowler", new AtomicInteger(1));
    }

    @Test
    public void getAllQuotes_success_200StatusAndAllQuotesReturned() {
        try {
            // Expectations
            when(quotesService.getAllQuotes()).thenReturn(quotes);

            // Call the actual method under test
            Response response = quotesAPI.getAllQuotes();

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(quotesService).getAllQuotes();

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            List<Quotes> quotesList = (List<Quotes>)respEntity;

            assertThat(respEntity, is(notNullValue()));
            assertThat(quotesList, hasSize(2));
            assertThat(quotesList, equalTo(quotes));

            Quotes quoteOneRes = quotesList.get(0);
            Quotes quoteTwoRes = quotesList.get(1);

            assertThat(quoteOneRes.getId(), is(equalTo(1L)));
            assertThat(quoteOneRes.getPerson(), is(equalTo("Albert Einstein")));
            assertThat(quoteOneRes.getQuote(), is(equalTo("Anyone who has never made a mistake has never tried anything new.")));

            assertThat(quoteTwoRes.getId(), is(equalTo(2L)));
            assertThat(quoteTwoRes.getPerson(), is(equalTo("Martin Fowler")));
            assertThat(quoteTwoRes.getQuote(), is(equalTo("Any fool can write code that a computer can understand. Good programmers write code that humans can understand.")));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void getQuote_invalidTooLowId_400BadRequestResponse() throws ServiceException {
        // Expectations
        when(quotesService.getQuote(-1L)).thenThrow(new ServiceException(INVALID_PARAMS));

        // Call the actual method under test
        Response response = quotesAPI.getQuote(-1L);

        // Verify (and Validation)
        assertThat(response, is(notNullValue()));

        verify(quotesService).getQuote(-1L);

        Object respEntity = response.getEntity();

        assertThat(respEntity, is(nullValue()));

        assertThat(response.getStatus(), is(equalTo(INVALID_PARAMS.getHttpStatusErrorCode())));
        assertThat(response.getStatusInfo().getReasonPhrase(), is(equalTo("Bad Request")));
    }

    @Test
    public void getQuote_plausibleIdButNonExistingQuote_404NotFoundResponse() throws ServiceException {
        // Expectations
        when(quotesService.getQuote(1000000L)).thenThrow(new ServiceException(NOT_FOUND));

        // Call the actual method under test
        Response response = quotesAPI.getQuote(1000000L);

        // Verify (and Validation)
        assertThat(response, is(notNullValue()));

        verify(quotesService).getQuote(1000000L);

        Object respEntity = response.getEntity();

        assertThat(respEntity, is(nullValue()));

        assertThat(response.getStatus(), is(equalTo(NOT_FOUND.getHttpStatusErrorCode())));
        assertThat(response.getStatusInfo().getReasonPhrase(), is(equalTo("Not Found")));
    }

    @Test
    public void getQuote_plausibleId_200StatusAndQuoteReturned() {
        try {
            // Expectations
            when(quotesService.getQuote(1L)).thenReturn(quoteOne);

            // Call the actual method under test
            Response response = quotesAPI.getQuote(1L);

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(quotesService).getQuote(1L);

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            assertThat(respEntity, is(notNullValue()));

            Quotes quoteEntityRet = (Quotes)respEntity;

            assertThat(quoteEntityRet.getId(), is(equalTo(1L)));
            assertThat(quoteEntityRet.getPerson(), is(equalTo("Albert Einstein")));
            assertThat(quoteEntityRet.getQuote(), is(equalTo("Anyone who has never made a mistake has never tried anything new.")));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

    @Test
    public void getAllAuthorsAndAuthorQuoteCount_success_200StatusAndQuotesCountReturned() {
        try {
            // Expectations
            when(quotesService.getAllAuthorsAndQuoteCount()).thenReturn(quotesAuthorsToQuoteCountMap);

            // Call the actual method under test
            Response response = quotesAPI.getAllAuthorsAndAuthorQuoteCount();

            // Verify (and Validation)
            assertThat(response, is(notNullValue()));

            verify(quotesService).getAllAuthorsAndQuoteCount();

            assertThat(response.getStatus(), is(equalTo(Response.Status.OK.getStatusCode())));

            Object respEntity = response.getEntity();

            Map<String, AtomicInteger> allAuthorsAndAuthorQuoteCount = (Map<String, AtomicInteger>)respEntity;

            assertThat(respEntity, is(notNullValue()));

            AtomicInteger albertEinsteinCount = allAuthorsAndAuthorQuoteCount.get("Albert Einstein");
            AtomicInteger martinFowlerCount = allAuthorsAndAuthorQuoteCount.get("Martin Fowler");

            assertThat(albertEinsteinCount, is(notNullValue()));
            assertThat(martinFowlerCount, is(notNullValue()));

            assertThat(albertEinsteinCount.intValue(), is(equalTo(2)));
            assertThat(martinFowlerCount.intValue(), is(equalTo(1)));
        } catch (final ServiceException se) {
            fail("No ServiceException should've been thrown");
        }
    }

}
