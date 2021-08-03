package com.momo.server.integration.utils;

import com.momo.server.utils.Aes128;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AesTest {

    private Aes128 aes;


    public AesTest(){
        aes = new Aes128("this-is-test-key");
    }

    @Test
    public void 테스트() throws Exception {
        String origin = "originString";
        String encrypted = aes.encrypt(origin);
        assertEquals(origin, aes.decrypt(encrypted));
    }
}
