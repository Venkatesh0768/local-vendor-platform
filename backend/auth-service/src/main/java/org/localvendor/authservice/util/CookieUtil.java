package org.localvendor.authservice.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    @Value("${app.cookie.domain:localhost}")
    private String cookieDomain;

    @Value("${app.cookie.secure:true}")
    private boolean secure;

    @Value("${app.cookie.same-site:Strict}")
    private String sameSite;

    /**
     * Create HTTP-only secure cookie
     */
    public void createCookie(
            HttpServletResponse response,
            String name,
            String value,
            long maxAge
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure); // true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge);
        cookie.setDomain(cookieDomain);

        // Set SameSite attribute via header (Spring Boot doesn't support it directly in Cookie)
        String cookieHeader = String.format(
                "%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=%s; Domain=%s",
                name, value, (int) maxAge, sameSite, cookieDomain
        );
        response.addHeader("Set-Cookie", cookieHeader);
    }

    /**
     * Get cookie value by name
     */
    public Optional<String> getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> name.equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue);
        }
        return Optional.empty();
    }

    /**
     * Delete cookie
     */
    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain(cookieDomain);
        response.addCookie(cookie);
    }
}