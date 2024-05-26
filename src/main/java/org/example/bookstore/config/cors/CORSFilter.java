package org.example.bookstore.config.cors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Configuration
public class CORSFilter implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH"));
        config.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "Access-Control-Allow-Headers",
                        "Access-Control-Expose-Headers",
                        "Access-Control-Allow-Origin",
                        "Content-Disposition",
                        "Authorization",
                        " X-Requested-With"));
        config.addExposedHeader("Content-Disposition");
        return config;
    }

}
