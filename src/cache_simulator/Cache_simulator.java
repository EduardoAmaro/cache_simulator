package cache_simulator;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
    cache_simulator <nsets_L1>:<bsize_L1>:<assoc_L1>:<polsubst_L1> arquivo_de_entrada <gerar>:<range>:<qtdenderecos>
    
    rodar na pasta src/
    javac cache_simulator/Cache_simulator.java
    java cache_simulator.Cache_simulator 256:4:1:0 arqBinario1.dat 1:1000:10000

 */
public class Cache_simulator {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Parâmetros incorretos");
        } else {
            ArrayList<String> enderecos = new ArrayList<>();

            String config = args[0];
            String arquivo = args[1];
            String gerMem = args[2];

            //parametros cache
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

            //parametros gerador endereços memória
            Integer gerar = Integer.parseInt(gerMem.split(":")[0]);//0 - não gerar 1 - gerar
            Integer range = Integer.parseInt(gerMem.split(":")[1]);
            Integer qtdEnderecos = Integer.parseInt(gerMem.split(":")[2]);

            GeradorMemProg gerador = new GeradorMemProg(range, qtdEnderecos);
            if (gerar == 1) {
                gerador.createFiles();
            }

            //leitura arquivo binário
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

            Cache cacheL1 = new Cache(nsets, assoc, bsize, polSubs);

            for (int i = 0; i < enderecos.size(); i++) {
                cacheL1.acessaCache(enderecos.get(i));
            }

            System.out.println("CONFIGURAÇÕES DA CACHE\n");
            System.out.println("Número de conjuntos: " + cacheL1.getNsets());
            System.out.println("Tamanho do bloco: " + cacheL1.getBsize() + " bytes");
            System.out.print("Associatividade: ");
            if (cacheL1.getAssoc() == 1) {
                System.out.println("Mapeamento Direto");
            } else if (cacheL1.getNsets() == 1 && cacheL1.getAssoc() >= 1) {
                System.out.println("Totalmente Associativa");
            } else {
                System.out.println("Conjunto Associativo de " + cacheL1.getAssoc() + " vias");
            }

            System.out.print("Política de substituição: ");
            if (cacheL1.getPolSubs() == 0) {
                System.out.println("Random");
            } else if (cacheL1.getPolSubs() == 1) {
                System.out.println("Fifo");
            } else if (cacheL1.getPolSubs() == 2) {
                System.out.println("LRU");
            }
            System.out.println("Tamanho da cache: " + 
            (double) ((cacheL1.getAssoc() * cacheL1.getNsets() * (cacheL1.getBsize() * 8 + cacheL1.getNtag() + 1))/8) + " bytes");
            System.out.println("\n");

            System.out.println("RESULTADOS DA CACHE\n");
            System.out.println("Número de acessos: " + enderecos.size());
            System.out.println("Miss compulsório: " + cacheL1.getMiss_compulsorio());
            System.out.println("Miss de conflito: " + cacheL1.getMiss_conflito());
            System.out.println("Miss de capacidade: " + cacheL1.getMiss_capacidade());
            System.out.println("Miss total: " + cacheL1.getMiss_total());
            System.out.println("Total de hits:" + cacheL1.getHit());

            String miss_rate = Cache_simulator.formatarDecimal(((double) cacheL1.getMiss_total() / (double) enderecos.size()) * 100);
            String hit_rate = Cache_simulator.formatarDecimal(((double) cacheL1.getHit() / (double) enderecos.size()) * 100);
            System.out.println("\nMiss Rate: " + miss_rate + "%");
            System.out.println("Hit Rate: " + hit_rate + "%");

        }

    }

    public static String preencheBinario(String endereco) {
        while (endereco.length() < 32) {
            endereco = "0" + endereco;
        }
        return endereco;
    }

    public static String formatarDecimal(double saldo) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String saida = df.format(saldo);
        return saida;
    }

}
