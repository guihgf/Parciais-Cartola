package org.fermino.parciaisdocartola.pojos;

/**
 * Created by guihgf on 02/07/2017.
 */

public class MercadoStatus {
    private int rodada;
    private int status;
    private int totalTimes;
    private String fechamento;

    public int getRodada() {
        return rodada;
    }

    public void setRodada(int rodada) {
        this.rodada = rodada;
    }

    public String getStatus() {
        return status==1?"Aberto":"Fechado";
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public String getFechamento() {
        return fechamento;
    }

    public void setFechamento(String fechamento) {
        this.fechamento = fechamento;
    }
}