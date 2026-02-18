package com.microsoft.jenkins.azuread.oauth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class StateCacheTest {

    @AfterEach
    void tearDown() {
        StateCache.CACHE.invalidateAll();
    }

    @Test
    void stateIsConsumedAfterRemoval() {
        String state = "test-state";
        StateCache.CacheHolder holder = new StateCache.CacheHolder("http://example.com", 0L, "test-nonce");
        StateCache.CACHE.put(state, holder);

        // First removal should return the cached value
        StateCache.CacheHolder removed = StateCache.CACHE.asMap().remove(state);
        assertNotNull(removed);
        assertEquals("test-nonce", removed.nonce());

        // Second removal should return null (state consumed)
        StateCache.CacheHolder removedAgain = StateCache.CACHE.asMap().remove(state);
        assertNull(removedAgain);
    }

    @Test
    void stateNotPresentReturnsNull() {
        StateCache.CacheHolder removed = StateCache.CACHE.asMap().remove("nonexistent-state");
        assertNull(removed);
    }
}
