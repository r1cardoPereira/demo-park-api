package com.r1cardoPereira.demoparkapi.config;

// Importando as bibliotecas necessárias
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Classe SpringTimeZoneConfig é usada para configurar o fuso horário padrão para a aplicação.
 * <p>
 * Esta classe é marcada como uma classe de configuração do Spring, o que significa que ela pode conter métodos 
 * bean e pode ser processada pelo contêiner Spring para gerar definições de bean e solicitações de serviço 
 * em tempo de execução.
 */
@Configuration
public class SpringTimeZoneConfig {

    /**
     * Método timeZoneConfig é executado após a injeção de dependência estar completa para realizar qualquer 
     * inicialização necessária. Neste caso, ele está configurando o fuso horário padrão para "America/Sao_Paulo".
     * <p>
     * A anotação @PostConstruct é usada em um método que precisa ser executado após a injeção de dependência 
     * do Spring para executar qualquer inicialização.
     */
    @PostConstruct
    public void timeZoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
