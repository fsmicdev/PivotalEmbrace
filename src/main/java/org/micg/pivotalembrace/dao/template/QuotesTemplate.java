package org.micg.pivotalembrace.dao.template;

import org.micg.pivotalembrace.dao.PersistenceException;
import org.micg.pivotalembrace.model.Quotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class QuotesTemplate {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Quotes> findByQuote(final String quotePattern) throws PersistenceException {
        final Query query = new Query();
        query.limit(10);
        query.addCriteria(Criteria.where("quote").regex(quotePattern));

        return mongoTemplate.find(query, Quotes.class);
    }

    public Map<String, AtomicInteger> findQuoteAuthorsAndQuoteCount() throws PersistenceException {
        final Query query = new Query();
        query.fields().include("person");

        final List<Quotes> quotes =  mongoTemplate.find(query, Quotes.class);

        final ConcurrentMap<String, AtomicInteger> authorsMap = new ConcurrentSkipListMap<String, AtomicInteger>();

        for (final Quotes quote : quotes) {
            final String authorName = quote.getPerson();

            authorsMap.putIfAbsent(authorName, new AtomicInteger(0));
            authorsMap.get(authorName).incrementAndGet();
        }

        return authorsMap;
    }
}
