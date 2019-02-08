package com.packages.api.dto;

import java.util.HashMap;
import java.util.Map;

public class Rate {

    private Map<String, Double> rates = new HashMap<>(100);

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
