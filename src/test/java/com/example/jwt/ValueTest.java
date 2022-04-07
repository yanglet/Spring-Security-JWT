package com.example.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ValueTest {
    @Value("${jwt.secret}")
    private String secretKey;

    @Test
    public void 값이들어가나(){
        System.out.println("this.secretKey = " + this.secretKey);
        Assertions.assertThat(this.secretKey).isEqualTo("c2VjdXJpdHktand0LXlhbmdsZXQ=");
    }
}
