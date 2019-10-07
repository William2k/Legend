package com.legendApi.helpers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHelper {
    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public static String hash(String password, String salt) {
        return argon2.hash(10, 65536, 1, (password + salt).getBytes());
    }

    public static boolean verify(String hashedPassword, String password, String salt) {
        return argon2.verify(hashedPassword, (password + salt).getBytes());
    }
}
