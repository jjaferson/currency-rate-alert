package org.josesilva.currencyRateAlert;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;


public class CurrencyRate {
    
    @JsonbDateFormat("dd-MM-yyyy HH:mm:ss")
    private Date date;

    private String base;

    private List<Currency> rates;

    CurrencyRate(Date date, String base, List<Currency> rates) {
        this.date = date;
        this.base = base;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public Date getDate() {
        return date;
    }

    public List<Currency> getRates() {
        return rates;
    }
    
    @JsonbCreator
    public static CurrencyRate of(String date, String base, List<Currency> rates){
        Timestamp timestamp = new Timestamp(Long.parseLong(date));
        Date newDate = new Date(timestamp.getTime());
        return new CurrencyRate(newDate, base, rates);
    }

    public static class Currency {

        private String name;

        private double value;

        Currency(String name, double value) {
            this.name = name;
            this.value = value;
        }

        @JsonbCreator
        public static Currency of(String name, String value){
            return new Currency(name, Double.parseDouble(value.replace(",", ".")));
        }

        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }
    }
}