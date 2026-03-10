package com.br.gerenciadorprojetos.service;

import com.br.gerenciadorprojetos.domain.repository.ProjetoMembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoRepository;
import com.br.gerenciadorprojetos.dto.relatorio.PortfolioResumoDto;
import com.br.gerenciadorprojetos.dto.relatorio.StatusResumoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RelatorioPortfolioService {

    private final ProjetoRepository projetoRepository;

    public RelatorioPortfolioService(ProjetoRepository projetoRepository,
            ProjetoMembroRepository projetoMembroRepository) {
        this.projetoRepository = projetoRepository;
    }

    public PortfolioResumoDto gerarResumo() {
        PortfolioResumoDto dto = new PortfolioResumoDto();

        List<StatusResumoDto> porStatus = projetoRepository.resumoPorStatus()
                .stream()
                .map(view -> {
                    StatusResumoDto s = new StatusResumoDto();
                    s.setStatus(view.getStatus());
                    s.setQuantidade(view.getQuantidade());
                    s.setTotalOrcado(view.getTotalOrcado());
                    return s;
                })
                .collect(Collectors.toList());

        dto.setPorStatus(porStatus);
        dto.setMediaDuracaoProjetosEncerrados(projetoRepository.mediaDiasProjetosEncerrados());
        dto.setTotalMembrosUnicosAlocados(projetoRepository.totalMembrosUnicosAlocados());

        return dto;
    }
}
