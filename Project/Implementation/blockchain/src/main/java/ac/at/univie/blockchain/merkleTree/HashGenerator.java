package ac.at.univie.blockchain.merkleTree;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//This clase is used from the page https://www.pranaybathini.com/2021/05/merkle-tree.html, https://www.baeldung.com/sha-256-hashing-java and SHA-256 documentation
public class HashGenerator {

    public String generateHash(String dataToHash) {

        MessageDigest messageDigest = null;
        byte[] bytes = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            bytes = messageDigest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        for (byte singleByte : bytes) {
            buffer.append(String.format("%02x", singleByte));
        }
        return buffer.toString();
    }

}
