package org.fermino.parciaisdocartola.pojos;

/**
 * Created by guihgf on 25/07/2017.
 */

public class Scout {
    private String codigo;
    private int quantidade;
    private Double pontos;

    public String getCodigo() {
        String desc="";
        switch (codigo){
            case "RB":
                desc="Roubada de bola";
                break;
            case "FC":
                desc="Falta cometida";
                break;
            case "GC":
                desc="Gol contra";
                break;
            case "CA":
                desc="Cartão amarelo";
                break;
            case "CV":
                desc="Cartão vermelho";
                break;
            case "FS":
                desc="Falta sofrida";
                break;
            case "PE":
                desc="Passe errado";
                break;
            case "FT":
                desc="Finalização na trave";
                break;
            case "FD":
                desc="Finalização defendida";
                break;
            case "FF":
                desc="Finalização para fora";
                break;
            case "G":
                desc="Gols";
                break;
            case "I":
                desc="Impedimento";
                break;
            case "PP":
                desc="Penalti perdido";
                break;
            case "A":
                desc="Assistência";
                break;
            case "SG":
                desc="Jogo sem sofrer gol";
                break;
            case "DD":
                desc="Defesa difícil";
                break;
            case "DP":
                desc="Defesa de penalti";
                break;
            case "GS":
                desc="Gol sofrido";
                break;
        }

        return desc;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPontos() {
        switch (codigo){
            case "RB":
                pontos=1.7;
                break;
            case "FC":
                pontos=-0.5;
                break;
            case "GC":
                pontos=-0.6;
                break;
            case "CA":
                pontos=-2.0;
                break;
            case "CV":
                pontos=-5.0;
                break;
            case "FS":
                pontos=0.5;
                break;
            case "PE":
                pontos=-0.3;
                break;
            case "FT":
                pontos=3.5;
                break;
            case "FD":
                pontos=1.0;
                break;
            case "FF":
                pontos=0.7;
                break;
            case "G":
                pontos=8.0;
                break;
            case "I":
                pontos=-0.5;
                break;
            case "PP":
                pontos=-3.5;
                break;
            case "A":
                pontos=5.0;
                break;
            case "SG":
                pontos=5.0;
                break;
            case "DD":
                pontos=3.0;
                break;
            case "DP":
                pontos=7.0;
                break;
            case "GS":
                pontos=-2.0;
                break;
        }

        return pontos;

    }
}
