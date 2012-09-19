package simple.marauroa.application.api;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public enum ConfigurationElement {

    DATABASE("database_implementation", ConfigElementType.STRING,
    "simple.server.application.db.SimpleDatabase"),
    FACTORY("factory_implementation", ConfigElementType.CLASS,
    "simple.server.core.engine.SimpleRPObjectFactory"),
    UNIVERSE("world", ConfigElementType.CLASS,
    "simple.server.core.engine.SimpleRPWorld"),
    RULE_PROCESSOR("ruleprocessor", ConfigElementType.CLASS,
    "simple.server.core.engine.SimpleRPRuleProcessor"),
    JDBC_URL("jdbc_url", ConfigElementType.STRING,
    "jdbc:h2:file:data/marauroa;CREATE=TRUE;AUTO_SERVER=TRUE;"
    + "DB_CLOSE_ON_EXIT=FALSE;LOCK_TIMEOUT=10000",
    "jdbc:mysql://localhost:3306/marauroa"),
    JDBC_CLASS("jdbc_class", ConfigElementType.CLASS, "org.h2.Driver",
    "com.mysql.jdbc.Driver"),
    DATABASE_ADAPTER("database_adapter", ConfigElementType.CLASS,
    "marauroa.server.db.adapter.H2DatabaseAdapter",
    "marauroa.server.db.adapter.MySQLDatabaseAdapter"),
    JDBC_USER("jdbc_user", ConfigElementType.STRING, "simple_user"),
    JDBC_PWD("jdbc_pwd", ConfigElementType.STRING, "password"),
    GAME_SERVER("gameserver_implementation", ConfigElementType.STRING,
    "marauroa.server.game.GameServerManager"),
    TCP_PORT("tcp_port", ConfigElementType.INT, "32180"),
    TURN_LENGTH("turn_length", ConfigElementType.INT, "100"),
    SERVER_TYPE("server_typeGame", ConfigElementType.STRING, "Simple"),
    SERVER_NAME("server_name", ConfigElementType.STRING, "Simple"),
    SERVER_VERSION("server_version", ConfigElementType.STRING, "0.02.05"),
    SERVER_CONTACT("server_contact", ConfigElementType.STRING, "https://sourceforge.net/tracker/?group_id=325779&atid=1367795"),
    SERVER_WELCOME("server_welcome", ConfigElementType.STRING, "This release is EXPERIMENTAL."
    + "Remember to keep your password completely secret, never tell to another friend, "
    + "player, or admin. Enjoy your stay!"),
    SERVER_EXTENSION("server_extension", ConfigElementType.STRING),
    STATS("statistics_filename", ConfigElementType.STRING, "./simple.server_stats.xml"),
    SYSTEM_PASSWORD("system_password", ConfigElementType.STRING, "password"),
    SYSTEM_EMAIL("system_email", ConfigElementType.STRING, "system@email.com"),
    SYSTEM_NAME("system_account_name", ConfigElementType.STRING, "System"),
    KEY("key", ConfigElementType.STRING),
    CLIENT_OBJECT("client_object", ConfigElementType.STRING,
    "simple.server.core.entity.clientobject.ClientObject"),
    LOG4J("log4j_url", ConfigElementType.STRING, "simple/server/log4j.properties");
    private String name = null;
    private ConfigElementType type = null;
    private Object value = null;
    private Object alternate = null;

    ConfigurationElement(String name, ConfigElementType type) {
        this.name = name;
        this.type = type;
    }

    ConfigurationElement(String name, ConfigElementType type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.alternate = value;
    }

    ConfigurationElement(String name, ConfigElementType type, Object value, Object alternate) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.alternate = alternate;
    }

    public static ConfigurationElement get(String name) {
        for (ConfigurationElement ce : ConfigurationElement.values()) {
            if (ce.getName().equals(name)) {
                return ce;
            }
        }
        return null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public ConfigElementType getType() {
        return type;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return getObject(value);
    }

    public Object getAlternate() {
        return getObject(alternate);
    }

    private Object getObject(Object value) {
        switch (type) {
            case CLASS:
            case STRING:
                return (String) value;
            case INT:
                return Integer.valueOf((String) value);
            default:
                Logger.getLogger(ConfigurationElement.class.getSimpleName()).log(
                        Level.WARNING, "Type: {0} is not supported yet. "
                        + "Returning an un-casted object", type.name());
                return value;
        }
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @param value the value to set as alternate
     */
    public void setAlternate(Object value) {
        this.alternate = value;
    }
}
