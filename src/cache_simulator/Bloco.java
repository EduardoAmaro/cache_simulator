package cache_simulator;

public class Bloco {
    Integer validade; //-1 [inicial (miss compulsório)], 0 (vazio), 1(cheio)
    String tag;
    
    public Bloco(){
        this.tag = "";
        this.validade = -1;
    }
}
