package org.example.bookstore.init;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.config.properties.AppPropertyConfig;
import org.example.bookstore.dto.auth.PermissionDTO;
import org.example.bookstore.dto.auth.RoleDTO;
import org.example.bookstore.dto.auth.UserProfileDTO;
import org.example.bookstore.dto.auth.UserRegistrationDTO;
import org.example.bookstore.model.auth.Permission;
import org.example.bookstore.model.auth.Role;
import org.example.bookstore.model.bookstore.Book;
import org.example.bookstore.service.auth.PermissionService;
import org.example.bookstore.service.auth.RoleService;
import org.example.bookstore.service.auth.UserRegistrationService;
import org.example.bookstore.service.bookstore.BookService;
import org.example.bookstore.util.AppUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */


@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
@Profile("bookstore")
public class StartUpTask {
    private final AppPropertyConfig propertyConfig;
    private final RoleService<RoleDTO, Role> roleServiceImpl;
    private final PermissionService<PermissionDTO, Permission> permissionServiceImpl;
    private final UserRegistrationService<UserProfileDTO, UserRegistrationDTO> userRegistrationServiceImpl;
    private final BookService<Book> bookBookServiceImpl;

    @EventListener(ContextRefreshedEvent.class)
    public void runSystemStartUpTask() {
        addSystemPermissions();
        addSystemBooks();
    }

    private void addSystemPermissions() {
        File file = getSystemFile(propertyConfig.getSystemDefinedPermissions());
        Set<Permission> permissions;
        try {
            permissions = AppUtil.getObjectMapper().readValue(file, new TypeReference<>() {
            });
        } catch (IOException ignored) {
            log.trace("no update required");
            return;
        }
        if ("permissions.json".equals(file.getName())) {
            permissionServiceImpl.addSystemPermissions(permissions);
            roleServiceImpl.systemRolesChecks();
            userRegistrationServiceImpl.systemUsersChecks();
        }
    }

    private void addSystemBooks() {
        File file = getSystemFile(propertyConfig.getSystemBooks());
        Set<Book> books;
        try {
            books = AppUtil.getObjectMapper().readValue(file, new TypeReference<>() {
            });
        } catch (IOException ignored) {
            log.trace("no update required");
            return;
        }
        if ("books.json".equals(file.getName())) {
            bookBookServiceImpl.addSystemBooks(books);
        }
    }

    private File getSystemFile(String filePath) {
        Path path = Paths.get(filePath);
        return path.toFile();
    }

}
