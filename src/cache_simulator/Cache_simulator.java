package cache_simulator;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Cache_simulator {

    public static void main(String[] args) {
        GeradorMemProg.createFiles();
        ArrayList<String> enderecos = new ArrayList<>();

        String config = args[0];
        String arquivo = args[1];

        Integer nsets = Integer.parseInt(config.split(":")[0]);
        Integer bsize = Integer.parseInt(config.split(":")[1]);
        Integer assoc = Integer.parseInt(config.split(":")[2]);
        
        System.out.println(nsets);
        System.out.println(bsize);
        System.out.println(assoc);
        System.out.println(arquivo);

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
        System.out.println(enderecos.size());
        
        Cache cache = new Cache(nsets,assoc,bsize);
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