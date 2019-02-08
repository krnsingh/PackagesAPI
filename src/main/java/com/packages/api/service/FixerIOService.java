package com.packages.api.service;

import com.packages.api.dto.Rate;
import com.packages.api.exception.InvalidCurrencyException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FixerIOService {

    public double convertToCurrency(final String currency, final double usdAmount) {
        final var allRateBaseEuro = getAllRateBaseEuro();
        if (isValid(currency, allRateBaseEuro)) {
            double eurToUsd = getRateByCurrency("USD", allRateBaseEuro);
            double eurToRequiredCurrency = getRateByCurrency(currency, allRateBaseEuro);
            return (1 / eurToUsd) * usdAmount * eurToRequiredCurrency;
        } else {
            throw new InvalidCurrencyException();
        }
    }

    private boolean isValid(final String currency, final Rate rates) {
        return rates.getRates().containsKey(currency);
    }

    private Double getRateByCurrency(final String currency, final Rate rates) {
        return rates.getRates().get(currency);
    }

    private Rate getAllRateBaseEuro() {
        final var restTemplate = new RestTemplate();
        final var response = restTemplate.exchange("http://data.fixer.io/api/latest?access_key=77587145a37a8ec5419b393b5bc70bd3",
                HttpMethod.GET, null, Rate.class);
        return response.getBody();
    }

}
