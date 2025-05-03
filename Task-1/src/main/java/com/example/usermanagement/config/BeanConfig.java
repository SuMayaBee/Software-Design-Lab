package com.example.usermanagement.config;

import com.example.usermanagement.application.RoleService;
import com.example.usermanagement.application.UserService;
import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.application.interfaces.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    // Spring automatically finds the implementations of UserRepository and RoleRepository 
    // (UserRepositoryAdapter, RoleRepositoryAdapter because they are annotated with @Component)
    // and injects them here.

    @Bean
    public UserService userService(UserRepository userRepository, RoleRepository roleRepository) {
        // Inject the repository interfaces (implemented by adapters) into the service
        return new UserService(userRepository, roleRepository);
    }

    @Bean
    public RoleService roleService(RoleRepository roleRepository) {
        // Inject the repository interface (implemented by adapter) into the service
        return new RoleService(roleRepository);
    }
    
    // Note: The repository adapters (UserRepositoryAdapter, RoleRepositoryAdapter)
    // and the JpaRepositories (UserJpaRepository, RoleJpaRepository) are automatically 
    // detected by Spring Boot's component scanning and auto-configuration, 
    // so we don't usually need to declare them as @Beans explicitly here.
} 