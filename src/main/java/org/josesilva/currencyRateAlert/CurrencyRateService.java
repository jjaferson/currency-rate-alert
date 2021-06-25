package org.josesilva.currencyRateAlert;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.josesilva.currencyRateAlert.CurrencyRate.Currency;

import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class CurrencyRateService {
    
    @RestClient
    CurrencyRateAPIService currencyRateService;

    private Multi<CurrencyRate> rates;
    
    CurrencyRateService() {
        this.rates = Multi.createFrom().ticks()
        .every(Duration.ofMinutes(1))
        .onItem()
        .transform(n -> currencyRateService.getCurrencyRate());
    }

    public Multi<CurrencyRate> getRates() {
        this.setAlert();
        return this.rates;
    }

    public CurrencyRate getBytreshold() {
        double threshold = 6.10;
        CurrencyRate c = currencyRateService.getCurrencyRate();      
        Currency cc = c.getRates().stream()
            .filter(rate -> rate.getValue() > threshold)
            .findAny()
            .orElse(null);
        if (cc == null) {
            return null;
        }
        return c;
    }

    public boolean filterByThreshold(CurrencyRate cr, double threshold){
        Currency cc = cr.getRates().stream()
        .filter(rate -> rate.getValue() > threshold)
        .findAny()
        .orElse(null);
        return cc != null;
    }

    public void setAlert() {
        double threshold = 6.10;

        this.rates
            .transform()
            .byFilteringItemsWith(item -> filterByThreshold(item, threshold))
            .subscribe().with(
                item -> System.out.println("Got " + item.getRates().get(0).getValue()),
                failure -> System.out.println("Got a failure: " + failure),
                () -> System.out.println("Got the completion event")
            );

    }
}