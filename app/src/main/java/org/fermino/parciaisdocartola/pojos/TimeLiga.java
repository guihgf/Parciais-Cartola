package org.fermino.parciaisdocartola.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by guihgf on 03/07/2017.
 */


@DatabaseTable(tableName = "times_ligas")
public class TimeLiga {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int ligaid;
    @DatabaseField
    private int timeid;
    @DatabaseField
    private String nome;
    @DatabaseField(columnName = "nome_cartola")
    private String nomeCartola;
    @DatabaseField
    private String slug;
    @DatabaseField
    private String escudo;
    @DatabaseField
    private String fotoPerfil;
    @DatabaseField
    private Double patrimonio;
    @DatabaseField
    private String jsonRanking;
    @DatabaseField
    private String jsonPontos;
    @DatabaseField
    private String jsonVariacao;
    @DatabaseField
    private String jsonTime;

    private Double pontuacao;
    private Double parcial;
    private int jogadoresJogaram;

    private List<JogadorTimeFavorito>jogadores;

    public TimeLiga(){}

    public TimeLiga(int ligaid, int timeid, String nome, String nomeCartola, String slug, String escudo, String fotoPerfil, Double patrimonio, /*String jsonRanking,*/ String jsonPontos/*, String jsonVariacao*/) {
        this.ligaid = ligaid;
        this.timeid = timeid;
        this.nome = nome;
        this.nomeCartola = nomeCartola;
        this.slug = slug;
        this.escudo = escudo;
        this.fotoPerfil = fotoPerfil;
        this.patrimonio = patrimonio;
        //this.jsonRanking = jsonRanking;
        this.jsonPontos = jsonPontos;
        //this.jsonVariacao = jsonVariacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLigaid() {
        return ligaid;
    }

    public void setLigaid(int ligaid) {
        this.ligaid = ligaid;
    }

    public int getTimeid() {
        return timeid;
    }

    public void setTimeid(int timeid) {
        this.timeid = timeid;
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

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Double getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(Double patrimonio) {
        this.patrimonio = patrimonio;
    }

    public String getJsonRanking() {
        return jsonRanking;
    }

    public void setJsonRanking(String jsonRanking) {
        this.jsonRanking = jsonRanking;
    }

    public String getJsonPontos() {
        return jsonPontos;
    }

    public void setJsonPontos(String jsonPontos) {
        this.jsonPontos = jsonPontos;
    }

    public String getJsonVariacao() {
        return jsonVariacao;
    }

    public void setJsonVariacao(String jsonVariacao) {
        this.jsonVariacao = jsonVariacao;
    }

    public String getJsonTime() {
        return jsonTime;
    }

    public void setJsonTime(String jsonTime) {
        this.jsonTime = jsonTime;
    }

    public List<JogadorTimeFavorito> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<JogadorTimeFavorito> jogadores) {
        this.jogadores = jogadores;
    }

    public Double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Double getParcial() {
        return parcial;
    }

    public void setParcial(Double parcial) {
        this.parcial = parcial;
    }

    public int getJogadoresJogaram() {
        return jogadoresJogaram;
    }

    public void setJogadoresJogaram(int jogadoresJogaram) {
        this.jogadoresJogaram = jogadoresJogaram;
    }
}
