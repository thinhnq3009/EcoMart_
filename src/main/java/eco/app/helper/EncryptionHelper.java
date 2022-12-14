
package eco.app.helper;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author ThinhNQ
 */
public class EncryptionHelper {

  /**
   * It takes a string and returns a hash of that string.
   * 
   * @param string The string to hash.
   * @return A hash of the string.
   */
    public static String hash(String string) {
        return DigestUtils.sha256Hex(string).toUpperCase();
    }

    /**
     * It takes a string and a hash, and returns true if the hash of the string is
     * equal to the hash
     * 
     * @param string The string to hash.
     * @param hash   The hash to verify.
     * @return A boolean value.
     */
    public static boolean verify(String string, String hash) {
        return hash(string).equals(hash);
    }
    



}
