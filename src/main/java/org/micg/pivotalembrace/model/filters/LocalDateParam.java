package org.micg.pivotalembrace.model.filters;

import org.apache.commons.lang3.StringUtils;


import org.micg.pivotalembrace.util.DatesUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.micg.pivotalembrace.model.apirest.ErrorCode.INVALID_PARAMS;

/**
 *
 *
 * @author fsmicdev
 */
public class LocalDateParam {

    private static final Logger logger = LoggerFactory.getLogger(LocalDateParam.class);

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final LocalDate localDate;

    public LocalDateParam(final LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDateParam(final String dateStr) throws WebApplicationException {
        try {
            if (StringUtils.isBlank(dateStr)) {
                throw new ParseException("{dateStr} parameter must not be null", -1);
            }

            this.localDate = DatesUtility.asLocalDate(DATE_FORMAT.parse(dateStr));
        } catch (final ParseException pe) {
            logger.error("WebApplicationException {}: " +
                         "Could not parse date string [" + dateStr + "]: " + pe.getMessage(), pe);

            throw new WebApplicationException(Response.status(INVALID_PARAMS.getHttpStatusErrorCode())
                    .build());
        }
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return DatesUtility.asLocalDateTime(DatesUtility.asUitlDateFromLocalDate(localDate));
    }

}
