package it.verde.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class DatabaseConfig {
    private static final String DB_PROPERTIES = "db.properties";

    private static final List<String> REQUIRED_KEYS = List.of(
        "CONNECTION_URL",
        "LOGIN_USER", "LOGIN_PASS",
        "ADMINISTRATOR_USER", "ADMINISTRATOR_PASS",
        "COMMERCIALE_USER", "COMMERCIALE_PASS",
        "LOGISTICO_USER", "LOGISTICO_PASS"
    );

    private static final List<Path> EXTERNAL_PATHS = List.of(
        Path.of("config", DB_PROPERTIES),
        Path.of(DB_PROPERTIES)
    );

    private final Properties properties;

    private DatabaseConfig(Properties properties) {
        this.properties = properties;
        validate();
    }

    public static DatabaseConfig load() {
        for (Path path : EXTERNAL_PATHS) {
            Properties properties = new Properties();
            if (loadFromFile(properties, path)) return new DatabaseConfig(properties);
        }

        Properties properties = new Properties();
        if (loadFromClasspath(properties)) return new DatabaseConfig(properties);

        throw new IllegalStateException("Unable to load database configuration");
    }

    public String connectionUrl() {
        return getRequired("CONNECTION_URL");
    }

    public String value(String key) {
        return getRequired(key);
    }

    private static boolean loadFromClasspath(Properties properties) {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(DB_PROPERTIES)) {
            if (input == null) return false;
            properties.load(input);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read db.properties from classpath", e);
        }
    }

    private static boolean loadFromFile(Properties properties, Path path) {
        if (!Files.isRegularFile(path)) return false;

        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read database configuration from " + path, e);
        }
    }

    private void validate() {
        for (String key : REQUIRED_KEYS) getRequired(key);
    }

    private String getRequired(String key) {
        String value = properties.getProperty(Objects.requireNonNull(key));
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing database property: " + key);
        return value;
    }
}