package cache_simulator;

import java.io.*;
import java.util.Random;

public class GeradorMemProg {
    private Integer range;
    private Integer qtdEnderecos;
    
    public GeradorMemProg(Integer range, Integer qtdEnderecos){
        this.range = range;
        this.qtdEnderecos = qtdEnderecos;
    }
    
    public void createFiles() {
        try {
            File file = new File("arqTexto1.txt");
            PrintWriter outFile = new PrintWriter(new FileWriter(file));
            FileOutputStream outFileBin = new FileOutputStream("arqBinario1.dat");
            DataOutputStream out = new DataOutputStream(outFileBin);

            int numGerado = 0;
            Random r = new Random();
            
            for (int i = 0; i < getQtdEnderecos(); i++) {
                numGerado = r.nextInt(getRange());
                outFile.println(numGerado);
                out.writeInt(numGerado);
            }
            
            outFile.close();
            outFileBin.close();
        } catch (FileNotFoundException exception) {
            System.out.println("Arquivo nao encontrado.");
        } catch (IOException exception) {
            System.out.println("Erro de I/O: " + exception);
        }
    }

    /**
     * @return the range
     */
    public Integer getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(Integer range) {
        this.range = range;
    }

    /**
     * @return the qtdEnderecos
     */
    public Integer getQtdEnderecos() {
        return qtdEnderecos;
    }

    /**
     * @param qtdEnderecos the qtdEnderecos to set
     */
    public void setQtdEnderecos(Integer qtdEnderecos) {
        this.qtdEnderecos = qtdEnderecos;
    }
}
