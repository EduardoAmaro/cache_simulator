package cache_simulator;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//faltando:
//implementação da escrita em posição seguinte vazia - totalmente associativa - miss
//retorno e controle dos misses e hits
//segundo nível de cache (se der)

public class Cache_simulator {

    public static void main(String[] args) {
        GeradorMemProg.createFiles();
        ArrayList<String> enderecos = new ArrayList<>();

        String config = args[0];
        String arquivo = args[1];

        Integer nsets = Integer.parseInt(config.split(":")[0]);
        Integer bsize = Integer.parseInt(config.split(":")[1]);
        Integer assoc = Integer.parseInt(config.split(":")[2]);
        Integer polSubs = Integer.parseInt(config.split(":")[3]);//0 - random 1 - fifo 2 - lru

        if (nsets == 0 || bsize == 0 || assoc == 0) {//default
            nsets = 256;
            bsize = 4;
            assoc = 1;
            polSubs = 0;
        }

        InputStream input;
        try {
            input = new FileInputStream(arquivo);
            DataInputStream in = new DataInputStream(input);

            while (in.available() > 0) {
                int data = in.readInt();

                String endereco = Integer.toBinaryString(data);

                if (endereco.length() < 32) {
                    endereco = Cache_simulator.preencheBinario(endereco);
                }
                enderecos.add(endereco);
            }

            in.close();

        } catch (IOException e) {
            //
        }

        for (int i = 0; i < enderecos.size(); i++) {
            System.out.println(enderecos.get(i));
        }

        //System.out.println(enderecos.size());
        Cache cacheL1 = new Cache(nsets, assoc, bsize, polSubs);

        //teste splits e tamanhos
        System.out.println("\n\n");
        System.out.println(cacheL1.getNtag() + " " + cacheL1.getNindice() + " " + cacheL1.getNoffset());
        System.out.println(enderecos.get(0));
        System.out.print(cacheL1.getSplitTag(enderecos.get(0)) + "|");
        System.out.println(cacheL1.getSplitIndice(enderecos.get(0)));

        for (int i = 0; i < enderecos.size(); i++) {
            cacheL1.acessaCache(enderecos.get(i));
        }

        System.out.println(cacheL1.busca(enderecos.get(0)));

        System.out.println("--------------------------------------");
        System.out.println(cacheL1.toString());

    }

    public static String preencheBinario(String endereco) {
        while (endereco.length() < 32) {
            endereco = "0" + endereco;
        }
        return endereco;
    }

    /*
    cache_simulator<nsets_L1>:<bsize_L1>:<assoc_L1> arquivo_de_entrada
    
    rodar na pasta src/
    javac cache_simulator/Cache_simulator.java
    java cache_simulator.Cache_simulator 256:64:4 arqBinario1.dat
     */
}
