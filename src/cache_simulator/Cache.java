package cache_simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Cache {

    private Integer noffset, nindice, ntag;
    private Integer assoc, nsets, bsize, polSubs;
    private Integer hit, miss_conflito, miss_compulsorio, miss_capacidade, miss_total;
    private Integer controleCapacidade;

    private ArrayList<ArrayList<Bloco>> conjuntos;
    private ArrayList<LinkedList<Integer>> ordemSubstituicao;

    public Cache(Integer nsets, Integer assoc, Integer bsize, Integer polSubs) {
        this.assoc = assoc;
        this.bsize = bsize;
        this.nsets = nsets;
        this.polSubs = polSubs;

        this.noffset = (int) (Math.log(bsize) / Math.log(2));
        this.nindice = (int) (Math.log(nsets) / Math.log(2));
        this.ntag = 32 - this.noffset - this.nindice;

        this.hit = 0;
        this.miss_conflito = 0;
        this.miss_compulsorio = 0;
        this.miss_capacidade = 0;
        this.miss_total = 0;
        this.controleCapacidade = 0;

        this.conjuntos = new ArrayList<>(assoc);
        this.ordemSubstituicao = new ArrayList<>(nsets);

        for (int i = 0; i < assoc; i++) {
            ArrayList<Bloco> blocos = new ArrayList<>();
            for (int j = 0; j < nsets; j++) {
                Bloco bloco = new Bloco();
                blocos.add(bloco);
            }
            this.conjuntos.add(blocos);
        }

        for (int i = 0; i < nsets; i++) {
            LinkedList<Integer> listaSubs = new LinkedList<>();
            this.ordemSubstituicao.add(listaSubs);
        }

    }

    public void acessaCache(String endereco) {
        boolean flagMiss = false;
        for (int i = 0; i < this.conjuntos.size(); i++) {
            if (this.conjuntos.get(i).get(this.getIntIndice(this.getSplitIndice(endereco))).getValidade() == 0) {//sem informação no bloco miss compulsório
                this.escreveCache(i, this.getIntIndice(this.getSplitIndice(endereco)), this.getSplitTag(endereco));
                this.ordemSubstituicao.get(this.getIntIndice(this.getSplitIndice(endereco))).addLast(i);
                this.miss_compulsorio++;
                this.miss_total++;
                flagMiss = false;
                return;

            } else if (this.conjuntos.get(i).get(this.getIntIndice(this.getSplitIndice(endereco))).getValidade() == 1) {//encontrou informação no bloco
                if (this.conjuntos.get(i).get(this.getIntIndice(this.getSplitIndice(endereco))).getTag().equals(this.getSplitTag(endereco))) {//verifica tag      
                    flagMiss = false;
                    this.hit++;
                    if (getPolSubs() == 2) {//lru
                        int lru = ordemSubstituicao.get(this.getIntIndice(this.getSplitIndice(endereco))).removeFirst();
                        this.ordemSubstituicao.get(this.getIntIndice(this.getSplitIndice(endereco))).addLast(lru);
                    }
                    return;
                } else {
                    flagMiss = true;
                }
            }
        }

        if (flagMiss == true) {
            if (getPolSubs() == 0) {//random
                Random gerador = new Random();
                int gerado = gerador.nextInt(this.conjuntos.size());
                this.escreveCache(gerado, this.getIntIndice(this.getSplitIndice(endereco)), this.getSplitTag(endereco));
            } else if (getPolSubs() == 1 || getPolSubs() == 2) {//fifo e lru
                Integer sub = ordemSubstituicao.get(this.getIntIndice(this.getSplitIndice(endereco))).removeFirst();
                this.escreveCache(sub, this.getIntIndice(this.getSplitIndice(endereco)), this.getSplitTag(endereco));
                this.ordemSubstituicao.get(this.getIntIndice(this.getSplitIndice(endereco))).addLast(sub);
            }

            if (this.getNsets() == 1 && this.getAssoc() >= 1) {//totalmente associativo não tem miss conflito
                this.miss_capacidade++;
            } else {
                if (this.controleCapacidade >= this.getAssoc() * this.getNsets()) {
                    this.miss_capacidade++;
                } else {
                    this.miss_conflito++;
                }
            }
            this.miss_total++;
        }
    }

    public void escreveCache(Integer indexConjunto, Integer indexBloco, String tag) {
        this.conjuntos.get(indexConjunto).get(indexBloco).setValidade(1);
        this.conjuntos.get(indexConjunto).get(indexBloco).setTag(tag);
        this.controleCapacidade++;
    }

    public boolean busca(String endereco) {
        for (int i = 0; i < this.conjuntos.size(); i++) {
            if (this.conjuntos.get(i).get(this.getIntIndice(this.getSplitIndice(endereco))).getTag().equals(this.getSplitTag(endereco))
                    && this.conjuntos.get(i).get(this.getIntIndice(this.getSplitIndice(endereco))).getValidade() == 1) {
                return true;
            }
        }
        return false;
    }

    public Integer estaCheia() {//-1 lista cheia || índice de primeiro local vazio
        for (int i = 0; i < this.getAssoc(); i++) {
            for (int j = 0; j < this.getNsets(); j++) {
                if (this.conjuntos.get(i).get(j).getValidade() == 0) {
                    return j;
                }
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String saida = "";
        for (int i = 0; i < this.getAssoc(); i++) {
            System.out.println("--------------------------------");
            for (int j = 0; j < this.getNsets(); j++) {
                System.out.println(this.conjuntos.get(i).get(j).getTag());
                if (this.conjuntos.get(i).get(j).getTag() == "") {
                    System.out.println("********************************");
                }
            }
        }

        return saida;
    }
    
    public String getSplitTag(String endereco) {
        return endereco.substring(0, this.ntag);
    }

    public String getSplitIndice(String endereco) {
        if (this.nindice == 0) {
            return "0";
        }
        return endereco.substring(this.ntag, this.ntag + this.nindice);
    }

    private Integer getIntIndice(String indice) {
        return Integer.parseInt(indice, 2);
    }

    /**
     * @return the miss_conflito
     */
    public Integer getMiss_conflito() {
        return miss_conflito;
    }

    /**
     * @return the miss_compulsorio
     */
    public Integer getMiss_compulsorio() {
        return miss_compulsorio;
    }

    /**
     * @return the miss_capacidade
     */
    public Integer getMiss_capacidade() {
        return miss_capacidade;
    }

    /**
     * @return the miss_total
     */
    public Integer getMiss_total() {
        return miss_total;
    }

    /**
     * @return the hit
     */
    public Integer getHit() {
        return hit;
    }

    /**
     * @return the polSubs
     */
    public Integer getPolSubs() {
        return polSubs;
    }

    /**
     * @param polSubs the polSubs to set
     */
    public void setPolSubs(Integer polSubs) {
        this.polSubs = polSubs;
    }

    /**
     * @return the assoc
     */
    public Integer getAssoc() {
        return assoc;
    }

    /**
     * @param assoc the assoc to set
     */
    public void setAssoc(Integer assoc) {
        this.assoc = assoc;
    }

    /**
     * @return the nsets
     */
    public Integer getNsets() {
        return nsets;
    }

    /**
     * @param nsets the nsets to set
     */
    public void setNsets(Integer nsets) {
        this.nsets = nsets;
    }

    /**
     * @return the bsize
     */
    public Integer getBsize() {
        return bsize;
    }

    /**
     * @param bsize the bsize to set
     */
    public void setBsize(Integer bsize) {
        this.bsize = bsize;
    }

    /**
     * @return the noffset
     */
    public Integer getNoffset() {
        return noffset;
    }

    /**
     * @param noffset the noffset to set
     */
    public void setNoffset(Integer noffset) {
        this.noffset = noffset;
    }

    /**
     * @return the nindice
     */
    public Integer getNindice() {
        return nindice;
    }

    /**
     * @param nindice the nindice to set
     */
    public void setNindice(Integer nindice) {
        this.nindice = nindice;
    }

    /**
     * @return the ntag
     */
    public Integer getNtag() {
        return ntag;
    }

    /**
     * @param ntag the ntag to set
     */
    public void setNtag(Integer ntag) {
        this.ntag = ntag;
    }

}
