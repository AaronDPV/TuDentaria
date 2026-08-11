package com.utp.tudentaria.service;

import com.utp.tudentaria.model.Rol;
import com.utp.tudentaria.model.Usuario;
import com.utp.tudentaria.repository.RolRepository;
import com.utp.tudentaria.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Rol rolUser = rolRepository.findByNombre("ROLE_USER").orElse(null);
        if (rolUser != null) {
            usuario.getRoles().add(rolUser);
        }

        return usuarioRepository.save(usuario);
    }
}