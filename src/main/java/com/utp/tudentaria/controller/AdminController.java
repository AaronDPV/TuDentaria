package com.utp.tudentaria.controller;

import com.utp.tudentaria.model.Usuario;
import com.utp.tudentaria.repository.CitaRepository;
import com.utp.tudentaria.repository.DoctorRepository;
import com.utp.tudentaria.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;

    public AdminController(UsuarioRepository usuarioRepository,
                           CitaRepository citaRepository,
                           DoctorRepository doctorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.citaRepository = citaRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        long totalUsuarios = usuarioRepository.count();
        long totalDoctores = doctorRepository.count();
        long citasPendientes = citaRepository.countByEstadoIgnoreCase("PENDIENTE");

        String nombreAdmin = usuarioRepository.findByEmail(authentication.getName())
                .map(Usuario::getNombre)
                .orElse("Administrador");

        model.addAttribute("nombreAdmin", nombreAdmin);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalDoctores", totalDoctores);
        model.addAttribute("citasPendientes", citasPendientes);

        return "admin/dashboard";
    }
}