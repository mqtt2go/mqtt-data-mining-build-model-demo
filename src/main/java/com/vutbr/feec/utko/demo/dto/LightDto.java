package com.vutbr.feec.utko.demo.dto;

public class LightDto {
    private Long id;
    private String state;

    public LightDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
