package in.ongrid.fitnesstracker.config;

import in.ongrid.fitnesstracker.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private JwtInterceptor jwtInterceptor;

    @Autowired
    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the JWT interceptor
        registry.addInterceptor(jwtInterceptor)
                .excludePathPatterns("/api/users/**") // Exclude public endpoints
                .addPathPatterns("/api/**");
    }
}