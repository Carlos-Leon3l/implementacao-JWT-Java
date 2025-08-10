package dio.springtoken.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// A classe que contém este método geralmente estende OncePerRequestFilter
@Component	
public class JWTFilter extends OncePerRequestFilter {
	
	
	private final SecurityConfig securityConfig;

    // Injetando o bean de configuração via construtor (melhor prática)
    @Autowired
    public JWTFilter(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        //obtem o token da request com AUTHORIZATION
        String token = request.getHeader(JWTCreator.HEADER_AUTHORIZARION);
        
        //esta implementação só esta validando a integridade do token
        try {
            if (token != null && !token.isEmpty()) {
                JWTObject tokenObject = JWTCreator.create(token, securityConfig.PREFIX ,securityConfig.KEY);

                List<SimpleGrantedAuthority> authorities = authorities(tokenObject.getRoles());

                UsernamePasswordAuthenticationToken userToken =
                        new UsernamePasswordAuthenticationToken(
                                tokenObject.getSubject(),
                                null,
                                authorities);
                
                // O código continua a partir daqui, provavelmente com:
                SecurityContextHolder.getContext().setAuthentication(userToken);

            } else {
            	SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Lidar com a exceção de token inválido
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            e.printStackTrace();
            return;
        }
        
    }
    
    // Método auxiliar 'authorities' que provavelmente existe na classe
    private List<SimpleGrantedAuthority> authorities(List<String> roles) {
        // VERIFICAÇÃO ADICIONADA AQUI
        if (roles == null) {
            // Se não houver roles no token, retorna uma lista vazia de permissões
        	System.out.println("esta vazio");
            return Collections.emptyList();
        }
        
        // Se a lista não for nula, o código continua como antes
        var listadeRoles = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        System.out.println(listadeRoles);
        return listadeRoles; 
        		
    }
}
