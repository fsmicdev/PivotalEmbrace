package org.micg.pivotalembrace.dataaccess.sequence;

import org.micg.pivotalembrace.exception.SequenceException;
import org.micg.pivotalembrace.model.SequenceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceDaoImpl implements SequenceDao {

    private Logger logger = LoggerFactory.getLogger(SequenceDaoImpl.class);

    private static final String FIELD_ID = "_id";
    private static final String FIELD_SEQUENCE_VALUE = "sequence_value";

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public Long getNextSequenceId(final String sequenceKeyName) throws SequenceException {
        // Get sequence id.
        final Query query = new Query(Criteria.where(FIELD_ID).is(sequenceKeyName));

        // Increase sequence id by 1.
        final Update update = new Update();
        update.inc(FIELD_SEQUENCE_VALUE, 1);

        // Return new increased id.
        final FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        // This is where the real-action takes place.
        final SequenceId seqId =
                mongoOperation.findAndModify(query, update, options, SequenceId.class);


        logger.info("##### Attained seqId: " + seqId.toString());

        // If no id, throws SequenceException.
        // Optional; just a way to tell user when the sequence id is failed to generate.
        if (seqId == null) {
            throw new SequenceException("Unable to get sequence id for key: " + sequenceKeyName);
        }

        return seqId.getSequenceValue();
    }
}
