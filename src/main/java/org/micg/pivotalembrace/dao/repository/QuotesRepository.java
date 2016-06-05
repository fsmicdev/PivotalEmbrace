package org.micg.pivotalembrace.dao.repository;

import org.micg.pivotalembrace.model.Quotes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("quotesRepository")
public interface QuotesRepository extends MongoRepository<Quotes, Long> {

    List<Quotes> findAll();

    List<Quotes> findByPerson(final String person);

    Quotes save(final Quotes quotes);

}
