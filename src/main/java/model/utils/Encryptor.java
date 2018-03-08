package model.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Encrypts the data.
 */
public class Encryptor {

    /**
     * Hashes text passwords.
     *
     * @param plainTextPassword     text to be hashed
     * @return                      hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Checks equality of two passwords.
     *
     * @param plainPassword         plain text password
     * @param hashedPassword        hashed password
     * @return                      boolean result of the comparison
     */
    public static boolean checkPass(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
