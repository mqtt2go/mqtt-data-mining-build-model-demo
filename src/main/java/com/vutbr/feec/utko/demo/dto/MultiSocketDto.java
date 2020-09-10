package com.vutbr.feec.utko.demo.dto;

import java.math.BigDecimal;

public class MultiSocketDto {
    
    private String state;
    private BigDecimal temperature;
    private BigDecimal humidity;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }
}
