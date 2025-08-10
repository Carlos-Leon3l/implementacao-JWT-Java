package dio.springtoken.controller;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dio.springtoken.dto.Login;
import dio.springtoken.dto.Sessao;
import dio.springtoken.model.User;
import dio.springtoken.repository.UserRepository;
import dio.springtoken.security.JWTCreator;
import dio.springtoken.security.JWTObject;
import dio.springtoken.security.SecurityConfig;

@RestController
public class LoginController {
    @Autowired
    private PasswordEncoder encoder;
    
    private final SecurityConfig securityConfig;

    // Injetando o bean de configuração via construtor (melhor prática)
    @Autowired
    public LoginController(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }
    
    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    public Sessao logar(@RequestBody Login login){
        User user = repository.findByUsername(login.getUsername());
        if(user != null) {
            boolean passwordOk = encoder.matches(login.getPassword(), user.getPassword());
            if (!passwordOk) {
                throw new RuntimeException("Senha inválida para o login: " + login.getUsername());
            }
            
            //estamos enviando um objeto Sessão para retornar mais informações do usuário
            Sessao sessao = new Sessao();
            sessao.setLogin(user.getUsername());
            
            JWTObject jwtObject = new JWTObject();
            jwtObject.setExpiration(new Date(System.currentTimeMillis() + securityConfig.EXPIRATION));
            jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
            jwtObject.setRoles(user.getRoles().toArray(new String[0]));
            
            
            sessao.setToken(JWTCreator.create(securityConfig.PREFIX, securityConfig.KEY, jwtObject));
            
            return sessao;
        } else {
            throw new RuntimeException("Erro ao tentar fazer login");
        }
    }
}
