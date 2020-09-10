package com.vutbr.feec.utko.demo.repositories;

import com.vutbr.feec.utko.demo.dto.MultiSocketDto;
import com.vutbr.feec.utko.demo.dto.MultiSocketModelCreateDto;
import com.vutbr.feec.utko.demo.dto.MultiSocketNormalizationDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class MultiSocketRepository {

    private JdbcTemplate jdbcTemplate;

    public MultiSocketRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MultiSocketNormalizationDto getMultiSocketMaxMinValues() {
        return jdbcTemplate.queryForObject("SELECT MAX(m.temperature) max_temperature, MIN(m.temperature) min_temperature , MAX(m.humidity) max_humidity, MIN(m.humidity) min_humidity FROM multi_sensor m ",
                new RowMapper<MultiSocketNormalizationDto>() {
                    @Override
                    public MultiSocketNormalizationDto mapRow(ResultSet resultSet, int i) throws SQLException {
                        MultiSocketNormalizationDto lightDto = new MultiSocketNormalizationDto();
                        lightDto.setTemperatureMax(resultSet.getBigDecimal("max_temperature"));
                        lightDto.setTemperatureMin(resultSet.getBigDecimal("min_temperature"));
                        lightDto.setHumidityMax(resultSet.getBigDecimal("max_humidity"));
                        lightDto.setHumidityMin(resultSet.getBigDecimal("min_humidity"));
                        return lightDto;
                    }
                });
    }

    public List<MultiSocketDto> getRecordsFromSpecificTimeRange(LocalTime startTime, LocalTime endTime) {
        return jdbcTemplate.query("SELECT m.state, m.temperature, m.humidity FROM multi_sensor m WHERE TIME(m.record_timestamp) > ? AND TIME(m.record_timestamp) < ?",
                new Object[]{startTime, endTime},
                new RowMapper<MultiSocketDto>() {
                    @Override
                    public MultiSocketDto mapRow(ResultSet resultSet, int i) throws SQLException {
                        MultiSocketDto multiSocketDto = new MultiSocketDto();
                        multiSocketDto.setHumidity(resultSet.getBigDecimal("humidity"));
                        multiSocketDto.setTemperature(resultSet.getBigDecimal("temperature"));
                        multiSocketDto.setState(resultSet.getString("state"));
                        return multiSocketDto;
                    }
                }
        );
    }

    public void saveTimeSeries(MultiSocketModelCreateDto multiSocketTimeSeries) {
        jdbcTemplate.update("INSERT INTO multi_sensor_model (min_val, max_val, min_percentage, max_percentage, timeseries_from_timestamp, timeseries_to_timestamp, is_common_model, in_home, group_id, device_id, home_id, gateway_id) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                multiSocketTimeSeries.getMinVal(),
                multiSocketTimeSeries.getMaxVal(),
                multiSocketTimeSeries.getMinPercentage(),
                multiSocketTimeSeries.getMaxPercentage(),
                multiSocketTimeSeries.getTimeseriesFromTimestmap(),
                multiSocketTimeSeries.getTimeseriesToTimestamp(),
                multiSocketTimeSeries.isCommonModel(),
                multiSocketTimeSeries.isInHome(),
                multiSocketTimeSeries.getGroupId(),
                multiSocketTimeSeries.getDeviceId(),
                multiSocketTimeSeries.getHomeId(),
                multiSocketTimeSeries.getGatewayId());
    }

    public void deleteByCommonModel() {
        jdbcTemplate.update("DELETE FROM multi_sensor_model WHERE is_common_model = ?", true);
    }
}
