package org.micg.pivotalembrace.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "counters")
public class SequenceId {

    @Id
    private String _id;

    @Field(value = "sequence_value")
    private Long sequenceValue;

    public String getId() {
        return _id;
    }

    public void setId(final String _id) {
        this._id = _id;
    }

    public Long getSequenceValue() {
        return sequenceValue;
    }

    public void setSequenceValue(final Long sequenceValue) {
        this.sequenceValue = sequenceValue;
    }

    @Override
    public String toString() {
        return "SequenceId{" +
                "_id='" + _id + '\'' +
                ", sequenceValue=" + sequenceValue +
                '}';
    }
}
