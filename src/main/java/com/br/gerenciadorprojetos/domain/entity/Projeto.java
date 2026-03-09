package com.br.gerenciadorprojetos.domain.entity;

import com.br.gerenciadorprojetos.domain.enums.ProjetoRisco;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projeto")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "previsao_termino", nullable = false)
    private LocalDate previsaoTermino;

    @Column(name = "data_termino_real")
    private LocalDate dataTerminoReal;

    @Column(name = "orcamento_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal orcamentoTotal;

    @Column
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gerente_id", nullable = false)
    private Membro gerente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjetoStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProjetoRisco risco;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjetoMembro> alocacoes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Membro getGerente() {
        return gerente;
    }

    public void setGerente(Membro gerente) {
        this.gerente = gerente;
    }

    public ProjetoStatus getStatus() {
        return status;
    }

    public void setStatus(ProjetoStatus status) {
        this.status = status;
    }

    public ProjetoRisco getRisco() {
        return risco;
    }

    public void setRisco(ProjetoRisco risco) {
        this.risco = risco;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Set<ProjetoMembro> getAlocacoes() {
        return alocacoes;
    }

    public void setAlocacoes(Set<ProjetoMembro> alocacoes) {
        this.alocacoes = alocacoes;
    }
}
