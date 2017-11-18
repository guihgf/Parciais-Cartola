package org.fermino.parciaisdocartola.pojos;

import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * Created by guihgf on 18/07/2017.
 */

public class JogadorTimeFavorito {
    private int id;
    private String apelido;
    private int clubeId;
    private int posicaoId;
    private Double numPontos;
    private List<Scout>scouts;
    private String escudo;

    public JogadorTimeFavorito(int id, String apelido, int clubeId, int posicaoId, Double numPontos, String escudo) {
        this.id = id;
        this.apelido = apelido;
        this.clubeId = clubeId;
        this.posicaoId = posicaoId;
        this.numPontos = numPontos;
        this.escudo=escudo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public int getClubeId() {
        return clubeId;
    }

    public void setClubeId(int clubeId) {
        this.clubeId = clubeId;
    }

    public int getPosicaoId() {
        return posicaoId;
    }

    public String getPosicaoDesc() {
        String posicao="";
        switch (posicaoId){
            case 1:
                posicao= "GOL";
                break;
            case 2:
                posicao= "LAT";
                break;
            case 3:
                posicao= "ZAG";
                break;
            case 4:
                posicao= "MEI";
                break;
            case 5:
                posicao= "ATA";
                break;
            case 6:
                posicao= "TEC";
                break;
        }
        return posicao;
    }

    public void setPosicaoId(int posicaoId) {
        this.posicaoId = posicaoId;
    }

    public Double getNumPontos() {
        return numPontos;
    }

    public void setNumPontos(Double numPontos) {
        this.numPontos = numPontos;
    }

    public String getEscudo() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }

    public List<Scout> getScouts() {
        return scouts;
    }

    public void setScout(List<Scout> scouts) {
        this.scouts = scouts;
    }



}
