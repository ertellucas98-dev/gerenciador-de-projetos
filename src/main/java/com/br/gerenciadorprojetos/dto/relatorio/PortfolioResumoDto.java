package com.br.gerenciadorprojetos.dto.relatorio;

import java.util.List;

public class PortfolioResumoDto {

    private List<StatusResumoDto> porStatus;
    private Double mediaDuracaoProjetosEncerrados;
    private Long totalMembrosUnicosAlocados;

    public List<StatusResumoDto> getPorStatus() {
        return porStatus;
    }

    public void setPorStatus(List<StatusResumoDto> porStatus) {
        this.porStatus = porStatus;
    }

    public Double getMediaDuracaoProjetosEncerrados() {
        return mediaDuracaoProjetosEncerrados;
    }

    public void setMediaDuracaoProjetosEncerrados(Double mediaDuracaoProjetosEncerrados) {
        this.mediaDuracaoProjetosEncerrados = mediaDuracaoProjetosEncerrados;
    }

    public Long getTotalMembrosUnicosAlocados() {
        return totalMembrosUnicosAlocados;
    }

    public void setTotalMembrosUnicosAlocados(Long totalMembrosUnicosAlocados) {
        this.totalMembrosUnicosAlocados = totalMembrosUnicosAlocados;
    }
}
