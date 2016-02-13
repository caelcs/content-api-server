package uk.co.caeldev.content.api.features.security;

import com.google.common.collect.Sets;

import java.util.Set;

public final class OriginsHelper {

    private final static Set<String> allowedOrigins = Sets.newHashSet("http://127.0.0.1:8888", "http://localhost:8888", "http://www.caeldev.co.uk");

    public static boolean isValid(String origin) {
        return allowedOrigins.contains(origin);
    }
}
