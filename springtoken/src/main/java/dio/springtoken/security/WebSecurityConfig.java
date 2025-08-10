package dio.springtoken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig 	{
	
	// Lista de URLs públicas para o Swagger
    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    
    // O Bean principal que define toda a cadeia de filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JWTFilter jwtFilter) throws Exception {
        http
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable()) // Habilitando o frame do H2 Console
            )
            .cors(cors -> {}) // Configuração de CORS, se necessário
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs stateless
            
            // Define a política de sessão como STATELESS (sem estado) para JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Define as regras de autorização para as requisições
            .authorizeHttpRequests(authorize -> authorize
                // Libera o acesso aos endpoints do Swagger e do H2 Console
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // Libera o acesso ao endpoint de login
                .requestMatchers("/login").permitAll()
                
                // Libera o acesso para criar um novo usuário (POST em /users)
                .requestMatchers(HttpMethod.POST, "/users").permitAll()

                // Apenas usuários com roles 'USERS' ou 'MANAGERS' podem acessar GET em /users
                .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USERS", "MANAGERS")
                
                // Apenas usuários com role 'MANAGERS' podem acessar /managers
                .requestMatchers("/managers").hasRole("MANAGERS")
                
                // Exige autenticação para todas as outras requisições
                .anyRequest().authenticated()
            )
            
            // Adiciona o filtro de JWT antes do filtro padrão de autenticação
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
   
   
 
    
}