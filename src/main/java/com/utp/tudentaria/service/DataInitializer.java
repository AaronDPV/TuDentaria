package com.utp.tudentaria.service;

import com.utp.tudentaria.model.Rol;
import com.utp.tudentaria.model.Usuario;
import com.utp.tudentaria.repository.RolRepository;
import com.utp.tudentaria.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Rol adminRol = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("ROLE_ADMIN");
                    return rolRepository.save(nuevoRol);
                });

        rolRepository.findByNombre("ROLE_USER")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("ROLE_USER");
                    return rolRepository.save(nuevoRol);
                });

        if (!usuarioRepository.findByEmail("admin@tudentaria.com").isPresent()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("TuDentaria");
            admin.setEmail("admin@tudentaria.com");
            admin.setPassword(passwordEncoder.encode("123456"));

            java.util.Set<Rol> rolesAdmin = new java.util.HashSet<>();
            rolesAdmin.add(adminRol);
            admin.setRoles(rolesAdmin);

            usuarioRepository.save(admin);
        }
    }
}