package org.example.bookstore.config.database;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.bookstore.util.AppUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Configuration
@RequiredArgsConstructor
public class EntityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String author = "SYSTEM";
        String loggedInUsername = AppUtil.getLoggedInUsername();
        if (!StringUtils.isEmpty(loggedInUsername)) author = loggedInUsername;

        return Optional.of(author);
    }

}
