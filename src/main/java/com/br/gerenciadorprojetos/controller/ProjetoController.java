package com.br.gerenciadorprojetos.controller;

import com.br.gerenciadorprojetos.dto.ProjetoRequestDto;
import com.br.gerenciadorprojetos.dto.ProjetoResponseDto;
import com.br.gerenciadorprojetos.service.ProjetoService;
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
    public ResponseEntity<List<ProjetoResponseDto>> listarTodos() {
        List<ProjetoResponseDto> projetos = projetoService.listarTodos();
        return ResponseEntity.ok(projetos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
