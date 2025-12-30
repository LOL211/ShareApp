package org.kush.share.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"test", "local"})
class ShareApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
