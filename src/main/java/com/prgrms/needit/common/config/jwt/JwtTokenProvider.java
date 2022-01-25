package com.prgrms.needit.common.config.jwt;

import com.prgrms.needit.domain.user.dto.Response;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final String AUTHORITIES_KEY = "auth";
	private static final String BEARER_TYPE = "Bearer";
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

	private final Key key;
	private final RedisTemplate<String, String> redisTemplate;

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secretKey,
		RedisTemplate<String, String> redisTemplate
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.redisTemplate = redisTemplate;
	}

	public Response.TokenInfo generateToken(Authentication authentication) {
		String authorities = authentication.getAuthorities()
										   .stream()
										   .map(GrantedAuthority::getAuthority)
										   .collect(Collectors.joining(","));

		long now = (new Date()).getTime();

		Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
		String accessToken = Jwts.builder()
								 .setSubject(authentication.getName())
								 .claim(AUTHORITIES_KEY, authorities)
								 .setExpiration(accessTokenExpiresIn)
								 .signWith(key, SignatureAlgorithm.HS256)
								 .compact();

		String refreshToken = Jwts.builder()
								  .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
								  .signWith(key, SignatureAlgorithm.HS256)
								  .compact();

		return Response.TokenInfo.builder()
								 .grantType(BEARER_TYPE)
								 .accessToken(accessToken)
								 .refreshToken(refreshToken)
								 .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
								 .build();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		if (claims.get(AUTHORITIES_KEY) == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(AUTHORITIES_KEY)
								.toString()
								.split(","))
				  .map(SimpleGrantedAuthority::new)
				  .collect(Collectors.toList());

		UserDetails principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}

	public boolean existsToken(String token) {
		return ObjectUtils.isEmpty(redisTemplate.opsForValue()
												.get(token));
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
					   .setSigningKey(key)
					   .build()
					   .parseClaimsJws(accessToken)
					   .getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public Long getExpiration(String accessToken) {
		Date expiration = Jwts.parserBuilder()
							  .setSigningKey(key)
							  .build()
							  .parseClaimsJws(accessToken)
							  .getBody()
							  .getExpiration();

		Long now = new Date().getTime();
		return (expiration.getTime() - now);
	}
}
