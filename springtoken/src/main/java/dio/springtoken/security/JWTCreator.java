package dio.springtoken.security;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

public class JWTCreator {
	public static final String HEADER_AUTHORIZARION = "authorization";
	public static final String ROLES_AUTHORITIES = "authorities";
	
	
	public static String create(String prefix, String key, JWTObject jwtObject) {
        // Converte a chave String para um objeto SecretKey, que é o exigido pela nova API
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .subject(jwtObject.getSubject())
                .issuedAt(jwtObject.getIssuedAt())
                .expiration(jwtObject.getExpiration())
                .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles()))
                // Usa a nova forma de assinar, com o objeto SecretKey
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
        
        return prefix + " " + token;
    }
	public static JWTObject create(String token, String prefix, String key) throws 
	ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

		JWTObject object = new JWTObject();
		token = token.replace(prefix, "").trim();
		Claims claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		object.setSubject(claims.getSubject());
		object.setExpiration(claims.getExpiration());
		object.setIssuedAt(claims.getIssuedAt());
		
		List<String> roles = claims.get(ROLES_AUTHORITIES, List.class);
        if (roles != null) {
            object.setRoles(roles.toArray(new String[0]));
        }
		
		return object;
		
	}
	
	public static List<String> checkRoles(List<String> roles){
		return roles.stream().map(s -> "ROLE_".concat(s.replaceFirst("ROLE_", ""))).collect(Collectors.toList()); 
	}

}
