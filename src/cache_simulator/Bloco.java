package cache_simulator;

public class Bloco {
    private Integer validade; //0 (vazio), 1(cheio)
    private String tag;
    
    public Bloco(){
        this.tag = "";
        this.validade = 0;
    }

    /**
     * @return the validade
     */
    public Integer getValidade() {
        return validade;
    }

    /**
     * @param validade the validade to set
     */
    public void setValidade(Integer validade) {
        this.validade = validade;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
