package com.utp.tudentaria.controller;

import com.utp.tudentaria.model.Rol;
import com.utp.tudentaria.model.Usuario;
import com.utp.tudentaria.repository.RolRepository;
import com.utp.tudentaria.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUsuarioController(UsuarioRepository usuarioRepository,
                                  RolRepository rolRepository,
                                  PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("rolesDisponibles", rolRepository.findAll());
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new Usuario());
        }
        return "admin/usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario,
                                 @RequestParam("idRol") Long idRol,
                                 RedirectAttributes redirectAttributes) {

        if (usuario.getId() == null) {
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado.");
                redirectAttributes.addFlashAttribute("usuario", usuario);
                return "redirect:/admin/usuarios";
            }
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            Usuario existente = usuarioRepository.findById(usuario.getId()).orElse(null);
            if (existente != null) {
                if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                    usuario.setPassword(existente.getPassword());
                } else {
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                }
            }
        }

        Rol rolSeleccionado = rolRepository.findById(idRol).orElse(null);
        if (rolSeleccionado != null) {
            HashSet<Rol> roles = new HashSet<>();
            roles.add(rolSeleccionado);
            usuario.setRoles(roles);
        }

        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("exito", "Usuario guardado correctamente.");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("exito", "Usuario eliminado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar este usuario.");
        }
        return "redirect:/admin/usuarios";
    }
}