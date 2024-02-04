package br.todolist.todoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class Env {
    @Autowired private Environment environment;
    
    public String getDBURL() {
        return environment.getProperty("DB_URL");
    }

    public String getDBDRIVER() {
        return environment.getProperty("DB_DRIVER");
    }

    public String getDBPASSWORD() {
        return environment.getProperty("DB_PASSWORD");
    }

    public String getDBUSERNAME() {
        return environment.getProperty("DB_USERNAME");
    }

    public String getJWTSECRET() {
        return environment.getProperty("JWT_SECRET");
    }
}
