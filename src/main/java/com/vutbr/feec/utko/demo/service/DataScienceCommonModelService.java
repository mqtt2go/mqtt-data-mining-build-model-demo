package com.vutbr.feec.utko.demo.service;

import com.vutbr.feec.utko.demo.dto.*;
import com.vutbr.feec.utko.demo.repositories.CameraRepository;
import com.vutbr.feec.utko.demo.repositories.LightRepository;
import com.vutbr.feec.utko.demo.repositories.MultiSocketRepository;
import com.vutbr.feec.utko.demo.repositories.SocketRepository;
import com.vutbr.feec.utko.demo.utils.SensorsState;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataScienceCommonModelService {

    private static final int SECONDS_IN_DAY = 1444;

    private static final Double LOWER_PERCENTILE_FOR_TIMESERIES = 5.0;
    private static final Double UPPER_PERCENTILE_FOR_TIMESERIES = 95.0;
    private static final String GATEWAY_ID = "BUT";
    private static final String HOME_ID = "BRQ";
    private static final String DEVICE_ID_MULTISENSOR = "th16";
    private static final String DEVICE_ID_CAMERA = "camera1";
    private static final String DEVICE_ID_LIGHT = "sb1";
    private static final String GROUP_ID = null;

    private FindRangePercentilesService findRangePercentilesService;
    // repositories
    private CameraRepository cameraRepository;
    private LightRepository lightRepository;
    private SocketRepository socketRepository;
    private MultiSocketRepository multiSocketRepository;

    public DataScienceCommonModelService(
            CameraRepository cameraRepository,
            FindRangePercentilesService findRangePercentilesService,
            LightRepository lightRepository,
            SocketRepository socketRepository,
            MultiSocketRepository multiSocketRepository) {
        this.findRangePercentilesService = findRangePercentilesService;
        this.cameraRepository = cameraRepository;
        this.lightRepository = lightRepository;
        this.socketRepository = socketRepository;
        this.multiSocketRepository = multiSocketRepository;
    }

    public void retrainModel(int timeSeriesLight, int timeSeriesMultiSensor, int timeSeriesCamera) {
        createTimeSeriesLight(timeSeriesLight);
        createTimeSeriesMultiSensor(timeSeriesMultiSensor);
        createTimeSeriesCamera(timeSeriesCamera);
    }

    public void createTimeSeriesMultiSensor(int minutesRange) {
        multiSocketRepository.deleteByCommonModel();
        MultiSocketNormalizationDto multiSensorForNormalization = multiSocketRepository.getMultiSocketMaxMinValues();
        LocalTime startTime = LocalTime.of(0, 0, 0);
        LocalTime endTime = startTime.plusMinutes(minutesRange);

        int numberOfIterations = SECONDS_IN_DAY / minutesRange;
        for (int i = 0; i < numberOfIterations; i++) {
            List<MultiSocketDto> multiSocketDto = multiSocketRepository.getRecordsFromSpecificTimeRange(startTime, endTime);
            if (multiSocketDto != null && !multiSocketDto.isEmpty()) {
                List<Double> normalizedAttributesSum = new ArrayList<>();
                multiSocketDto.forEach(m -> {
                    int stateValueInInt = 0;
                    if (m.getState() != null && m.getState().equals(SensorsState.ON.getValue())) {
                        stateValueInInt = 1;
                    } else {
                        stateValueInInt = 0;
                    }
                    double normalizedState = normalizeValue(
                            Double.valueOf(1.0),
                            Double.valueOf(0.0),
                            stateValueInInt);
                    double normalizedTemperature = normalizeValue(
                            multiSensorForNormalization.getTemperatureMax().doubleValue(),
                            multiSensorForNormalization.getTemperatureMin().doubleValue(),
                            m.getTemperature().doubleValue());
                    double normalizedHumidity = normalizeValue(
                            multiSensorForNormalization.getHumidityMax().doubleValue(),
                            multiSensorForNormalization.getHumidityMin().doubleValue(),
                            m.getHumidity().doubleValue());
                    normalizedAttributesSum.add(normalizedState + normalizedTemperature + normalizedHumidity);
                });
                double[] rangePercentileValuesState = findRangePercentilesService.findValueWithPercentileOne(LOWER_PERCENTILE_FOR_TIMESERIES.intValue(), UPPER_PERCENTILE_FOR_TIMESERIES.intValue(), normalizedAttributesSum);

                MultiSocketModelCreateDto multiSocketTimeSeries = new MultiSocketModelCreateDto();
                multiSocketTimeSeries.setCommonModel(true);
                multiSocketTimeSeries.setInHome(true);
                multiSocketTimeSeries.setMaxPercentage(UPPER_PERCENTILE_FOR_TIMESERIES);
                multiSocketTimeSeries.setMinPercentage(LOWER_PERCENTILE_FOR_TIMESERIES);
                multiSocketTimeSeries.setMinVal(rangePercentileValuesState[0]);
                multiSocketTimeSeries.setMaxVal(rangePercentileValuesState[1]);
                multiSocketTimeSeries.setTimeseriesFromTimestmap(startTime);
                multiSocketTimeSeries.setTimeseriesToTimestamp(endTime);
                multiSocketTimeSeries.setDeviceId(DEVICE_ID_MULTISENSOR);
                multiSocketTimeSeries.setGatewayId(GATEWAY_ID);
                multiSocketTimeSeries.setGroupId(GROUP_ID);
                multiSocketTimeSeries.setHomeId(HOME_ID);
                multiSocketRepository.saveTimeSeries(multiSocketTimeSeries);
            } else {
                MultiSocketModelCreateDto multiSocketTimeSeries = new MultiSocketModelCreateDto();
                multiSocketTimeSeries.setCommonModel(true);
                multiSocketTimeSeries.setInHome(true);
                multiSocketTimeSeries.setMaxPercentage(UPPER_PERCENTILE_FOR_TIMESERIES);
                multiSocketTimeSeries.setMinPercentage(LOWER_PERCENTILE_FOR_TIMESERIES);
                multiSocketTimeSeries.setMinVal(-1.0);
                multiSocketTimeSeries.setMaxVal(-1.0);
                multiSocketTimeSeries.setTimeseriesFromTimestmap(startTime);
                multiSocketTimeSeries.setTimeseriesToTimestamp(endTime);
                multiSocketTimeSeries.setDeviceId(DEVICE_ID_MULTISENSOR);
                multiSocketTimeSeries.setGatewayId(GATEWAY_ID);
                multiSocketTimeSeries.setGroupId(GROUP_ID);
                multiSocketTimeSeries.setHomeId(HOME_ID);
                multiSocketRepository.saveTimeSeries(multiSocketTimeSeries);
            }
            startTime = startTime.plusMinutes(minutesRange);
            endTime = endTime.plusMinutes(minutesRange);
        }
    }

    public void createTimeSeriesLight(int minutesRange) {
        lightRepository.deleteByCommonModel();
        LocalTime startTime = LocalTime.of(0, 0, 0);
        LocalTime endTime = startTime.plusMinutes(minutesRange);

        int numberOfIterations = SECONDS_IN_DAY / minutesRange;

        for (int i = 0; i < numberOfIterations; i++) {
            List<LightDto> lightsDb = lightRepository.getRecordsFromSpecificTimeRange(startTime, endTime);
            if (lightsDb != null && !lightsDb.isEmpty()) {
                // state zone
                if (lightsDb.
                        stream()
                        .anyMatch(l -> l.getState().equals(SensorsState.ON.getValue()))) {
                    LightModelCreateDto lightModelCreateDto = new LightModelCreateDto();
                    lightModelCreateDto.setCommonModel(true);
                    lightModelCreateDto.setInHome(true);
                    lightModelCreateDto.setMaxPercentage(UPPER_PERCENTILE_FOR_TIMESERIES);
                    lightModelCreateDto.setMinPercentage(LOWER_PERCENTILE_FOR_TIMESERIES);
                    lightModelCreateDto.setMinVal(0.0);
                    lightModelCreateDto.setMaxVal(1.0);
                    lightModelCreateDto.setTimeseriesFromTimestmap(startTime);
                    lightModelCreateDto.setTimeseriesToTimestamp(endTime);
                    lightModelCreateDto.setDeviceId(DEVICE_ID_LIGHT);
                    lightModelCreateDto.setGatewayId(GATEWAY_ID);
                    lightModelCreateDto.setGroupId(GROUP_ID);
                    lightModelCreateDto.setHomeId(HOME_ID);
                    lightRepository.saveTimeSeries(lightModelCreateDto);
                } else {
                    saveLightRangeWithoutActivity(startTime, endTime);
                }
            } else {
                saveLightRangeWithoutActivity(startTime, endTime);
            }
            startTime = startTime.plusMinutes(minutesRange);
            endTime = endTime.plusMinutes(minutesRange);
        }
    }

    private void saveLightRangeWithoutActivity(LocalTime startTime, LocalTime endTime) {
        LightModelCreateDto lightModelCreateDto = new LightModelCreateDto();
        lightModelCreateDto.setCommonModel(true);
        lightModelCreateDto.setInHome(true);
        lightModelCreateDto.setMaxPercentage(UPPER_PERCENTILE_FOR_TIMESERIES);
        lightModelCreateDto.setMinPercentage(LOWER_PERCENTILE_FOR_TIMESERIES);
        lightModelCreateDto.setMinVal(0.0);
        lightModelCreateDto.setMaxVal(0.0);
        lightModelCreateDto.setTimeseriesFromTimestmap(startTime);
        lightModelCreateDto.setTimeseriesToTimestamp(endTime);
        lightModelCreateDto.setDeviceId(DEVICE_ID_LIGHT);
        lightModelCreateDto.setGatewayId(GATEWAY_ID);
        lightModelCreateDto.setGroupId(GROUP_ID);
        lightModelCreateDto.setHomeId(HOME_ID);
        lightRepository.saveTimeSeries(lightModelCreateDto);
    }

    private void createTimeSeriesCamera(int minutesRange) {
        cameraRepository.deleteByCommonModel();
        LocalTime startTime = LocalTime.of(0, 0, 0);
        LocalTime endTime = startTime.plusMinutes(minutesRange);

        int numberOfIterations = SECONDS_IN_DAY / minutesRange;

        for (int i = 0; i < numberOfIterations; i++) {
            List<CameraDto> cameraDtos = cameraRepository.getRecordsFromSpecificTimeRange(startTime, endTime);
            if (cameraDtos != null && !cameraDtos.isEmpty()) {
                // state zone
                if (cameraDtos.
                        stream()
                        .anyMatch(c -> c.getState().equals(SensorsState.ON.getValue()))) {
                    CameraModelCreateDto cameraModelCreateDto = new CameraModelCreateDto();
                    cameraModelCreateDto.setCommonModel(true);
                    cameraModelCreateDto.setInHome(true);
                    cameraModelCreateDto.setMaxPercentage(UPPER_PERCENTILE_FOR_TIMESERIES);
                    cameraModelCreateDto.setMinPercentage(LOWER_PERCENTILE_FOR_TIMESERIES);
                    cameraModelCreateDto.setMinVal(0.0);
                    cameraModelCreateDto.setMaxVal(1.0);
                    cameraModelCreateDto.setTimeseriesFromTimestmap(startTime);
                    cameraModelCreateDto.setTimeseriesToTimestamp(endTime);
                    cameraModelCreateDto.setDeviceId(DEVICE_ID_CAMERA);
                    cameraModelCreateDto.setGatewayId(GATEWAY_ID);
                    cameraModelCreateDto.setGroupId(GROUP_ID);
                    cameraModelCreateDto.setHomeId(HOME_ID);
                    cameraRepository.saveTimeSeries(cameraModelCreateDto);
                } else {
                    saveCameraWithoutActivity(startTime, endTime);
                }
            } else {
                saveCameraWithoutActivity(startTime, endTime);
            }
            startTime = startTime.plusMinutes(minutesRange);
            endTime = endTime.plusMinutes(minutesRange);
        }
    }

    private void saveCameraWithoutActivity(LocalTime startTime, LocalTime endTime) {
        CameraModelCreateDto cameraModelCreateDto = new CameraModelCreateDto();
        cameraModelCreateDto.setCommonModel(true);
        cameraModelCreateDto.setInHome(true);
        cameraModelCreateDto.setMaxPercentage(UPPER_PERCENTILE_FOR_TIMESERIES);
        cameraModelCreateDto.setMinPercentage(LOWER_PERCENTILE_FOR_TIMESERIES);
        cameraModelCreateDto.setMinVal(0.0);
        cameraModelCreateDto.setMaxVal(0.0);
        cameraModelCreateDto.setTimeseriesFromTimestmap(startTime);
        cameraModelCreateDto.setTimeseriesToTimestamp(endTime);
        cameraModelCreateDto.setDeviceId(DEVICE_ID_CAMERA);
        cameraModelCreateDto.setGatewayId(GATEWAY_ID);
        cameraModelCreateDto.setGroupId(GROUP_ID);
        cameraModelCreateDto.setHomeId(HOME_ID);
        cameraRepository.saveTimeSeries(cameraModelCreateDto);
    }


    public double normalizeValue(double max, double min, double valueToNormalize) {
        return (valueToNormalize - min) / (max - min);
    }


}
