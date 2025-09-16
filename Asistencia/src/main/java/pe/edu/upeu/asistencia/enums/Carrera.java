package pe.edu.upeu.asistencia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public enum Carrera {
    SISTEMAS(Facultad.FIA,"Sistemas"),

    CIVIL(Facultad.FIA,"Civil"),

    ARQUITECTURA(Facultad.FIA,"Arquitectura"),

    EDUCACION(Facultad.FACIHED,"Educacion"),

    CONTABILIDAD(Facultad.FCE,"Contabilidades"),

    NUTRICION(Facultad.FCS,"Nutricion"),

    INTERNACIONAL(Facultad.GENERAL,"Internacional"),;

    private Facultad facultad;
    private String descripcion;

}
