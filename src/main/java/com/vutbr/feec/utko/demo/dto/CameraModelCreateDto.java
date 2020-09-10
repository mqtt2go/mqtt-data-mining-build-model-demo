package com.vutbr.feec.utko.demo.dto;

import java.time.LocalTime;

public class CameraModelCreateDto {

    private Double minVal;
    private Double maxVal;
    private Double minPercentage;
    private Double maxPercentage;
    private LocalTime timeseriesFromTimestmap;
    private LocalTime timeseriesToTimestamp;
    private boolean isCommonModel;
    private boolean inHome;
    private String groupId;
    private String deviceId;
    private String homeId;
    private String gatewayId;

    public Double getMinVal() {
        return minVal;
    }

    public void setMinVal(Double minVal) {
        this.minVal = minVal;
    }

    public Double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(Double maxVal) {
        this.maxVal = maxVal;
    }

    public Double getMinPercentage() {
        return minPercentage;
    }

    public void setMinPercentage(Double minPercentage) {
        this.minPercentage = minPercentage;
    }

    public Double getMaxPercentage() {
        return maxPercentage;
    }

    public void setMaxPercentage(Double maxPercentage) {
        this.maxPercentage = maxPercentage;
    }

    public LocalTime getTimeseriesFromTimestmap() {
        return timeseriesFromTimestmap;
    }

    public void setTimeseriesFromTimestmap(LocalTime timeseriesFromTimestmap) {
        this.timeseriesFromTimestmap = timeseriesFromTimestmap;
    }

    public LocalTime getTimeseriesToTimestamp() {
        return timeseriesToTimestamp;
    }

    public void setTimeseriesToTimestamp(LocalTime timeseriesToTimestamp) {
        this.timeseriesToTimestamp = timeseriesToTimestamp;
    }

    public boolean isCommonModel() {
        return isCommonModel;
    }

    public void setCommonModel(boolean commonModel) {
        isCommonModel = commonModel;
    }

    public boolean isInHome() {
        return inHome;
    }

    public void setInHome(boolean inHome) {
        this.inHome = inHome;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }
}
