package cache_simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Cache {

    private Integer noffset, nindice, ntag;
    private Integer assoc, nsets, bsize, polSubs;
    private Integer hit, miss_conflito, miss_compulsorio, miss_capacidade, miss_total;

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
                if (this.conjuntos.get(i).get(this.getIntIndice(this.getSplitIndice(endereco))).getTag() == this.getSplitTag(endereco)) {//verifica tag
                    flagMiss = false;
                    this.hit++;
                    if (polSubs == 2) {
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
            if (polSubs == 0) {//random
                Random gerador = new Random();
                int gerado = gerador.nextInt(this.conjuntos.size());
                
                //System.out.println(gerado);
                //System.out.println(this.getIntIndice(this.getSplitIndice(endereco)));
                
                this.escreveCache(gerado, this.getIntIndice(this.getSplitIndice(endereco)), this.getSplitTag(endereco));
                this.miss_conflito++;
                this.miss_total++;
            } else if (polSubs == 1 || polSubs == 2) {//fifo
                Integer sub = ordemSubstituicao.get(this.getIntIndice(this.getSplitIndice(endereco))).removeFirst();
                this.escreveCache(sub, this.getIntIndice(this.getSplitIndice(endereco)), this.getSplitTag(endereco));
                this.ordemSubstituicao.get(this.getIntIndice(this.getSplitIndice(endereco))).addLast(sub);
            }
        }
    }

    public void escreveCache(Integer indexConjunto, Integer indexBloco, String tag) {
        this.conjuntos.get(indexConjunto).get(indexBloco).setValidade(1);
        this.conjuntos.get(indexConjunto).get(indexBloco).setTag(tag);
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
        for (int i = 0; i < this.assoc; i++) {
            for (int j = 0; j < this.nsets; j++) {
                if (this.conjuntos.get(i).get(j).getValidade() == 0) {
                    return j;
                }
            }
        }
        return -1;
    }

    public String getSplitTag(String endereco) {
        return endereco.substring(0, this.ntag);
    }

    public String getSplitIndice(String endereco) {
        return endereco.substring(this.ntag, this.ntag + this.nindice);
    }

    private Integer getIntIndice(String indice) {
        return Integer.parseInt(indice, 2);
    }

    @Override
    public String toString() {
        String saida = "";
        for (int i = 0; i < this.assoc; i++) {
            System.out.println("----------------------------------------------------------");
            for (int j = 0; j < this.nsets; j++) {
                System.out.println(this.conjuntos.get(i).get(j).getTag() + "|");
            }
        }

        return saida;
    }
}
