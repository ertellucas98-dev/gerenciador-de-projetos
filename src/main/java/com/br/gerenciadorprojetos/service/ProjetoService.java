package com.br.gerenciadorprojetos.service;

import com.br.gerenciadorprojetos.domain.entity.Membro;
import com.br.gerenciadorprojetos.domain.entity.Projeto;
import com.br.gerenciadorprojetos.domain.repository.MembroRepository;
import com.br.gerenciadorprojetos.domain.repository.ProjetoRepository;
import com.br.gerenciadorprojetos.dto.ProjetoRequestDto;
import com.br.gerenciadorprojetos.dto.ProjetoResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        aplicarDadosBasicos(dto, projeto);
        Projeto salvo = projetoRepository.save(projeto);
        return toResponse(salvo);
    }

    public ProjetoResponseDto atualizar(Long id, ProjetoRequestDto dto) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        aplicarDadosBasicos(dto, projeto);
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

    public void excluir(Long id) {
        // Regras de bloqueio de exclusão serão adicionadas em etapa futura
        projetoRepository.deleteById(id);
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
