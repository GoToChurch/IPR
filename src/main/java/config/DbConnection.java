package config;

import lombok.SneakyThrows;
import org.sql2o.GenericDatasource;
import org.sql2o.Sql2o;

import java.util.concurrent.ConcurrentHashMap;

import static utils.CommonUtils.MAPPER;
import static utils.CommonUtils.getFromResources;

public class DbConnection {
    public static volatile ConcurrentHashMap<String, Sql2o> connections = new ConcurrentHashMap<>();

    @SneakyThrows
    private static synchronized Sql2o prepareDbConnection(DbConfig dbConfig) {
        return new Sql2o(new GenericDatasource(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword()));
    }

    @SneakyThrows
    public static synchronized DbConfig getConfig() {
        return MAPPER.readValue(getFromResources("src/main/resources/db_properties.json"), DbConfig.class);
    }

    @SneakyThrows
    public static synchronized Sql2o prepareDbConnection() {
        return prepareDbConnection(getConfig());
    }

    public static synchronized Sql2o getConnection(String config) {
        if (!connections.containsKey(config)) connections.put(config, prepareDbConnection());
        return connections.get(config);
    }
}
