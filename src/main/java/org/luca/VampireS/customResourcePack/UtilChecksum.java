package org.luca.VampireS.customResourcePack;

import org.luca.VampireS.MainClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilChecksum {

    private final CustomResourcePack addon;

    private final MainClass plugin;

    public ResourcePackReject(CustomResourcePack addon, MainClass plugin) {
        this.addon = addon;
        this.plugin = plugin;
    }

    public static byte[] getChecksum(byte[] input) {
        try {
            byte[] hash = new byte[20];
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hash = md.digest(input);
            return hash;
        } catch (NoSuchAlgorithmException ex) {
            CustomResourcePack.getInstance.getLogger().severe("Unable to find SHA-1 algorithm");
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] fileToByteArray(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            CustomResourcePack.getInstance().getLogger().severe("Unable to read bytes from file");
            e.printStackTrace();
        }
        return null;
    }

}

