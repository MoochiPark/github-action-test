package com.jejuroad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class JejuRoadApplicationTests {

    @Test
    @DisplayName("실패하는 테스트")
    void failTest() {
        fail();
    }

}
