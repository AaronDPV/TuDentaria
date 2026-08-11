package com.utp.tudentaria.controller;

import com.utp.tudentaria.model.Usuario;
import com.utp.tudentaria.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioRepository usuarioRepository;

    public PerfilController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String verPerfil(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String nombreUsuario = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findAll().stream()
                .filter(u -> u.getNombre().equals(nombreUsuario))
                .findFirst();

        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
        } else {
            model.addAttribute("usuario", new Usuario());
        }

        return "pages/perfil";
    }

    @PostMapping("/actualizar")
    public String actualizarDatos(@ModelAttribute("usuario") Usuario datosActualizados,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String nombreActualSesion = authentication.getName();
        Usuario usuarioExistente = usuarioRepository.findAll().stream()
                .filter(u -> u.getNombre().equals(nombreActualSesion))
                .findFirst()
                .orElse(null);

        if (usuarioExistente != null) {
            usuarioExistente.setNombre(datosActualizados.getNombre());
            usuarioExistente.setApellido(datosActualizados.getApellido());
            usuarioExistente.setEmail(datosActualizados.getEmail());

            usuarioRepository.save(usuarioExistente);
            redirectAttributes.addFlashAttribute("exito", "Tus datos personales se actualizaron correctamente.");
        }

        return "redirect:/perfil";
    }
}