package com.utp.tudentaria.controller;

import com.utp.tudentaria.model.Doctor;
import com.utp.tudentaria.repository.DoctorRepository;
import com.utp.tudentaria.service.UploadFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
@RequestMapping("/admin/doctores")
public class AdminDoctorController {

    private final DoctorRepository doctorRepository;
    private final UploadFileService uploadFileService;

    public AdminDoctorController(DoctorRepository doctorRepository, UploadFileService uploadFileService) {
        this.doctorRepository = doctorRepository;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping
    public String listarDoctores(Model model) {
        model.addAttribute("doctores", doctorRepository.findAll());
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new Doctor());
        }
        return "admin/doctores";
    }

    @PostMapping("/guardar")
    public String guardarDoctor(@ModelAttribute("doctor") Doctor doctor,
                                @RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            if (doctor.getId() == null) {
                if (!file.isEmpty()) {
                    String nombreImagen = uploadFileService.guardarImagen(file);
                    doctor.setImagen(nombreImagen);
                }
            } else {
                Doctor existente = doctorRepository.findById(doctor.getId()).orElse(null);
                if (existente != null) {
                    if (!file.isEmpty()) {
                        uploadFileService.eliminarImagen(existente.getImagen());
                        String nombreImagen = uploadFileService.guardarImagen(file);
                        doctor.setImagen(nombreImagen);
                    } else {
                        doctor.setImagen(existente.getImagen());
                    }
                }
            }
            doctorRepository.save(doctor);
            redirectAttributes.addFlashAttribute("exito", "Doctor guardado correctamente.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la imagen.");
        }
        return "redirect:/admin/doctores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDoctor(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor != null) {
            uploadFileService.eliminarImagen(doctor.getImagen());
            doctorRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("exito", "Doctor eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el doctor.");
        }
        return "redirect:/admin/doctores";
    }
}