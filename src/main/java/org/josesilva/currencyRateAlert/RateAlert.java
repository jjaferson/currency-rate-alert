package org.josesilva.currencyRateAlert;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class RateAlert extends PanacheEntity{
    
    public double threshold;
    
    public String email;
}






