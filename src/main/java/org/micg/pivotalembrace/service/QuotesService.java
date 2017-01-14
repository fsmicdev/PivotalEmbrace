package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.document.Quotes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service interface for [Quote]s in Pivotal Embrace.
 *
 * @author fsmicdev
 */
public interface QuotesService {

    List<Quotes> getAllQuotes() throws ServiceException;

    Quotes getQuote(final Long id) throws ServiceException;

    List<Quotes> getQuotesByPerson(final String person) throws ServiceException;

    List<Quotes> getQuotesByQuotePattern(final String quotePattern) throws ServiceException;

    Map<String, AtomicInteger> getAllAuthorsAndAuthorQuoteCount() throws ServiceException;

    Quotes save(final String quote, final String author) throws ServiceException;

    Quotes update(final Long id, final String quote, final String author) throws ServiceException;

    boolean delete(final Quotes preExistingQuote) throws ServiceException;
}
