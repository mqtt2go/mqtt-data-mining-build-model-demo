package com.vutbr.feec.utko.demo.dto;

import java.math.BigDecimal;

public class MultiSocketNormalizationDto {

    private BigDecimal temperatureMax;
    private BigDecimal temperatureMin;
    private BigDecimal humidityMax;
    private BigDecimal humidityMin;

    public BigDecimal getHumidityMin() {
        return humidityMin;
    }

    public void setHumidityMin(BigDecimal humidityMin) {
        this.humidityMin = humidityMin;
    }

    public BigDecimal getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(BigDecimal temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public BigDecimal getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(BigDecimal temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public BigDecimal getHumidityMax() {
        return humidityMax;
    }

    public void setHumidityMax(BigDecimal humidityMax) {
        this.humidityMax = humidityMax;
    }
}
