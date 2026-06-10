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
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }
}
