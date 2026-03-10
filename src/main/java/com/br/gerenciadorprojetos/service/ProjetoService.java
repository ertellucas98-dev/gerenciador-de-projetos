package com.br.gerenciadorprojetos.service;

import com.br.gerenciadorprojetos.domain.entity.Membro;
import com.br.gerenciadorprojetos.domain.entity.Projeto;
import com.br.gerenciadorprojetos.domain.enums.ProjetoRisco;
import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import com.br.gerenciadorprojetos.domain.repository.MembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoRepository;
import com.br.gerenciadorprojetos.domain.specification.ProjetoSpecification;
import com.br.gerenciadorprojetos.dto.ProjetoRequestDto;
import com.br.gerenciadorprojetos.dto.ProjetoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final MembroRepository membroRepository;

    public ProjetoService(ProjetoRepository projetoRepository, MembroRepository membroRepository) {
        this.projetoRepository = projetoRepository;
        this.membroRepository = membroRepository;
    }

    public ProjetoResponseDto criar(ProjetoRequestDto dto) {
        Projeto projeto = new Projeto();
        // Status inicial sempre em análise
        projeto.setStatus(ProjetoStatus.EM_ANALISE);
        aplicarDadosBasicos(dto, projeto);
        projeto.setRisco(calcularRisco(projeto));
        Projeto salvo = projetoRepository.save(projeto);
        return toResponse(salvo);
    }

    public ProjetoResponseDto atualizar(Long id, ProjetoRequestDto dto) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        aplicarDadosBasicos(dto, projeto);
        projeto.setRisco(calcularRisco(projeto));
        Projeto salvo = projetoRepository.save(projeto);
        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public ProjetoResponseDto buscarPorId(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
        return toResponse(projeto);
    }

    @Transactional(readOnly = true)
    public List<ProjetoResponseDto> listarTodos() {
        return projetoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProjetoResponseDto> listarPaginado(String nome, ProjetoStatus status, Pageable pageable) {
        Specification<Projeto> spec = Specification
                .where(ProjetoSpecification.nomeContem(nome))
                .and(ProjetoSpecification.statusIgual(status));

        return projetoRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public void excluir(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        ProjetoStatus status = projeto.getStatus();
        if (status == ProjetoStatus.INICIADO
                || status == ProjetoStatus.EM_ANDAMENTO
                || status == ProjetoStatus.ENCERRADO) {
            throw new IllegalStateException("Projetos iniciados, em andamento ou encerrados não podem ser excluídos");
        }

        projetoRepository.delete(projeto);
    }

    private void aplicarDadosBasicos(ProjetoRequestDto dto, Projeto projeto) {
        projeto.setNome(dto.getNome());
        projeto.setDataInicio(dto.getDataInicio());
        projeto.setPrevisaoTermino(dto.getPrevisaoTermino());
        projeto.setDataTerminoReal(dto.getDataTerminoReal());
        projeto.setOrcamentoTotal(dto.getOrcamentoTotal());
        projeto.setDescricao(dto.getDescricao());

        if (dto.getGerenteId() != null) {
            Optional<Membro> gerenteOpt = membroRepository.findById(dto.getGerenteId());
            gerenteOpt.ifPresent(projeto::setGerente);
        }
    }

    private ProjetoRisco calcularRisco(Projeto projeto) {
        if (projeto.getOrcamentoTotal() == null
                || projeto.getDataInicio() == null
                || projeto.getPrevisaoTermino() == null) {
            return ProjetoRisco.BAIXO;
        }

        BigDecimal orcamento = projeto.getOrcamentoTotal();
        BigDecimal limiteBaixo = new BigDecimal("100000");
        BigDecimal limiteMedio = new BigDecimal("500000");

        long mesesPrazo = ChronoUnit.MONTHS.between(
                projeto.getDataInicio(),
                projeto.getPrevisaoTermino());

        // Garante que prazos muito curtos não fiquem negativos
        if (mesesPrazo < 0) {
            mesesPrazo = 0;
        }

        // Alto risco: orçamento acima de 500.000 ou prazo superior a 6 meses
        if (orcamento.compareTo(limiteMedio) > 0 || mesesPrazo > 6) {
            return ProjetoRisco.ALTO;
        }

        // Médio risco: orçamento entre 100.001 e 500.000
        // ou prazo entre 3 a 6 meses (exclusivo >3, inclusivo <=6)
        boolean orcamentoMedio = orcamento.compareTo(limiteBaixo) > 0
                && orcamento.compareTo(limiteMedio) <= 0;
        boolean prazoMedio = mesesPrazo > 3 && mesesPrazo <= 6;

        if (orcamentoMedio || prazoMedio) {
            return ProjetoRisco.MEDIO;
        }

        // Caso contrário, baixo risco: orçamento até 100.000 e prazo <= 3 meses
        return ProjetoRisco.BAIXO;
    }

    private ProjetoResponseDto toResponse(Projeto projeto) {
        ProjetoResponseDto response = new ProjetoResponseDto();
        response.setId(projeto.getId());
        response.setNome(projeto.getNome());
        response.setDataInicio(projeto.getDataInicio());
        response.setPrevisaoTermino(projeto.getPrevisaoTermino());
        response.setDataTerminoReal(projeto.getDataTerminoReal());
        response.setOrcamentoTotal(projeto.getOrcamentoTotal());
        response.setDescricao(projeto.getDescricao());
        if (projeto.getGerente() != null) {
            response.setGerenteId(projeto.getGerente().getId());
        }
        response.setStatus(projeto.getStatus());
        response.setRisco(projeto.getRisco());
        return response;
    }
}
