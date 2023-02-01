package esan.menufragmentos;

public class User {
    private int id, telefone;
    private String nome, email;
    private boolean guardar;

    public User (int id, int telefone, String nome, String email, boolean guardar) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.guardar = guardar;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefone() {
        return telefone;
    }

    public boolean getGuardar() {
        return guardar;
    }
}
