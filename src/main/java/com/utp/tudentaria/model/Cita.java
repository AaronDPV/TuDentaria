package com.utp.tudentaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String estado = "PENDIENTE";

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    private String apellido;

    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Size(min = 7, max = 20, message = "El teléfono debe tener entre 7 y 20 caracteres.")
    @Column(name = "telephone")
    private String telephone;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "Por favor, introduce un correo electrónico válido.")
    private String email;

    @NotBlank(message = "El motivo de la visita es obligatorio.")
    @Size(max = 150, message = "El motivo no puede exceder los 150 caracteres.")
    private String motivo;

    @NotNull(message = "La fecha de la cita es obligatoria.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "La fecha de la cita no puede ser en el pasado.")
    private LocalDate fecha;

    @Column(columnDefinition = "TEXT")
    private String notas;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}