package com.br.gerenciadorprojetos.service;

import com.br.gerenciadorprojetos.domain.entity.Membro;
import com.br.gerenciadorprojetos.domain.entity.Projeto;
import com.br.gerenciadorprojetos.domain.entity.ProjetoMembro;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import com.br.gerenciadorprojetos.domain.repository.MembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoMembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlocacaoMembroServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private ProjetoMembroRepository projetoMembroRepository;

    @InjectMocks
    private AlocacaoMembroService alocacaoMembroService;

    @Test
    @DisplayName("Deve lançar exceção se membro não for funcionario")
    void deveFalharQuandoMembroNaoForFuncionario() {
        Projeto projeto = new Projeto();
        projeto.setId(1L);

        Membro membro = new Membro();
        membro.setId(10L);
        membro.setAtribuicao("terceirizado");

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroRepository.findById(10L)).thenReturn(Optional.of(membro));

        assertThatThrownBy(() -> alocacaoMembroService.alocarMembro(1L, 10L, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Apenas membros com atribuição 'funcionario'");

        verify(projetoMembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve limitar em no máximo 10 membros por projeto")
    void deveLimitarDezMembrosPorProjeto() {
        Projeto projeto = new Projeto();
        projeto.setId(1L);

        Membro membro = new Membro();
        membro.setId(10L);
        membro.setAtribuicao("funcionario");

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroRepository.findById(10L)).thenReturn(Optional.of(membro));
        when(projetoMembroRepository.countByProjetoId(1L)).thenReturn(10L);

        assertThatThrownBy(() -> alocacaoMembroService.alocarMembro(1L, 10L, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no máximo 10 membros");

        verify(projetoMembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve limitar membro em no máximo 3 projetos ativos")
    void deveLimitarTresProjetosAtivosPorMembro() {
        Projeto projeto = new Projeto();
        projeto.setId(1L);

        Membro membro = new Membro();
        membro.setId(10L);
        membro.setAtribuicao("funcionario");

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroRepository.findById(10L)).thenReturn(Optional.of(membro));
        when(projetoMembroRepository.countByProjetoId(1L)).thenReturn(5L);
        when(projetoMembroRepository.contarProjetosAtivosPorMembro(10L, ProjetoStatus.ENCERRADO, ProjetoStatus.CANCELADO))
                .thenReturn(3L);

        assertThatThrownBy(() -> alocacaoMembroService.alocarMembro(1L, 10L, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("mais de 3 projetos ativos");

        verify(projetoMembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve salvar nova alocação se membro já estiver alocado no projeto")
    void naoDeveDuplicarAlocacao() {
        Projeto projeto = new Projeto();
        projeto.setId(1L);

        Membro membro = new Membro();
        membro.setId(10L);
        membro.setAtribuicao("funcionario");

        ProjetoMembro existente = new ProjetoMembro();

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(membroRepository.findById(10L)).thenReturn(Optional.of(membro));
        when(projetoMembroRepository.countByProjetoId(1L)).thenReturn(5L);
        when(projetoMembroRepository.contarProjetosAtivosPorMembro(10L, ProjetoStatus.ENCERRADO, ProjetoStatus.CANCELADO))
                .thenReturn(1L);
        when(projetoMembroRepository.findByProjetoIdAndMembroId(1L, 10L)).thenReturn(Optional.of(existente));

        alocacaoMembroService.alocarMembro(1L, 10L, "DEV");

        verify(projetoMembroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve impedir desalocar quando projeto tiver apenas 1 membro")
    void deveImpedirDesalocarQuandoApenasUmMembro() {
        when(projetoMembroRepository.countByProjetoId(1L)).thenReturn(1L);

        assertThatThrownBy(() -> alocacaoMembroService.desalocarMembro(1L, 10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("pelo menos 1 membro");

        verify(projetoMembroRepository, never()).findByProjetoIdAndMembroId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Deve desalocar membro quando houver mais de 1 no projeto")
    void deveDesalocarQuandoMaisDeUmMembro() {
        ProjetoMembro alocacao = new ProjetoMembro();

        when(projetoMembroRepository.countByProjetoId(1L)).thenReturn(2L);
        when(projetoMembroRepository.findByProjetoIdAndMembroId(1L, 10L))
                .thenReturn(Optional.of(alocacao));

        alocacaoMembroService.desalocarMembro(1L, 10L);

        verify(projetoMembroRepository).delete(alocacao);
    }
}
