package it.verde.persistence.connection;

import it.verde.config.DatabaseConfig;
import it.verde.exception.DaoException;
import it.verde.model.type.UserRole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class JdbcConnectionManager implements ConnectionProvider, DatabaseSession {
    private static final Logger LOGGER = Logger.getLogger(JdbcConnectionManager.class.getName());
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private final DatabaseConfig config;
    private Connection connection;
    private CredentialProfile activeProfile = CredentialProfile.LOGIN;

    private JdbcConnectionManager(DatabaseConfig config) {
        this.config = Objects.requireNonNull(config);
        loadDriver();
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeAtShutdown, "verde-jdbc-shutdown"));
    }

    public static JdbcConnectionManager getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public synchronized Connection getConnection() throws DaoException {
        try {
            if (connection == null || connection.isClosed()) connection = createConnection(activeProfile);
            return connection;
        } catch (SQLException e) {
            throw new DaoException("Unable to open database connection", e);
        }
    }

    @Override
    public synchronized void useLoginRole() throws DaoException {
        switchProfile(CredentialProfile.LOGIN);
    }

    @Override
    public synchronized void useRole(UserRole role) throws DaoException {
        if (role == null) throw new DaoException("Database role is required");
        switchProfile(CredentialProfile.from(role));
    }

    @Override
    public synchronized boolean isValid(int timeoutSeconds) throws DaoException {
        if (timeoutSeconds < 0) throw new IllegalArgumentException("Timeout cannot be negative");

        try {
            return getConnection().isValid(timeoutSeconds);
        } catch (SQLException e) {
            throw new DaoException("Unable to validate database connection", e);
        }
    }

    @Override
    public synchronized void close() throws DaoException {
        closeCurrentConnection();
        activeProfile = CredentialProfile.LOGIN;
    }

    private void switchProfile(CredentialProfile targetProfile) throws DaoException {
        if (targetProfile == activeProfile && isOpen(connection)) return;

        CredentialProfile previousProfile = activeProfile;
        closeCurrentConnection();

        try {
            connection = createConnection(targetProfile);
            activeProfile = targetProfile;
        } catch (SQLException e) {
            activeProfile = previousProfile;
            throw new DaoException("Unable to switch database role", e);
        }
    }

    private Connection createConnection(CredentialProfile profile) throws SQLException {
        return DriverManager.getConnection(config.connectionUrl(), config.value(profile.userKey), config.value(profile.passwordKey));
    }

    private void closeCurrentConnection() throws DaoException {
        if (connection == null) return;

        Connection current = connection;
        connection = null;

        try {
            if (!current.isClosed()) {
                if (!current.getAutoCommit()) current.rollback();
                current.close();
            }
        } catch (SQLException e) {
            throw new DaoException("Unable to close database connection", e);
        }
    }

    private boolean isOpen(Connection candidate) throws DaoException {
        if (candidate == null) return false;

        try {
            return !candidate.isClosed();
        } catch (SQLException e) {
            throw new DaoException("Unable to inspect database connection", e);
        }
    }

    private void closeAtShutdown() {
        try {
            close();
        } catch (DaoException e) {
            LOGGER.log(Level.WARNING, "Unable to close database connection during JVM shutdown", e);
        }
    }

    private void loadDriver() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver not found", e);
        }
    }

    private enum CredentialProfile {
        LOGIN("LOGIN_USER", "LOGIN_PASS"),
        ADMINISTRATOR("ADMINISTRATOR_USER", "ADMINISTRATOR_PASS"),
        SALES_MANAGER("COMMERCIALE_USER", "COMMERCIALE_PASS"),
        LOGISTICS_MANAGER("LOGISTICO_USER", "LOGISTICO_PASS");

        private final String userKey;
        private final String passwordKey;

        CredentialProfile(String userKey, String passwordKey) {
            this.userKey = userKey;
            this.passwordKey = passwordKey;
        }

        private static CredentialProfile from(UserRole role) {
            return switch (role) {
                case ADMINISTRATOR -> ADMINISTRATOR;
                case SALES_MANAGER -> SALES_MANAGER;
                case LOGISTICS_MANAGER -> LOGISTICS_MANAGER;
            };
        }
    }

    private static final class Holder {
        private static final JdbcConnectionManager INSTANCE = new JdbcConnectionManager(DatabaseConfig.load());
    }
}
