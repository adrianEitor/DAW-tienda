public class Usuario {
    private int id;
    private String email;
    private String password;
    private String nombre;
    private String numeroTarjeta;

    public Usuario(int id, String email, String password, String nombre, String numeroTarjeta) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.numeroTarjeta = numeroTarjeta;
    }

    public Usuario(String email, String password, String nombre, String numeroTarjeta) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.numeroTarjeta = numeroTarjeta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
}
