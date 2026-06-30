package main.java.Observer;

import main.java.StateArtigo.Artigo;


//  Define o contrato para quem deseja escutar as mudanças de estado do Artigo.
 
public interface ArtigoObserver {
    
    
      //Método acionado automaticamente quando o artigo muda de estado.
    
    void onEstadoAlterado(Artigo artigo);
}