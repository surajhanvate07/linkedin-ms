package com.suraj.linkedinms.api_gateway.filters;

import com.suraj.linkedinms.api_gateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	private final JwtService jwtService;

	public AuthenticationFilter(JwtService jwtService) {
		super(Config.class);
		this.jwtService = jwtService;
	}

	@Override
	public GatewayFilter apply(Config config) {
		log.info("Applying Authentication Filter");
		return ((exchange, chain) -> {
			log.info("Authentication Filter: Login Request - {}", exchange.getRequest().getURI());

			String userId = null;
			try {
				final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
				if (authHeader == null || !authHeader.startsWith("Bearer ")) {
					log.error("Authentication failed: Missing or invalid Authorization header");
					exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
					return exchange.getResponse().setComplete();
				}

//			String token = authHeader.substring(7);
				String token = authHeader.split("Bearer ")[1].trim();
				userId = jwtService.extractUserIdFromToken(token);
				log.info("Extracted userId from JWT token: {}", userId);

				if (userId == null) {
					log.error("Authentication failed: Invalid JWT token");
					exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
					return exchange.getResponse().setComplete();
				}
			} catch (JwtException e) {
				log.error(e.getLocalizedMessage());
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

			ServerWebExchange mutatedExchange = exchange.mutate()
					.request(exchange.getRequest().mutate()
							.header("X-User-Id", userId)
							.build())
					.build();

			return chain.filter(mutatedExchange);
		});
	}

	public static class Config {

	}


}
