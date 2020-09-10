package com.vutbr.feec.utko.demo.repositories;

import com.vutbr.feec.utko.demo.dto.LightDto;
import com.vutbr.feec.utko.demo.dto.LightModelCreateDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class LightRepository {

    private JdbcTemplate jdbcTemplate;

    public LightRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LightDto> getRecordsFromSpecificTimeRange(LocalTime startTime, LocalTime endTime) {
        return jdbcTemplate.query("SELECT * FROM light l WHERE TIME(l.record_timestamp) > ? AND TIME(l.record_timestamp) < ?",
                new Object[]{startTime, endTime},
                new RowMapper<LightDto>() {
                    @Override
                    public LightDto mapRow(ResultSet resultSet, int i) throws SQLException {
                        LightDto lightDto = new LightDto();
                        lightDto.setId(resultSet.getLong("id"));
                        lightDto.setState(resultSet.getString("state"));
                        return lightDto;
                    }
                });
    }

    public void saveTimeSeries(LightModelCreateDto lightModelCreateDto) {
        jdbcTemplate.update("INSERT INTO light_model (min_val, max_val, min_percentage, max_percentage, timeseries_from_timestamp, timeseries_to_timestamp, is_common_model, in_home, group_id, device_id, home_id, gateway_id) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                lightModelCreateDto.getMinVal(),
                lightModelCreateDto.getMaxVal(),
                lightModelCreateDto.getMinPercentage(),
                lightModelCreateDto.getMaxPercentage(),
                lightModelCreateDto.getTimeseriesFromTimestmap(),
                lightModelCreateDto.getTimeseriesToTimestamp(),
                lightModelCreateDto.isCommonModel(),
                lightModelCreateDto.isInHome(),
                lightModelCreateDto.getGroupId(),
                lightModelCreateDto.getDeviceId(),
                lightModelCreateDto.getHomeId(),
                lightModelCreateDto.getGatewayId());
    }


    public void deleteByCommonModel() {
        jdbcTemplate.update("DELETE FROM light_model WHERE is_common_model = ?", true);
    }

}
