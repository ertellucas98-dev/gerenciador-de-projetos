package com.br.gerenciadorprojetos.domain.enums;

import java.util.List;

public enum ProjetoStatus {
    EM_ANALISE,
    ANALISE_REALIZADA,
    ANALISE_APROVADA,
    INICIADO,
    PLANEJADO,
    EM_ANDAMENTO,
    ENCERRADO,
    CANCELADO;

    public List<ProjetoStatus> proximosStatusPermitidos() {
        return switch (this) {
            case EM_ANALISE -> List.of(ANALISE_REALIZADA, CANCELADO);
            case ANALISE_REALIZADA -> List.of(ANALISE_APROVADA, CANCELADO);
            case ANALISE_APROVADA -> List.of(INICIADO, CANCELADO);
            case INICIADO -> List.of(PLANEJADO, CANCELADO);
            case PLANEJADO -> List.of(EM_ANDAMENTO, CANCELADO);
            case EM_ANDAMENTO -> List.of(ENCERRADO, CANCELADO);
            case ENCERRADO, CANCELADO -> List.of();
        };
    }

    public boolean podeTransicionarPara(ProjetoStatus novoStatus) {
        return proximosStatusPermitidos().contains(novoStatus);
    }
}
