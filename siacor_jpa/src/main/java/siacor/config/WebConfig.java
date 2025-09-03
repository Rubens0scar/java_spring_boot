package siacor.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import siacor.interceptor.ActivityInterceptor;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    // CORS (Cross-Origin Resource Sharing) mecanismo que permite a un navegador web realizar solicitudes a un dominio diferente del que sirvió la página web actual. 
    // Esto es útil cuando tu backend (API) y tu frontend (aplicación web) están en dominios diferentes.
    // Por ejemplo:
    //     Frontend: https://mi-frontend.com
    //     Backend: https://mi-backend.com
    // Sin CORS, el navegador bloquearía las solicitudes del frontend al backend por razones de seguridad.
    @Autowired
    private ActivityInterceptor activityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(activityInterceptor);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Eliminar el convertidor de XML si existe
        converters.removeIf(converter -> converter.getSupportedMediaTypes().stream()
                .anyMatch(mediaType -> mediaType.includes(MediaType.APPLICATION_XML)));
        
        // Asegurar que Jackson para JSON esté presente
        converters.add(new MappingJackson2HttpMessageConverter());

        // Agregar el convertidor para 'application/pdf'
        converters.add(new ByteArrayHttpMessageConverter());
    }
}
