package org.micg.pivotalembrace.restweb;

import io.swagger.annotations.*;
import org.micg.pivotalembrace.model.ErrorRespBody;
import org.micg.pivotalembrace.model.Quotes;
import org.micg.pivotalembrace.service.QuotesService;
import org.micg.pivotalembrace.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("quotes")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "quotes",
     description = "API to get and modify Quotes related information.")
public class QuotesAPI {

    @Autowired
    private QuotesService quotesService;

    @Autowired
    private CacheControl cacheControl;

    @GET
    @ApiOperation("Get all Quotes.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned all Quotes."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getAllQuotes() {
        try {
            return Response.ok(quotesService.getAllQuotes()).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @GET
    @Path("/quote/{id}")
    @ApiOperation("Get a Quote by its unique id.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned Quote via unique id."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getQuote(@ApiParam(value = "Unique id of Quote.", required = true)
                             @QueryParam("id") final Long id) {
        try {
            return Response.ok(quotesService.getQuote(id)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @GET
    @Path("/person/quotecount")
    @ApiOperation("Get all authors and authors Quote count.  [N.B. This statistical/summary data " +
                  "will be client-cached for 1 hour].")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned matching Quotes."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getAllAuthorsAndAuthorQuoteCount() {
        try {
            return Response.ok(quotesService.getAllAuthorsAndQuoteCount()).cacheControl(cacheControl).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @GET
    @Path("/person")
    @ApiOperation("Get all Quotes authored by said Quote person.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned matching Quotes."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getAllQuotesByPerson(@ApiParam(value = "Author of Quote.", required = true)
                                         @QueryParam("quotePerson") final String quotePerson) {
        try {
            return Response.ok(quotesService.getQuotesByPerson(quotePerson)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @GET
    @Path("/part")
    @ApiOperation("Get all Quotes with a sub-part/fragment/pattern of said Quote sub-part (regular expressions supported).")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lookup succeeded. Returned matching Quotes."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response getAllQuotesWithQuoteSubpart(@ApiParam(value = "Quote pattern (regular expressions supported).", required = true)
                                                 @QueryParam("quoteSubpart") final String quoteSubpart) {
        try {
            return Response.ok(quotesService.getQuotesByQuotePattern(quoteSubpart)).build();
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @POST
    @Path("/quote")
    @ApiOperation("Create and save a new Quote into Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New Quote was created."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response saveNewQuote(@ApiParam(value = "Quoted person (i.e. author).", required = true)
                                 @QueryParam("quotedPerson") final String quotedPerson,
                                 @ApiParam(value = "Quote text.", required = true)
                                 @QueryParam("quoteText") final String quoteText) {
        try {
            final Quotes savedQuote = quotesService.save(quoteText, quotedPerson);

            if ((savedQuote != null) && (savedQuote.getId() != null)) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @PUT
    @Path("/quote/{id}")
    @ApiOperation("Update an existing Quote in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Quote was updated."),
            @ApiResponse(code = 404, message = "The specific Quote was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response updateQuote(
            @ApiParam(value = "Existing id of Quote to update.", required = true)
            @PathParam(value = "id") final Long id,
            @ApiParam(value = "Quoted person (i.e. author).", required = true)
            @QueryParam("quotedPerson") final String quotedPerson,
            @ApiParam(value = "Quote text.", required = true)
            @QueryParam("quoteText") final String quoteText) {
        try {
            if (quotesService.getQuote(id) == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                final Quotes savedQuote =
                        quotesService.update(id, quoteText, quotedPerson);

                if ((savedQuote != null) && (savedQuote.getId() != null)) {
                    return Response.status(Response.Status.OK).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }

    @DELETE
    @Path("/quote/{id}")
    @ApiOperation("Delete an existing Quote in Pivotal Embrace.")
    @Produces((MediaType.APPLICATION_JSON))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Quote was deleted."),
            @ApiResponse(code = 404, message = "The specific Quote was not found."),
            @ApiResponse(code = 500, message = "Unexpected Server Error.", response = ErrorRespBody.class)
    })
    public Response deleteQuote(
            @ApiParam(value = "Existing id of Quote to delete.", required = true)
            @PathParam(value = "id") final Long id) {
        try {
            final Quotes preExistingQuote = quotesService.getQuote(id);

            if (preExistingQuote == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                if (quotesService.delete(preExistingQuote)) {
                    return Response.status(Response.Status.OK).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
        } catch (final ServiceException se) {
            return Response.status(se.getErrorCode().getHttpStatusErrorCode()).build();
        }
    }
}
