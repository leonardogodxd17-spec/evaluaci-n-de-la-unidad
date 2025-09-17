package pe.edu.upeu.asistencia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Carrera {
    SISTEMAS(Facultad.FIA, "Sistemas"),
    CIVIL(Facultad.FIA,  "Civil"),

    ADMINISTRACION(Facultad.FCE,  "Administración"),

    NUTRICION(Facultad.FCS, "Nutrición"),

    EDUCACION(Facultad.FACIHED, "Educación"),

    GENERAL(Facultad.GENERAL, "General"),;

    private Facultad facultad;
    private String descripcion;

}
