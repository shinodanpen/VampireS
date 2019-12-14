package org.luca.VampireS.resourcepack;

import org.luca.VampireS.VampireSPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilChecksum {

    public static byte[] getChecksum(byte[] input) {
        try {
            byte[] hash = new byte[20];
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hash = md.digest(input);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
            VampireSPlugin.getInstance().getLogger().severe("Unable to find SHA-1 algorithm");
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] fileToByteArray(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            VampireSPlugin.getInstance().getLogger().severe("Unable to read bytes from file");
            e.printStackTrace();
        }
        return null;
    }

}

