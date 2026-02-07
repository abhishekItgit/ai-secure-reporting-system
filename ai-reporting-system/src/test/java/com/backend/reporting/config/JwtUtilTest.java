package com.backend.reporting.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilTest {

    @Test
    void generatesAndParsesRoleClaim() {
        JwtUtil jwtUtil = new JwtUtil("this-is-a-very-long-jwt-secret-key-123456", 60000);
        jwtUtil.init();

        String token = jwtUtil.generateToken("alice", "ADMIN");

        assertEquals("alice", jwtUtil.extractUsername(token));
        assertEquals("ADMIN", jwtUtil.extractRole(token));
    }
}
