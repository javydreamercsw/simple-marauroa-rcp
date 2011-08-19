/*
 * $Rev: 308 $
 * $LastChangedDate: 2010-05-02 17:45:46 -0500 (Sun, 02 May 2010) $
 * $LastChangedBy: javydreamercsw $
 */
package simple.marauroa.client.components.common;

/**
 * User login profile.
 */
public class Profile {

    /** Old server names to remap. */
    protected static final String[] OLD_SERVER_HOSTS = {""};
    /** Default server name to replace old ones with. */
    protected static final String NEW_SERVER_HOST = "localhost";
    /** Default server port. */
    public static final int DEFAULT_SERVER_PORT = 32170;
    protected String host;
    protected int port;
    protected String user;
    protected String password;

    /**
     * Constructor
     */
    public Profile() {
        this("", DEFAULT_SERVER_PORT, "", "");
    }

    /**
     * Constructor
     * @param host
     * @param port
     * @param user
     * @param password
     */
    public Profile(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    //
    // Profile
    //
    /**
     * Encode the login profile as a string.
     * 
     * @return A string excoded form (with newlines).
     */
    public String encode() {
        StringBuilder sbuf;

        sbuf = new StringBuilder();
        sbuf.append(getHost());
        sbuf.append('\n');
        sbuf.append(getUser());
        sbuf.append('\n');
        sbuf.append(getPassword());
        sbuf.append('\n');
        sbuf.append(getPort());
        sbuf.append('\n');
        sbuf.append(true); // TCP

        return sbuf.toString();
    }

    /**
     * Get host
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     * Get Password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get Port
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Get user
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     * Decode a login profile from a string.
     * 
     * @param info
     *            The string encoded profile.
     * 
     * @return A login profile.
     */
    public static Profile decode(String info) {
        String[] params;
        Profile profile;
        String s;

        params = info.split("\n");

        profile = new Profile();

        /*
         * Server Host
         */
        if (params.length > 0) {
            s = params[0];

            for (String host : OLD_SERVER_HOSTS) {
                if (s.equals(host)) {
                    s = NEW_SERVER_HOST;
                    break;
                }
            }

            if (s.length() != 0) {
                profile.setHost(s);
            }
        }

        /*
         * User
         */
        if (params.length > 1) {
            s = params[1];

            if (s.length() != 0) {
                profile.setUser(s);
            }
        }

        /*
         * Password
         */
        if (params.length > 2) {
            s = params[2];

            if (s.length() != 0) {
                profile.setPassword(s);
            }
        }

        /*
         * Server Port
         */
        if (params.length > 3) {
            s = params[3];

            if (s.length() != 0) {
                try {
                    profile.setPort(Integer.parseInt(s));
                } catch (NumberFormatException ex) {
                }
            }
        }

        /*
         * Server Protocol (TCP/UDP)
         */
        // params[4]
        //
        // ignore this token for compatibility reasons
        // just add what every you want behind it
        return profile;
    }

    /**
     * Set host
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Set password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set port
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Set user
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    //
    // Object
    //
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Profile)) {
            return false;
        }

        Profile profile = (Profile) obj;

        if (!getHost().equals(profile.getHost())) {
            return false;
        }

        if (getPort() != profile.getPort()) {
            return false;
        }

        if (!getUser().equals(profile.getUser())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getHost().hashCode() ^ getUser().hashCode();
    }

    /**
     * Get the label string. This label is used for the profile selection list.
     * 
     * @return The label in the form of <em>user</em><strong>@</strong><em>server-host</em>[<strong>:</strong><em>port</em>].
     */
    @Override
    public String toString() {
        StringBuilder sbuf;

        sbuf = new StringBuilder();
        sbuf.append(getUser());
        sbuf.append('@');
        sbuf.append(getHost());

        if (getPort() != DEFAULT_SERVER_PORT) {
            sbuf.append(':');
            sbuf.append(getPort());
        }

        return sbuf.toString();
    }
}
