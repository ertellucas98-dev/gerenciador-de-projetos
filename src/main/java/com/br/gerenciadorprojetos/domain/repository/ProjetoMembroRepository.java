package com.br.gerenciadorprojetos.domain.repository;

import com.br.gerenciadorprojetos.domain.entity.ProjetoMembro;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjetoMembroRepository extends JpaRepository<ProjetoMembro, Long> {

    long countByProjetoId(Long projetoId);

    Optional<ProjetoMembro> findByProjetoIdAndMembroId(Long projetoId, Long membroId);

    @Query("select count(pm) from ProjetoMembro pm " +
	    "where pm.membro.id = :membroId " +
	    "and pm.projeto.status not in (:statusEncerrado, :statusCancelado)")
    long contarProjetosAtivosPorMembro(@Param("membroId") Long membroId,
					    @Param("statusEncerrado") ProjetoStatus statusEncerrado,
					    @Param("statusCancelado") ProjetoStatus statusCancelado);
}
