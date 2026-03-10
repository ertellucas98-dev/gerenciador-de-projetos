package com.br.gerenciadorprojetos.domain.specification;

import com.br.gerenciadorprojetos.domain.entity.Projeto;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import org.springframework.data.jpa.domain.Specification;

public final class ProjetoSpecification {

    private ProjetoSpecification() {
    }

    public static Specification<Projeto> statusIgual(ProjetoStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Projeto> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }
}
