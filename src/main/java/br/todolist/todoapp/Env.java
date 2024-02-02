package br.todolist.todoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
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
}
