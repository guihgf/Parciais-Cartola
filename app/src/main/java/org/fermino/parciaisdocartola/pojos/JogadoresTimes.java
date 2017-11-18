package org.fermino.parciaisdocartola.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 08/08/2017.
 */

public class JogadoresTimes
{
    private int id;
    private String nome;

    private List<TimeFavorito> times=new ArrayList<TimeFavorito>();

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

    public List<TimeFavorito> getTimes() {
        return times;
    }

    public void setTimes(TimeFavorito times) {
        this.times.add(times);
    }
}
