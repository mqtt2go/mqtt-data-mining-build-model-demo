package com.vutbr.feec.utko.demo;

import com.vutbr.feec.utko.demo.config.HikariDataSourceJdbcTemplate;
import com.vutbr.feec.utko.demo.repositories.CameraRepository;
import com.vutbr.feec.utko.demo.repositories.LightRepository;
import com.vutbr.feec.utko.demo.repositories.MultiSocketRepository;
import com.vutbr.feec.utko.demo.repositories.SocketRepository;
import com.vutbr.feec.utko.demo.service.DataScienceCommonModelService;
import com.vutbr.feec.utko.demo.service.FindRangePercentilesService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(HikariDataSourceJdbcTemplate.getDataSource());

        CameraRepository cameraRepository = new CameraRepository(jdbcTemplate);
        LightRepository lightRepository = new LightRepository(jdbcTemplate);
        MultiSocketRepository multiSocketRepospitory = new MultiSocketRepository(jdbcTemplate);
        SocketRepository socketRepository = new SocketRepository(jdbcTemplate);

        FindRangePercentilesService findRangePercentilesService = new FindRangePercentilesService();
        DataScienceCommonModelService dataScienceCommonModelService =
                new DataScienceCommonModelService(cameraRepository,
                        findRangePercentilesService,
                        lightRepository,
                        socketRepository,
                        multiSocketRepospitory);
        dataScienceCommonModelService.retrainModel(5, 15, 5);
    }
}
