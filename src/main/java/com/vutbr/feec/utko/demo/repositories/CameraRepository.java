package com.vutbr.feec.utko.demo.repositories;

import com.vutbr.feec.utko.demo.dto.CameraDto;
import com.vutbr.feec.utko.demo.dto.CameraModelCreateDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class CameraRepository {

    private JdbcTemplate jdbcTemplate;

    public CameraRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CameraDto> getRecordsFromSpecificTimeRange(LocalTime startTime, LocalTime endTime) {
        return jdbcTemplate.query("SELECT * FROM camera m WHERE TIME(m.record_timestamp) > ? AND TIME(m.record_timestamp) < ?",
                new Object[]{startTime, endTime},
                new RowMapper<CameraDto>() {
                    @Override
                    public CameraDto mapRow(ResultSet resultSet, int i) throws SQLException {
                        CameraDto cameraDto = new CameraDto();
                        cameraDto.setId(resultSet.getLong("id"));
                        cameraDto.setState(resultSet.getString("state"));
                        return cameraDto;
                    }
                });
    }

    public void saveTimeSeries(CameraModelCreateDto cameraModelCreateDto) {
        jdbcTemplate.update("INSERT INTO camera_model (min_val, max_val, min_percentage, max_percentage, timeseries_from_timestamp, timeseries_to_timestamp, is_common_model, in_home, group_id, device_id, home_id, gateway_id) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                cameraModelCreateDto.getMinVal(),
                cameraModelCreateDto.getMaxVal(),
                cameraModelCreateDto.getMinPercentage(),
                cameraModelCreateDto.getMaxPercentage(),
                cameraModelCreateDto.getTimeseriesFromTimestmap(),
                cameraModelCreateDto.getTimeseriesToTimestamp(),
                cameraModelCreateDto.isCommonModel(),
                cameraModelCreateDto.isInHome(),
                cameraModelCreateDto.getGroupId(),
                cameraModelCreateDto.getDeviceId(),
                cameraModelCreateDto.getHomeId(),
                cameraModelCreateDto.getGatewayId());
    }


    public void deleteByCommonModel() {
        jdbcTemplate.update("DELETE FROM camera_model WHERE is_common_model = ?", true);
    }


}
