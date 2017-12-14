package practica4;

public class Degree {
	private final String identificador;
	private String nombre;
	
	public String getKey() {
		return identificador;
	}

	public String getValue() {
		return nombre;
	}

	public void setValue(String value) {
		this.nombre = value;
	}
	public Degree(String idDegree, String name) {
		this.identificador=idDegree;
		this.nombre=name;
	}
}
