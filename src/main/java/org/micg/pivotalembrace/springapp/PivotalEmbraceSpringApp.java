package org.micg.pivotalembrace.springapp;

import org.micg.pivotalembrace.model.document.Quotes;
import org.micg.pivotalembrace.service.QuotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 * @author fsmicdev
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.micg.pivotalembrace"})
@EnableMongoRepositories(basePackages = {"org.micg.pivotalembrace.dataaccess.repository"})
public class PivotalEmbraceSpringApp implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(PivotalEmbraceSpringApp.class);

    @Autowired
    private QuotesService quotesService;

    public static void main(String[] args) {
        SpringApplication.run(PivotalEmbraceSpringApp.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        log.info("\n");

        // ----------------------------
        // ----- Fetch all quotes -----
        // ----------------------------
        log.info("Quotes found with findAll():");
        log.info("---------------------------");
        for (final Quotes quote : quotesService.getAllQuotes()) {
            log.info(quote.toString());
        }

        log.info("\n");

        // ------------------------------------------------
        // ----- Fetch quotes by person (i.e. author) -----
        // ------------------------------------------------
        log.info("Quotes found with findByPerson('Albert Einstein'):");
        log.info("-------------------------------------------------");
        for (final Quotes quote : quotesService.getQuotesByPerson("Albert Einstein")) {
            log.info(quote.toString());
        }

        log.info("\n");

        // -----------------------------------------------
        // ----- Fetch quotes with a substring in it -----
        // -----------------------------------------------
        log.info("Quotes found with find having regex 'the':");
        log.info("-----------------------------------------");
        for (final Quotes quote : quotesService.getQuotesByQuotePattern("the")) {
            log.info(quote.toString());
        }

        log.info("\n");

        // ---------------------------------------------------------------
        // ----- Fetch all quote authors and respective quote counts -----
        // ---------------------------------------------------------------
        log.info("Quote authors and respective quote counts are:");
        log.info("---------------------------------------------");
        final Map<String, AtomicInteger> authorsMap = quotesService.getAllAuthorsAndAuthorQuoteCount();

        for (final String author : authorsMap.keySet()) {
            final AtomicInteger quoteCountForAuthor = authorsMap.get(author);

            final String suffix = (quoteCountForAuthor.intValue() > 1) ? "quotes" : "quote";

            log.info("Person [" + author + "] has [" + quoteCountForAuthor + "] " + suffix);
        }

        log.info("\n");
     }

}
