package cache_simulator;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Cache_simulator {

    public static void main(String[] args) {
        
        GeradorMemProg.createFiles();
        
        String config=args[0];
        String arquivo=args[1];
        
        Integer nsets = Integer.parseInt(config.split(":")[0]);
        Integer bsize = Integer.parseInt(config.split(":")[1]);
        Integer assoc = Integer.parseInt(config.split(":")[2]);
        
        System.out.println(nsets);
        System.out.println(bsize);
        System.out.println(assoc);
        System.out.println(arquivo);
        
        try{
            FileReader arq = new FileReader(arquivo);
            BufferedReader buffer = new BufferedReader(arq);
            String linha = "";
            
            while((linha=buffer.readLine()) != null){
                linha.split(" ");
            }
            
        }catch (IOException e){
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        
        
        
    }
    
}

//cache_simulator<nsets_L1>:<bsize_L1>:<assoc_L1> arquivo_de_entrada
//java cache_simulator.java 4:10:1 arquivo_de_entrada
//java cache_simulator.java 4:10:1 arqBinario1
