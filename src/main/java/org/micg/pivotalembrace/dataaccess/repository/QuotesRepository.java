package org.micg.pivotalembrace.dataaccess.repository;

import org.micg.pivotalembrace.model.document.Quotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("quotesRepository")
public interface QuotesRepository extends MongoRepository<Quotes, Long> {

    List<Quotes> findAll();

    Quotes findOne(final Long id);

    List<Quotes> findByPerson(final String person);

    Quotes save(final Quotes quotes);

    void delete(final Long id);

}
