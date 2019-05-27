package cache_simulator;

import java.util.ArrayList;

public class Cache {

    private Integer noffset, nindice, ntag;
    private Integer hit, miss_conflito, miss_compulsorio, miss_capacidade, miss_total;

    private ArrayList<ArrayList<Bloco>> conjuntos;

    public Cache(Integer nsets, Integer assoc, Integer bsize) {
        this.noffset = (int) (Math.log(bsize) / Math.log(2));
        this.nindice = (int) (Math.log(nsets) / Math.log(2));
        this.ntag = 32 - this.noffset - this.nindice;
        this.conjuntos = new ArrayList<>(assoc);

        for (int i = 0; i < assoc; i++) {
            ArrayList<Bloco> blocos = new ArrayList<>();
            for (int j = 0; j < nsets / assoc; j++) {
                Bloco bloco = new Bloco();
                blocos.add(bloco);
            }
            this.conjuntos.add(blocos);
        }

        /*
        for(int i=0;i<assoc;i++){
            System.out.print("[");
            for(int j=0;j<nsets/assoc;j++){    
                System.out.print(j + ",");
            }
            System.out.print("]\n");
        }
         */
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
        for (int i = 0; i < this.conjuntos.size(); i++) {
            if (this.conjuntos.get(i).get(this.getIntIndice(endereco)).getValidade() == 0) {//sem informação no bloco miss compulsório
                this.escreveCache(i, this.getIntIndice(endereco), this.getSplitTag(endereco));
                this.miss_compulsorio++;
                this.miss_total++;
                return;
            } else if (this.conjuntos.get(i).get(this.getIntIndice(endereco)).getValidade() == 1) {//encontrou informação no bloco
                if (this.conjuntos.get(i).get(this.getIntIndice(endereco)).getTag() == this.getSplitTag(endereco)) {//verifica tag
                    this.hit++;
                    return;
                } else {
                    
                }
            } else if (this.conjuntos.get(i).get(this.getIntIndice(endereco)).getValidade() == -1) {
                this.miss_compulsorio++;
                this.miss_total++;
                this.escreveCache(i, this.getIntIndice(endereco), this.getSplitTag(endereco));
                return;
            } else {

            }

        }
    }

    public void escreveCache(Integer indexConjunto, Integer indexBloco, String tag) {
        this.conjuntos.get(indexConjunto).get(indexBloco).setValidade(1);
        this.conjuntos.get(indexConjunto).get(indexBloco).setTag(tag);
    }

    public boolean busca(String endereco) {
        for (int i = 0; i < this.conjuntos.size(); i++) {
            if (this.conjuntos.get(i).get(this.getIntIndice(endereco)).getTag() == this.getSplitTag(endereco)
                    & this.conjuntos.get(i).get(this.getIntIndice(endereco)).getValidade() == 1) {
                return true;
            }
        }
        return false;
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
}
