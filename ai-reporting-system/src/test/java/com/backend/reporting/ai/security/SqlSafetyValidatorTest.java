package com.backend.reporting.ai.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlSafetyValidatorTest {

    private final SqlSafetyValidator validator = new SqlSafetyValidator();

    @Test
    void allowsSimpleSelect() {
        assertDoesNotThrow(() -> validator.validate("SELECT * FROM users"));
    }

    @Test
    void blocksNonSelectStatements() {
        assertThrows(SecurityException.class, () -> validator.validate("UPDATE users SET name = 'x'"));
    }

    @Test
    void blocksNotEnoughInformationMarker() {
        assertThrows(SecurityException.class, () -> validator.validate("-- NOT_ENOUGH_INFORMATION"));
    }
}
