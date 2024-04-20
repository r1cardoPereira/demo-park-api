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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.r1cardoPereira.demoparkapi.jwt.JwtAuthenticationEntryPoint;
import com.r1cardoPereira.demoparkapi.jwt.JwtAuthorizationFilter;

@Configuration // Anotação que indica que esta classe é uma classe de configuração do Spring.
@EnableWebMvc // Anotação que habilita o suporte para o Spring MVC.
@EnableMethodSecurity // Anotação que habilita a segurança em nível de método.

public class SpringSecurityConfig { // Declaração da classe de configuração de segurança.

    private static final String[] DOCUMENTATION_OPENAPI = {
        "/docs/index.html",
        "/docs-park.html", "/docs-park/**",
        "/v3/api-docs/**",
        "/swagger-ui-custom.html", "/swagger-ui.html","/swagger-ui/**",
        "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };

    // Método que configura a cadeia de filtros de segurança do Spring Security.
    @Bean // Anotação que indica que este método produz um bean que será gerenciado pelo Spring.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http // Configuração do objeto HttpSecurity.
            .csrf(csrf -> csrf.disable()) // Desativa a proteção CSRF.
            .formLogin(form -> form.disable()) // Desativa o login baseado em formulário.
            .httpBasic(basic -> basic.disable()) // Desativa a autenticação básica HTTP.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST,"api/v1/usuarios").permitAll() // Permite a todos os usuários (autenticados ou não) fazerem solicitações POST para "api/v1/usuarios".
                .requestMatchers(HttpMethod.POST,"api/v1/auth").permitAll()
                .requestMatchers(DOCUMENTATION_OPENAPI).permitAll() 
                .anyRequest().authenticated() // Exige que todas as outras solicitações sejam autenticadas.
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class) // Configura a política de criação de sessão como STATELESS.
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))  
            .build(); // Constrói a cadeia de filtros de segurança.
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter();
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
