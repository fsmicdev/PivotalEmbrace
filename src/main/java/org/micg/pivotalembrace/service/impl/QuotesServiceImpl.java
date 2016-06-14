package org.micg.pivotalembrace.service.impl;

import org.micg.pivotalembrace.dataaccess.PersistenceException;
import org.micg.pivotalembrace.dataaccess.sequence.SequenceDao;
import org.micg.pivotalembrace.dataaccess.template.QuotesTemplate;
import org.micg.pivotalembrace.model.Quotes;
import org.micg.pivotalembrace.dataaccess.repository.QuotesRepository;
import org.micg.pivotalembrace.service.QuotesService;
import org.micg.pivotalembrace.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.micg.pivotalembrace.model.ErrorCode.INVALID_PARAMS;

@Service("quotesService")
public class QuotesServiceImpl implements QuotesService {

    private static final String QUOTES_SEQ_KEY = "quotesid";

    private Logger logger = LoggerFactory.getLogger(QuotesServiceImpl.class);

    @Autowired
    private QuotesRepository quotesRepository;

    @Autowired
    private QuotesTemplate quotesTemplate;

    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public List<Quotes> getAllQuotes() throws ServiceException {
        try {
            return quotesRepository.findAll();
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Quotes getQuote(final Long id) throws ServiceException {
        try {
            return quotesRepository.findOne(id);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Quotes> getQuotesByPerson(final String person) throws ServiceException {
        try {
            if (StringUtils.isEmpty(person)) {
                throw new ServiceException(INVALID_PARAMS);
            } else {
                return quotesRepository.findByPerson(person);
            }
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Quotes> getQuotesByQuotePattern(final String quotePattern) throws ServiceException {
        try {
            if (StringUtils.isEmpty(quotePattern)) {
                throw new ServiceException(INVALID_PARAMS);
            } else {
                return quotesTemplate.findByQuote(quotePattern);
            }
        } catch (final PersistenceException pe) {
            throw new ServiceException(pe);
        }
    }

    @Override
    public Map<String, AtomicInteger> getAllAuthorsAndQuoteCount() throws ServiceException {
        try {
            return quotesTemplate.findQuoteAuthorsAndQuoteCount();
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Quotes save(final String quoteText, final String author) throws ServiceException {
        final Quotes quote = new Quotes();

        try {
            final Long nextIdVal = sequenceDao.getNextSequenceId(QUOTES_SEQ_KEY);
            logger.info("##### nextIdVal: " + nextIdVal);

            quote.setId(nextIdVal);
            quote.setPerson(author);
            quote.setQuote(quoteText);

            return quotesRepository.save(quote);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Quotes update(final Long id, final String quoteText, final String author) throws ServiceException {
        final Quotes quote = new Quotes();

        quote.setId(id);
        quote.setQuote(quoteText);
        quote.setPerson(author);

        try {
            return quotesRepository.save(quote);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(final Quotes preExistingQuote) throws ServiceException {
        try {
            if (preExistingQuote == null) {
                return false;
            } else {
                quotesRepository.delete(preExistingQuote);

                return true;
            }
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }
}
