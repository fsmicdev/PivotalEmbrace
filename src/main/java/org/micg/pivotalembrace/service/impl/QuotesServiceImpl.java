package org.micg.pivotalembrace.service.impl;

import org.micg.pivotalembrace.dao.PersistenceException;
import org.micg.pivotalembrace.dao.sequence.SequenceDao;
import org.micg.pivotalembrace.dao.template.QuotesTemplate;
import org.micg.pivotalembrace.model.Quotes;
import org.micg.pivotalembrace.dao.repository.QuotesRepository;
import org.micg.pivotalembrace.service.QuotesService;
import org.micg.pivotalembrace.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.micg.pivotalembrace.model.ErrorCode.INVALID_PARAMS;

@Service("quotesService")
public class QuotesServiceImpl implements QuotesService {

    private Logger logger = LoggerFactory.getLogger(QuotesServiceImpl.class);

    private static final String QUOTES_SEQ_KEY = "quotesid";

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
    public Quotes save(final String quoteText, final String author) {
        final Long nextIdVal = sequenceDao.getNextSequenceId(QUOTES_SEQ_KEY);

        logger.info("##### nextIdVal: " + nextIdVal);

        final Quotes quote = new Quotes();
        quote.setPerson(author);
        quote.setQuote(quoteText);
        quote.setId(nextIdVal);

        return quotesRepository.save(quote);
    }
}
