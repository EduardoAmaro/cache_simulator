package cache_simulator;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Cache_simulator {

    public static void main(String[] args) {
        //args[0] = "4:10:1";
        //args[1] = "arqBinario1.dat";
        GeradorMemProg.createFiles();
        ArrayList<Integer> enderecos = new ArrayList();

        //String config=args[0];
        String arquivo = "arqBinario1.dat";

        //Integer nsets = Integer.parseInt(config.split(":")[0]);
        //Integer bsize = Integer.parseInt(config.split(":")[1]);
        //Integer assoc = Integer.parseInt(config.split(":")[2]);
        //System.out.println(nsets);
        //System.out.println(bsize);
        //System.out.println(assoc);
        //System.out.println(arquivo);
        
        InputStream input;
        try {
            input = new FileInputStream(arquivo);
            DataInputStream in = new DataInputStream(input);

            while (in.available() > 0) {

                int data = in.readInt();
                enderecos.add(data);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(enderecos.toString());
        System.out.println(enderecos.size());

    }

}

//cache_simulator<nsets_L1>:<bsize_L1>:<assoc_L1> arquivo_de_entrada
//java cache_simulator.java 4:10:1 arqBinario1.dat

//java -jar cache_simulator.jar 4:10:1 arqBinario1.dat
