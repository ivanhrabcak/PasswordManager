package com.ivik.passwordmanager;

import org.junit.Test;

import static org.junit.Assert.*;
import com.ivik.passwordmanager.PasswordManager;

public class PasswordManagerTest {
    @Test
    public void encryptString() {
        String expected = "o5pv4GVm8vEdAe7i698xpQ=="; // '1234' encrypted with key '1234'
        String actual = PasswordManager.encryptString("1234", "1234");
        assertEquals(expected, actual);
    }

    @Test
    public void decryptString() {
        String expected = "1234";
        String actual = PasswordManager.decryptString("o5pv4GVm8vEdAe7i698xpQ==", "1234");
        assertEquals(expected, actual);
    }
}
