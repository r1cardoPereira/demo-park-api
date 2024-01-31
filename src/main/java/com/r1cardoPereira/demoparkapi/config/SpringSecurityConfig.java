package com.r1cardoPereira.demoparkapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration // Anotação que indica que esta classe é uma classe de configuração do Spring.
@EnableWebMvc // Anotação que habilita o suporte para o Spring MVC.
@EnableMethodSecurity // Anotação que habilita a segurança em nível de método.

public class SpringSecurityConfig { // Declaração da classe de configuração de segurança.

    // Método que configura a cadeia de filtros de segurança do Spring Security.
    @Bean // Anotação que indica que este método produz um bean que será gerenciado pelo Spring.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http // Configuração do objeto HttpSecurity.
            .csrf(csrf -> csrf.disable()) // Desativa a proteção CSRF.
            .formLogin(form -> form.disable()) // Desativa o login baseado em formulário.
            .httpBasic(basic -> basic.disable()) // Desativa a autenticação básica HTTP.
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers(HttpMethod.POST,"api/v1/usuarios").permitAll() // Permite a todos os usuários (autenticados ou não) fazerem solicitações POST para "api/v1/usuarios".
                .anyRequest().authenticated() // Exige que todas as outras solicitações sejam autenticadas.
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura a política de criação de sessão como STATELESS.
            .build(); // Constrói a cadeia de filtros de segurança.
    }

    // Método que fornece um codificador de senha para ser usado ao codificar senhas de usuário.
    @Bean // Anotação que indica que este método produz um bean que será gerenciado pelo Spring.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // Retorna uma nova instância de BCryptPasswordEncoder.
    }

    // Método que fornece um gerenciador de autenticação para ser usado ao autenticar usuários.
    @Bean // Anotação que indica que este método produz um bean que será gerenciado pelo Spring.
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager(); // Retorna o gerenciador de autenticação do Spring Security.
    }
    
}
