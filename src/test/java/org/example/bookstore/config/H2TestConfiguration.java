package org.example.bookstore.config;

import org.example.bookstore.config.properties.AppPropertyConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 * N/B: To run test do the following
 * Set active profile to 'bookstore' and 'test'
 */

@Configuration
public class H2TestConfiguration {
    AppPropertyConfig propertyConfig;
    public H2TestConfiguration(AppPropertyConfig propertyConfig) {
        // Ensure the tests use H2 database, or fail otherwise

        if (!"jdbc:h2:mem:bookstore-test".equalsIgnoreCase(propertyConfig.getDatabaseUrl())) {
            throw new IllegalArgumentException("Tests must use H2 database for data source URL.");
        }
    }
}
