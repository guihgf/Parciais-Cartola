package org.fermino.parciaisdocartola.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by guihgf on 27/07/2017.
 */

@DatabaseTable(tableName = "ligas_favoritas")
public class LigaFavorita {
    @DatabaseField(columnName = "id",id = true,generatedId = false)
    private int ligaId;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String slug;
    @DatabaseField
    private String imagem;
    @DatabaseField(columnName = "mata_mata")
    private boolean mataMata;
    @DatabaseField
    private int rodadaTime;
    @DatabaseField
    private int statusRodadaTime;

    public LigaFavorita(){}

    public LigaFavorita(int ligaId, String nome, String slug, String imagem, boolean mataMata) {
        this.ligaId = ligaId;
        this.nome = nome;
        this.slug = slug;
        this.imagem = imagem;
        this.mataMata = mataMata;
    }

    public int getLigaId() {
        return ligaId;
    }

    public void setLigaId(int ligaId) {
        this.ligaId = ligaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isMataMata() {
        return mataMata;
    }

    public void setMataMata(boolean mataMata) {
        this.mataMata = mataMata;
    }

    public int getRodadaTime() {
        return rodadaTime;
    }

    public void setRodadaTime(int rodadaTime) {
        this.rodadaTime = rodadaTime;
    }

    public int getStatusRodadaTime() {
        return statusRodadaTime;
    }

    public void setStatusRodadaTime(int statusRodadaTime) {
        this.statusRodadaTime = statusRodadaTime;
    }
}
