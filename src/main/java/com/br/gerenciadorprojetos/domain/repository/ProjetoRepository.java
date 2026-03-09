package com.br.gerenciadorprojetos.domain.repository;

import com.br.gerenciadorprojetos.domain.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
