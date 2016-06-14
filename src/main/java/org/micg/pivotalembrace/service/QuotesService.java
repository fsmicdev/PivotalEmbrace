package org.micg.pivotalembrace.service;

import org.micg.pivotalembrace.model.Quotes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface QuotesService {

    List<Quotes> getAllQuotes() throws ServiceException;

    Quotes getQuote(final Long id) throws ServiceException;

    List<Quotes> getQuotesByPerson(final String person) throws ServiceException;

    List<Quotes> getQuotesByQuotePattern(final String quotePattern) throws ServiceException;

    Map<String, AtomicInteger> getAllAuthorsAndQuoteCount() throws ServiceException;

    Quotes save(final String quote, final String author) throws ServiceException;

    Quotes update(final Long id, final String quote, final String author) throws ServiceException;

    boolean delete(final Quotes preExistingQuote) throws ServiceException;
}
