package com.br.gerenciadorprojetos.controller;

import com.br.gerenciadorprojetos.domain.enums.ProjetoStatus;
import com.br.gerenciadorprojetos.dto.ProjetoRequestDto;
import com.br.gerenciadorprojetos.dto.ProjetoResponseDto;
import com.br.gerenciadorprojetos.service.ProjetoService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @PostMapping
    public ResponseEntity<ProjetoResponseDto> criar(@RequestBody ProjetoRequestDto dto) {
        ProjetoResponseDto criado = projetoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDto> atualizar(@PathVariable Long id,
            @RequestBody ProjetoRequestDto dto) {
        ProjetoResponseDto atualizado = projetoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDto> buscarPorId(@PathVariable Long id) {
        ProjetoResponseDto projeto = projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
    }

    @GetMapping
    public ResponseEntity<Page<ProjetoResponseDto>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) ProjetoStatus status,
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ProjetoResponseDto> projetos = projetoService.listarPaginado(nome, status, pageable);
        return ResponseEntity.ok(projetos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjetoResponseDto> alterarStatus(@PathVariable Long id,
            @RequestParam ProjetoStatus novoStatus) {
        ProjetoResponseDto atualizado = projetoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(atualizado);
    }
}
