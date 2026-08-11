package com.utp.tudentaria.controller;

import com.utp.tudentaria.model.Cita;
import com.utp.tudentaria.model.Usuario;
import com.utp.tudentaria.repository.CitaRepository;
import com.utp.tudentaria.repository.DoctorRepository;
import com.utp.tudentaria.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PublicController {

    private final UsuarioService usuarioService;
    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;

    public PublicController(UsuarioService usuarioService, CitaRepository citaRepository, DoctorRepository doctorRepository) {
        this.usuarioService = usuarioService;
        this.citaRepository = citaRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        if (!model.containsAttribute("cita")) {
            model.addAttribute("cita", new Cita());
        }
        return "index";
    }

    @PostMapping("/solicitar-cita")
    public String procesarCita(@Valid @ModelAttribute("cita") Cita cita,
                               BindingResult result,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        String referer = request.getHeader("Referer");
        boolean esContacto = referer != null && referer.contains("/contacto");

        if (result.hasErrors()) {
            model.addAttribute("cita", cita);
            model.addAttribute("errorCita", "Por favor, corrige los errores en el formulario de solicitud.");

            if (esContacto) {
                return "pages/contacto";
            }
            return "index";
        }

        citaRepository.save(cita);
        redirectAttributes.addFlashAttribute("exitoCita", "Tu solicitud de cita ha sido enviada con éxito. Nos comunicaremos contigo pronto.");

        if (esContacto) {
            return "redirect:/contacto";
        }
        return "redirect:/#contacto";
    }

    @GetMapping("/nosotros")
    public String nosotros(Model model) {
        model.addAttribute("doctores", doctorRepository.findAll());
        return "pages/nosotros";
    }

    @GetMapping("/servicios")
    public String servicios() { return "pages/servicios"; }

    @GetMapping("/blog")
    public String blog() { return "pages/blog"; }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        if (!model.containsAttribute("cita")) {
            model.addAttribute("cita", new Cita());
        }
        return "pages/contacto";
    }

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "pages/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registrado";
    }
}