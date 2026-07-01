package Util;

public enum Veredito {
    REJEITADO(0),
    FRACAMENTE_REJEITADO(1),
    FRACAMENTE_ACEITO(2),
    ACEITO(3);

    private final int peso;

    Veredito(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }

    public static Veredito fromString(String texto) {
        String t = texto.trim().toLowerCase();
        return switch (t) {
            case "rejeitado" -> REJEITADO;
            case "fracamente rejeitado" -> FRACAMENTE_REJEITADO;
            case "fracamente aceito" -> FRACAMENTE_ACEITO;
            case "aceito" -> ACEITO;
            default -> throw new IllegalArgumentException("Veredito inválido: " + texto);
        };
    }

    public boolean isAprovado() {
        return this == ACEITO || this == FRACAMENTE_ACEITO;
    }
}