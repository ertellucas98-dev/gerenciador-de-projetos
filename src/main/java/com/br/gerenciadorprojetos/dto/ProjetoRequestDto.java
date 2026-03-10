package com.br.gerenciadorprojetos.dto;

import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjetoRequestDto {

    private String nome;
    private LocalDate dataInicio;
    private LocalDate previsaoTermino;
    private LocalDate dataTerminoReal;
    private BigDecimal orcamentoTotal;
    private String descricao;
    private Long gerenteId;

    @Schema(description = "Status do projeto", example = "EM_ANALISE", allowableValues = { "EM_ANALISE",
            "ANALISE_REALIZADA", "ANALISE_APROVADA",
            "INICIADO", "PLANEJADO", "EM_ANDAMENTO", "ENCERRADO", "CANCELADO" })
    private ProjetoStatus status;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getPrevisaoTermino() {
        return previsaoTermino;
    }

    public void setPrevisaoTermino(LocalDate previsaoTermino) {
        this.previsaoTermino = previsaoTermino;
    }

    public LocalDate getDataTerminoReal() {
        return dataTerminoReal;
    }

    public void setDataTerminoReal(LocalDate dataTerminoReal) {
        this.dataTerminoReal = dataTerminoReal;
    }

    public BigDecimal getOrcamentoTotal() {
        return orcamentoTotal;
    }

    public void setOrcamentoTotal(BigDecimal orcamentoTotal) {
        this.orcamentoTotal = orcamentoTotal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getGerenteId() {
        return gerenteId;
    }

    public void setGerenteId(Long gerenteId) {
        this.gerenteId = gerenteId;
    }

    public ProjetoStatus getStatus() {
        return status;
    }

    public void setStatus(ProjetoStatus status) {
        this.status = status;
    }
}
