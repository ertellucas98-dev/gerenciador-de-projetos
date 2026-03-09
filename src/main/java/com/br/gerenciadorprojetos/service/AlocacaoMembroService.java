package com.br.gerenciadorprojetos.service;

import com.br.gerenciadorprojetos.domain.entity.Membro;
import com.br.gerenciadorprojetos.domain.entity.Projeto;
import com.br.gerenciadorprojetos.domain.entity.ProjetoMembro;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import com.br.gerenciadorprojetos.domain.repository.MembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoMembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlocacaoMembroService {

    private static final String ATRIBUICAO_FUNCIONARIO = "funcionario";

    private final ProjetoRepository projetoRepository;
    private final MembroRepository membroRepository;
    private final ProjetoMembroRepository projetoMembroRepository;

    public AlocacaoMembroService(ProjetoRepository projetoRepository,
            MembroRepository membroRepository,
            ProjetoMembroRepository projetoMembroRepository) {
        this.projetoRepository = projetoRepository;
        this.membroRepository = membroRepository;
        this.projetoMembroRepository = projetoMembroRepository;
    }

    public void alocarMembro(Long projetoId, Long membroId, String papel) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        Membro membro = membroRepository.findById(membroId)
                .orElseThrow(() -> new IllegalArgumentException("Membro não encontrado"));

        if (!ATRIBUICAO_FUNCIONARIO.equalsIgnoreCase(membro.getAtribuicao())) {
            throw new IllegalStateException(
                    "Apenas membros com atribuição 'funcionario' podem ser associados a projetos");
        }

        long totalNoProjeto = projetoMembroRepository.countByProjetoId(projetoId);
        if (totalNoProjeto >= 10) {
            throw new IllegalStateException("Cada projeto permite no máximo 10 membros alocados");
        }

        long projetosAtivosDoMembro = projetoMembroRepository.contarProjetosAtivosPorMembro(
                membroId,
                ProjetoStatus.ENCERRADO,
                ProjetoStatus.CANCELADO);

        if (projetosAtivosDoMembro >= 3) {
            throw new IllegalStateException("Um membro não pode estar alocado em mais de 3 projetos ativos");
        }

        boolean jaAlocado = projetoMembroRepository
                .findByProjetoIdAndMembroId(projetoId, membroId)
                .isPresent();

        if (jaAlocado) {
            return; // já está alocado, nenhuma ação
        }

        ProjetoMembro projetoMembro = new ProjetoMembro();
        projetoMembro.setProjeto(projeto);
        projetoMembro.setMembro(membro);
        projetoMembro.setPapel(papel);

        projetoMembroRepository.save(projetoMembro);
    }

    public void desalocarMembro(Long projetoId, Long membroId) {
        long totalNoProjeto = projetoMembroRepository.countByProjetoId(projetoId);
        if (totalNoProjeto <= 1) {
            throw new IllegalStateException("Cada projeto deve ter pelo menos 1 membro alocado");
        }

        ProjetoMembro alocacao = projetoMembroRepository
                .findByProjetoIdAndMembroId(projetoId, membroId)
                .orElseThrow(() -> new IllegalArgumentException("Alocação não encontrada para este projeto e membro"));

        projetoMembroRepository.delete(alocacao);
    }
}
