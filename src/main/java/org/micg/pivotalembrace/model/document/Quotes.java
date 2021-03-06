package org.micg.pivotalembrace.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model abstraction for [Quotes] in Pivotal Embrace.
 *
 * @author fsmicdev
 */
@Document(collection = "quotes")
public class Quotes {

    @Id
    private Long _id;

    private String quote;
    private String person;

    public Quotes() {
    }

    public Quotes(final String quote, final String person) {
        this.quote = quote;
        this.person = person;
    }

    @Override
    public String toString() {
        return String.format(
                "Quotes[id=%s, quote='%s', person='%s']",
                _id, quote, person);
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(final String person) {
        this.person = person;
    }

    public Long getId() {
        return _id;
    }

    public void setId(final Long _id) {
        this._id = _id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(final String quote) {
        this.quote = quote;
    }
}
