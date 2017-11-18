package org.fermino.parciaisdocartola.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by guihgf on 03/07/2017.
 */


@DatabaseTable(tableName = "times_favoritos")
public class TimeFavorito {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String nome;
    @DatabaseField(columnName = "nome_cartola")
    private String nomeCartola;
    @DatabaseField
    private String slug;
    @DatabaseField
    private String escudo;
    @DatabaseField
    private int rodadaTime;
    @DatabaseField
    private int statusRodadaTime;
    @DatabaseField
    private String jsonTime;

    private List<JogadorTimeFavorito>jogadores;

    public  TimeFavorito(){}

    public TimeFavorito(int id,String nome, String nomeCartola, String slug, String escudo){
        this.id=id;
        this.nome=nome;
        this.nomeCartola=nomeCartola;
        this.slug=slug;
        this.escudo=escudo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeCartola() {
        return nomeCartola;
    }

    public void setNomeCartola(String nomeCartola) {
        this.nomeCartola = nomeCartola;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEscudo() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }

    public List<JogadorTimeFavorito> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<JogadorTimeFavorito> jogadores) {
        this.jogadores = jogadores;
    }

    public int getRodadaTime() {
        return rodadaTime;
    }

    public void setRodadaTime(int rodadaTime) {
        this.rodadaTime = rodadaTime;
    }

    public String getJsonTime() {
        return jsonTime;
    }

    public void setJsonTime(String jsonTime) {
        this.jsonTime = jsonTime;
    }

    public int getStatusRodadaTime() {
        return statusRodadaTime;
    }

    public void setStatusRodadaTime(int statusRodadaTime) {
        this.statusRodadaTime = statusRodadaTime;
    }
}
