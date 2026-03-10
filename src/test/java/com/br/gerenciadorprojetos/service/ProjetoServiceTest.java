package com.br.gerenciadorprojetos.service;

import com.br.gerenciadorprojetos.domain.entity.Projeto;
import com.br.gerenciadorprojetos.domain.enums.ProjetoRisco;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import com.br.gerenciadorprojetos.domain.repository.MembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoRepository;
import com.br.gerenciadorprojetos.dto.ProjetoRequestDto;
import com.br.gerenciadorprojetos.dto.ProjetoResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private MembroRepository membroRepository;

    @InjectMocks
    private ProjetoService projetoService;

    @Test
    @DisplayName("Deve calcular risco BAIXO para orçamento até 100k e prazo até 3 meses")
    void deveCalcularRiscoBaixo() {
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(invocation -> {
            Projeto p = invocation.getArgument(0, Projeto.class);
            if (p.getId() == null) {
                p.setId(1L);
            }
            return p;
        });
        ProjetoRequestDto dto = new ProjetoRequestDto();
        dto.setNome("Projeto A");
        dto.setDataInicio(LocalDate.of(2025, 1, 1));
        dto.setPrevisaoTermino(LocalDate.of(2025, 3, 1));
        dto.setOrcamentoTotal(new BigDecimal("50000"));

        ProjetoResponseDto response = projetoService.criar(dto);

        assertThat(response.getRisco()).isEqualTo(ProjetoRisco.BAIXO);
        assertThat(response.getStatus()).isEqualTo(ProjetoStatus.EM_ANALISE);
    }

    @Test
    @DisplayName("Deve calcular risco MEDIO para orçamento entre 100001 e 500000")
    void deveCalcularRiscoMedioPorOrcamento() {
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(invocation -> {
            Projeto p = invocation.getArgument(0, Projeto.class);
            if (p.getId() == null) {
                p.setId(1L);
            }
            return p;
        });
        ProjetoRequestDto dto = new ProjetoRequestDto();
        dto.setNome("Projeto B");
        dto.setDataInicio(LocalDate.of(2025, 1, 1));
        dto.setPrevisaoTermino(LocalDate.of(2025, 2, 1));
        dto.setOrcamentoTotal(new BigDecimal("150000"));

        ProjetoResponseDto response = projetoService.criar(dto);

        assertThat(response.getRisco()).isEqualTo(ProjetoRisco.MEDIO);
    }

    @Test
    @DisplayName("Deve calcular risco MEDIO para prazo entre 3 e 6 meses")
    void deveCalcularRiscoMedioPorPrazo() {
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(invocation -> {
            Projeto p = invocation.getArgument(0, Projeto.class);
            if (p.getId() == null) {
                p.setId(1L);
            }
            return p;
        });
        ProjetoRequestDto dto = new ProjetoRequestDto();
        dto.setNome("Projeto C");
        dto.setDataInicio(LocalDate.of(2025, 1, 1));
        dto.setPrevisaoTermino(LocalDate.of(2025, 6, 1));
        dto.setOrcamentoTotal(new BigDecimal("80000"));

        ProjetoResponseDto response = projetoService.criar(dto);

        assertThat(response.getRisco()).isEqualTo(ProjetoRisco.MEDIO);
    }

    @Test
    @DisplayName("Deve calcular risco ALTO para orçamento acima de 500000")
    void deveCalcularRiscoAltoPorOrcamento() {
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(invocation -> {
            Projeto p = invocation.getArgument(0, Projeto.class);
            if (p.getId() == null) {
                p.setId(1L);
            }
            return p;
        });
        ProjetoRequestDto dto = new ProjetoRequestDto();
        dto.setNome("Projeto D");
        dto.setDataInicio(LocalDate.of(2025, 1, 1));
        dto.setPrevisaoTermino(LocalDate.of(2025, 2, 1));
        dto.setOrcamentoTotal(new BigDecimal("600000"));

        ProjetoResponseDto response = projetoService.criar(dto);

        assertThat(response.getRisco()).isEqualTo(ProjetoRisco.ALTO);
    }

    @Test
    @DisplayName("Deve calcular risco ALTO para prazo superior a 6 meses")
    void deveCalcularRiscoAltoPorPrazo() {
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(invocation -> {
            Projeto p = invocation.getArgument(0, Projeto.class);
            if (p.getId() == null) {
                p.setId(1L);
            }
            return p;
        });
        ProjetoRequestDto dto = new ProjetoRequestDto();
        dto.setNome("Projeto E");
        dto.setDataInicio(LocalDate.of(2025, 1, 1));
        dto.setPrevisaoTermino(LocalDate.of(2025, 10, 1));
        dto.setOrcamentoTotal(new BigDecimal("90000"));

        ProjetoResponseDto response = projetoService.criar(dto);

        assertThat(response.getRisco()).isEqualTo(ProjetoRisco.ALTO);
    }

    @Test
    @DisplayName("Deve permitir exclusão quando status não for iniciado, em andamento ou encerrado")
    void devePermitirExclusaoQuandoStatusPermitido() {
        Projeto projeto = new Projeto();
        projeto.setId(10L);
        projeto.setStatus(ProjetoStatus.EM_ANALISE);

        when(projetoRepository.findById(10L)).thenReturn(Optional.of(projeto));

        projetoService.excluir(10L);

        verify(projetoRepository).delete(projeto);
    }

    @Test
    @DisplayName("Deve bloquear exclusão quando status for INICIADO")
    void deveBloquearExclusaoQuandoIniciado() {
        Projeto projeto = new Projeto();
        projeto.setId(11L);
        projeto.setStatus(ProjetoStatus.INICIADO);

        when(projetoRepository.findById(11L)).thenReturn(Optional.of(projeto));

        assertThatThrownBy(() -> projetoService.excluir(11L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("não podem ser excluídos");

        verify(projetoRepository, never()).delete(any(Projeto.class));
    }

    @Test
    @DisplayName("Deve permitir transição EM_ANALISE \u2192 ANALISE_REALIZADA")
    void devePermitirTransicaoValida() {
        Projeto projeto = new Projeto();
        projeto.setId(1L);
        projeto.setStatus(ProjetoStatus.EM_ANALISE);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjetoResponseDto response = projetoService.alterarStatus(1L, ProjetoStatus.ANALISE_REALIZADA);

        assertThat(response.getStatus()).isEqualTo(ProjetoStatus.ANALISE_REALIZADA);
    }

    @Test
    @DisplayName("Deve permitir cancelar projeto em qualquer status ativo")
    void devePermitirCancelarProjeto() {
        Projeto projeto = new Projeto();
        projeto.setId(2L);
        projeto.setStatus(ProjetoStatus.EM_ANDAMENTO);

        when(projetoRepository.findById(2L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjetoResponseDto response = projetoService.alterarStatus(2L, ProjetoStatus.CANCELADO);

        assertThat(response.getStatus()).isEqualTo(ProjetoStatus.CANCELADO);
    }

    @Test
    @DisplayName("Deve bloquear pular etapas: EM_ANALISE \u2192 EM_ANDAMENTO")
    void deveBloquearPularEtapas() {
        Projeto projeto = new Projeto();
        projeto.setId(3L);
        projeto.setStatus(ProjetoStatus.EM_ANALISE);

        when(projetoRepository.findById(3L)).thenReturn(Optional.of(projeto));

        assertThatThrownBy(() -> projetoService.alterarStatus(3L, ProjetoStatus.EM_ANDAMENTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Transi\u00e7\u00e3o de status inv\u00e1lida");
    }

    @Test
    @DisplayName("Deve bloquear transi\u00e7\u00e3o a partir de ENCERRADO")
    void deveBloquearTransicaoDeEncerrado() {
        Projeto projeto = new Projeto();
        projeto.setId(4L);
        projeto.setStatus(ProjetoStatus.ENCERRADO);

        when(projetoRepository.findById(4L)).thenReturn(Optional.of(projeto));

        assertThatThrownBy(() -> projetoService.alterarStatus(4L, ProjetoStatus.CANCELADO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Transi\u00e7\u00e3o de status inv\u00e1lida");
    }
}
