package com.webdesign.countryservice.model;

public class Country {
    private final String name;
    private final String capital;
    private String iso2;
    private Double population;
    private Double popGrowth;
    private String currencyCode;
    private String currencyName;

    public Country(String name, String capital, String iso2, Double population, Double popGrowth, String currencyCode,  String currencyName) {
        this.name = name;
        this.capital = capital;
        this.iso2 = iso2;
        this.population = population;
        this.popGrowth = popGrowth;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }
}
