package com.br.gerenciadorprojetos.domain.repository;

import com.br.gerenciadorprojetos.domain.entity.Projeto;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

	@Query("select p.status as status, count(p) as quantidade, coalesce(sum(p.orcamentoTotal), 0) as totalOrcado " +
		   "from Projeto p group by p.status")
	List<StatusResumoView> resumoPorStatus();

	@Query("select avg(datediff(day, p.dataInicio, p.dataTerminoReal)) from Projeto p " +
		   "where p.status = com.br.gerenciadorprojetos.domain.enums.ProjetoStatus.ENCERRADO " +
		   "and p.dataTerminoReal is not null")
	Double mediaDiasProjetosEncerrados();

	@Query("select count(distinct pm.membro.id) from ProjetoMembro pm")
	Long totalMembrosUnicosAlocados();

	interface StatusResumoView {
		ProjetoStatus getStatus();

		Long getQuantidade();

		BigDecimal getTotalOrcado();
	}
}
