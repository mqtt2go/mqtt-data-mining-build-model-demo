package com.vutbr.feec.utko.demo.utils;

public enum SensorsState {
    ON("on"), OFF("off"), OPEN("open"), CLOSED("closed");

    private String value;

    SensorsState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
