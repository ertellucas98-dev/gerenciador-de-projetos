package com.br.gerenciadorprojetos.dto.relatorio;

import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;

import java.math.BigDecimal;

public class StatusResumoDto {

    private ProjetoStatus status;
    private Long quantidade;
    private BigDecimal totalOrcado;

    public ProjetoStatus getStatus() {
        return status;
    }

    public void setStatus(ProjetoStatus status) {
        this.status = status;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getTotalOrcado() {
        return totalOrcado;
    }

    public void setTotalOrcado(BigDecimal totalOrcado) {
        this.totalOrcado = totalOrcado;
    }
}
