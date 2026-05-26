package co.edu.uniremington.Dsierra.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/dashboard",
                        "/admin",
                        "/productos",
                        "/categorias",
                        "/ventas",
                        "/stock",
                        "/api/productos/**",
                        "/api/usuarios/**",
                        "/api/upload/**"
                );
    }
}
