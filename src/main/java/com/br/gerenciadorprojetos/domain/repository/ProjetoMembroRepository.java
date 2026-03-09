package com.br.gerenciadorprojetos.domain.repository;

import com.br.gerenciadorprojetos.domain.entity.ProjetoMembro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoMembroRepository extends JpaRepository<ProjetoMembro, Long> {
}
