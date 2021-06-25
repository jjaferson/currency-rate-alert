package org.josesilva.currencyRateAlert;

import java.time.Duration;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.SseElementType;

import io.smallrye.mutiny.Multi;

@Path("/")
public class CurrencyRateAlert {

    @Inject
    CurrencyRateService currencyRateService;

    @GET
    @Path("rates")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Multi<CurrencyRate> listRate() {
        return currencyRateService.getRates();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("alerts")
    public List<RateAlert> listAlerts(){
        return RateAlert.listAll();
    }

    @POST
    @Path("alerts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createAlert(RateAlert alert){
        System.out.println(alert.toString());
        alert.id = null;
        alert.persist();


        return Response.status(Status.CREATED)
            .entity(alert)
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rate-with-threshold")
    public Response listRateWithThreshold(){
        CurrencyRate cr = currencyRateService.getBytreshold();
        if (cr == null){
            return Response.status(Status.OK).entity("entity").build();
        }
        return Response.status(Status.OK)
        .entity(cr)
        .build();
    }   
}