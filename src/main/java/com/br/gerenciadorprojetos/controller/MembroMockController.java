package com.br.gerenciadorprojetos.controller;

import com.br.gerenciadorprojetos.domain.entity.Membro;
import com.br.gerenciadorprojetos.domain.repository.MembroRepository;
import com.br.gerenciadorprojetos.dto.MembroRequestDto;
import com.br.gerenciadorprojetos.dto.MembroResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/membros-externos")
public class MembroMockController {

    private final MembroRepository membroRepository;

    public MembroMockController(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    @PostMapping
    public ResponseEntity<MembroResponseDto> criar(@RequestBody MembroRequestDto dto) {
        Membro membro = new Membro();
        membro.setNome(dto.getNome());
        membro.setAtribuicao(dto.getAtribuicao());
        membro.setIdentificadorExterno(UUID.randomUUID().toString());
        membro.setAtivo(Boolean.TRUE);

        Membro salvo = membroRepository.save(membro);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<MembroResponseDto>> listarTodos() {
        List<MembroResponseDto> membros = membroRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(membros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroResponseDto> buscarPorId(@PathVariable Long id) {
        return membroRepository.findById(id)
                .map(membro -> ResponseEntity.ok(toResponse(membro)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private MembroResponseDto toResponse(Membro membro) {
        MembroResponseDto dto = new MembroResponseDto();
        dto.setId(membro.getId());
        dto.setNome(membro.getNome());
        dto.setAtribuicao(membro.getAtribuicao());
        dto.setAtivo(membro.getAtivo());
        return dto;
    }
}
