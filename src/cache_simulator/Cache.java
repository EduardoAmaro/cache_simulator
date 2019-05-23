package cache_simulator;

import java.util.ArrayList;

public class Cache {
    Integer noffset, nindice, ntag;
    
    ArrayList<ArrayList> conjuntos;

    public Cache(Integer nsets, Integer assoc, Integer bsize) {
        this.noffset = (int) (Math.log(bsize)/Math.log(2));
        this.nindice = (int) (Math.log(nsets)/Math.log(2));
        this.ntag = 32 - this.noffset - this.nindice;
    }
}
