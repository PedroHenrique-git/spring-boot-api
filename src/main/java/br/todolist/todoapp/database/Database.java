package br.todolist.todoapp.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import br.todolist.todoapp.Env;

@Configuration
public class Database {
    @Autowired private Env env;
    
    private JdbcTemplate jdbc;

    @Bean
    public JdbcTemplate getJdbc() {
        jdbc = new JdbcTemplate(getDataSource());

        return jdbc;
    }

    private SingleConnectionDataSource getDataSource() {
        SingleConnectionDataSource sc = new SingleConnectionDataSource();
    
        sc.setDriverClassName(env.getDBDRIVER());
        sc.setUrl(env.getDBURL());
        sc.setUsername(env.getDBUSERNAME());
        sc.setPassword(env.getDBPASSWORD());

        return sc;
    }
}
