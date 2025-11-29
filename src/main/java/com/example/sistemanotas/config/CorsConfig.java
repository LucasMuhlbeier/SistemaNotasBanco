package com.example.sistemanotas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração global do CORS para permitir que o frontend Angular
 * (ou qualquer outra origem) acesse os endpoints da API.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Aplica as regras de CORS a todos os endpoints da API (/**)
        registry.addMapping("/**")
                // Permite acesso de qualquer origem (seu frontend Angular)
                // Na produção, você trocaria o "*" pelo domínio específico do seu frontend.
                .allowedOrigins("*")
                // Permite todos os métodos HTTP que você usará (GET, POST, PUT, DELETE, etc.)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Permite todos os cabeçalhos
                .allowedHeaders("*");
    }
}