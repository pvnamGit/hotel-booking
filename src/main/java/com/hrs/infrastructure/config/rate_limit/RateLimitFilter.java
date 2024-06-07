package com.hrs.infrastructure.config.rate_limit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.io.IOException;
import java.time.Duration;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class RateLimitFilter extends GenericFilterBean {

  private final Bucket bucket;

  private final Integer RATE_LIMIT = 1;
  private final Integer RATE_TIMEOUT = 1;

  public RateLimitFilter() {
    // Define the rate limiting configuration (1 request per second)
    Bandwidth limit =
        Bandwidth.classic(
            RATE_LIMIT, Refill.intervally(RATE_LIMIT, Duration.ofSeconds(RATE_TIMEOUT)));
    this.bucket = Bucket4j.builder().addLimit(limit).build();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (bucket.tryConsume(1)) {
      // Request is within limits, proceed with the chain
      chain.doFilter(request, response);
    } else {
      // Request exceeds limits, return rate limit exceeded response
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }
  }
}
