public class Gestion {
    private String nombre;
    private String estudio;

    public Gestion(String nombre, String estudio){
        this.nombre = nombre;
        this.estudio = estudio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstudio() {
        return this.estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public String toString() {
        return "El Alumno " + this.nombre + " esta estudiando " + this.estudio + ".";
    }
}
