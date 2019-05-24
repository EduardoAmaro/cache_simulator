package cache_simulator;

import java.util.ArrayList;

public class Cache {
    Integer noffset, nindice, ntag;
    ArrayList<ArrayList<Bloco>> conjuntos;

    public Cache(Integer nsets, Integer assoc, Integer bsize) {
        this.noffset = (int) (Math.log(bsize)/Math.log(2));
        this.nindice = (int) (Math.log(nsets)/Math.log(2));
        this.ntag = 32 - this.noffset - this.nindice;
        this.conjuntos = new ArrayList<>(assoc);
        
        for(int i=0;i<assoc;i++){
            ArrayList<Bloco> blocos = new ArrayList<>();
            for(int j=0;j<nsets;j++){    
                Bloco bloco = new Bloco();
                blocos.add(bloco);
            }
            this.conjuntos.add(blocos);
        }
        
        for(int i=0;i<assoc;i++){
            System.out.print("[");
            for(int j=0;j<nsets;j++){    
                System.out.print(j + ",");
            }
            System.out.print("]\n");
        }
    }
}
