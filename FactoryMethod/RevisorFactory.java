package FactoryMethod;

public class RevisorFactory  implements UsuarioFactory {
    @Override
    public Usuario criar(String email, String senha, String instituicao) {
        return new Revisor(email, senha, instituicao);
    }
}
