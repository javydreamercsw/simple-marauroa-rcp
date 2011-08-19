/*
 * $Rev: 308 $
 * $LastChangedDate: 2010-05-02 17:45:46 -0500 (Sun, 02 May 2010) $
 * $LastChangedBy: javydreamercsw $
 */
package simple.marauroa.client.components.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User login profile list.
 */
public class ProfileList {

    /**
     *
     */
    protected ArrayList<Profile> profiles;

    /**
     * Create an empty profile list.
     */
    public ProfileList() {
        profiles = new ArrayList<Profile>();
    }

    //
    // ProfileList
    //
    /**
     * Add a profile. This will remove duplicates.
     *
     * @param profile
     *            A user login profile.
     */
    public void add(Profile profile) {
        /*
         * Keep one equivalent entry (can't use HasSet and preserve order)
         */
        profiles.remove(profile);
        profiles.add(profile);
    }
    
    public boolean isEmpty(){
        return profiles.isEmpty();
    }

    /**
     * Remove all profiles.
     */
    public void clear() {
        profiles.clear();
    }

    /**
     * Get an iterator of profiles.
     *
     * @return An iterator of profiles.
     */
    public Iterator<Profile> iterator() {
        return profiles.iterator();
    }

    /**
     * Load a list of profiles from an input stream. This will replace any
     * existing list.
     * @param in The Stream to read
     * @throws IOException if any IO operation fails
     */
    public void load(InputStream in) throws IOException {
        Encoder codec = new Encoder();
        String s;
        BufferedReader r = new BufferedReader(new InputStreamReader(in));

        clear();
        while ((s = r.readLine()) != null) {
            add(Profile.decode(codec.decode(s)));
        }
    }

    /**
     * Remove a profile.
     *
     * @param profile
     *            A user login profile.
     */
    public void remove(Profile profile) {
        profiles.remove(profile);
    }

    /**
     * Save a list of profiles to an output stream.
     *
     * @param out
     *            The stream to write.
     * @throws IOException if any IO operation fails
     */
    public void save(OutputStream out) throws IOException {
        Encoder codec = new Encoder();

        PrintStream ps = new PrintStream(out);

        try {
            Iterator<Profile> iter = iterator();

            while (iter.hasNext()) {
                ps.println(codec.encode(iter.next().encode()));
            }
        } finally {
            ps.flush();
        }
    }

    //
    //
    /**
     *
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        ProfileList list;
        InputStream in;

        if (args.length != 1) {
            System.err.println("Usage: java " + ProfileList.class.getName() + " <user.dat>");

            System.exit(1);
        }

        list = new ProfileList();

        in = new java.io.FileInputStream(args[0]);

        try {
            list.load(in);

        } finally {
            in.close();
        }

        Iterator<Profile> iter = list.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
