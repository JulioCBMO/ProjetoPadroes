package FactoryMethod;

import java.util.HashSet;
import java.util.Set;

public class AreaTematica {
    private static Set<String> areasCadastradas = new HashSet<>();

    public static void cadastrarArea(String area) {
        areasCadastradas.add(area.toLowerCase().trim());
    }

    public static boolean areaExiste(String area) {
        return areasCadastradas.contains(area.toLowerCase().trim());
    }

    public static Set<String> getAreasCadastradas() {
        return new HashSet<>(areasCadastradas);
    }

    public static void limparAreas() {
        areasCadastradas.clear();
    }
}