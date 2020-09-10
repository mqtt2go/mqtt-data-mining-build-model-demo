package com.vutbr.feec.utko.demo.repositories;

import org.springframework.jdbc.core.JdbcTemplate;

public class SocketRepository {

    private JdbcTemplate jdbcTemplate;

    public SocketRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
