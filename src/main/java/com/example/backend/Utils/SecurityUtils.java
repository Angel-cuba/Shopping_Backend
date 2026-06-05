package com.example.backend.Utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility methods for Spring Security context — avoids duplicating
 * SecurityContextHolder access across controllers.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Returns the username of the currently authenticated principal.
     * Works with both UserDetails and raw string principals.
     */
    public static String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }
}
