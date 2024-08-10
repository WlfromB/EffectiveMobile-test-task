package org.example.effectivemobiletesttask.security;

public interface PasswordProvider {
    String getPassword(String password);

    boolean passwordMatches(String password, String hashedPassword);
}
