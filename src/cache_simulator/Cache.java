package cache_simulator;

import java.util.ArrayList;

public class Cache {
    private Integer noffset, nindice, ntag;
    private ArrayList<ArrayList<Bloco>> conjuntos;

    public Cache(Integer nsets, Integer assoc, Integer bsize) {
        this.noffset = (int) (Math.log(bsize)/Math.log(2));
        this.nindice = (int) (Math.log(nsets)/Math.log(2));
        this.ntag = 32 - this.noffset - this.nindice;
        this.conjuntos = new ArrayList<>(assoc);
        
        for(int i=0;i<assoc;i++){
            ArrayList<Bloco> blocos = new ArrayList<>();
            for(int j=0;j<nsets/assoc;j++){    
                Bloco bloco = new Bloco();
                blocos.add(bloco);
            }
            this.conjuntos.add(blocos);
        }
        
        for(int i=0;i<assoc;i++){
            System.out.print("[");
            for(int j=0;j<nsets/assoc;j++){    
                System.out.print(j + ",");
            }
            System.out.print("]\n");
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
    
    public boolean busca(String tag, String indice, String offset){
        
    }
}
