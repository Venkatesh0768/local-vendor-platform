package org.localvendor.authservice.aspect;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.localvendor.authservice.exception.RateLimitExceededException;
import org.localvendor.authservice.util.IpAddressUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedisTemplate<String, String> redisTemplate;
    private final HttpServletRequest request;

    @Around("@annotation(rateLimit)")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = generateKey(rateLimit.key());
        String count = redisTemplate.opsForValue().get(key);

        if (count != null && Integer.parseInt(count) >= rateLimit.limit()) {
            log.warn("Rate limit exceeded for key: {}", key);
            throw new RateLimitExceededException(
                    "Rate limit exceeded. Please try again in " + rateLimit.duration() + " seconds."
            );
        }

        // Increment counter
        Long newCount = redisTemplate.opsForValue().increment(key);
        if (newCount != null && newCount == 1) {
            redisTemplate.expire(key, rateLimit.duration(), TimeUnit.SECONDS);
        }

        return joinPoint.proceed();
    }

    private String generateKey(String prefix) {
        String ipAddress = IpAddressUtil.getClientIpAddress(request);
        return String.format("rate_limit:%s:%s", prefix, ipAddress);
    }
}
