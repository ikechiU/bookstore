package org.example.bookstore.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "bookstore")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppPropertyConfig {
    public String databaseUrl = "";
    public String databaseUsername = "";
    public String databasePassword = "";
    public String databaseDriver = "";
    public String databaseMaxActive = "1000";
    public String databaseHibernateDialect = "";
    public String securityJWTKeyStorePath = "";
    public String securityJWTKeyStorePassword = "";
    public String securityJWTKeyAlias = "";
    public String securityJWTPrivateKeyPassphrase = "";
    public String systemDefinedPermissions = null;
    public String systemBooks = null;
    public String systemDefaultUser = "";
    public String systemDefaultAdmin = "";
    public String systemDefaultSuperAdmin = "";
    public String systemDefaultPassword = "";
}