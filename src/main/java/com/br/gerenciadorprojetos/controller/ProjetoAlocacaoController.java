package com.br.gerenciadorprojetos.controller;

import com.br.gerenciadorprojetos.service.AlocacaoMembroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projetos/{projetoId}/membros")
public class ProjetoAlocacaoController {

    private final AlocacaoMembroService alocacaoMembroService;

    public ProjetoAlocacaoController(AlocacaoMembroService alocacaoMembroService) {
        this.alocacaoMembroService = alocacaoMembroService;
    }

    @PostMapping("/{membroId}")
    public ResponseEntity<Void> alocarMembro(@PathVariable Long projetoId,
            @PathVariable Long membroId,
            @RequestParam(required = false) String papel) {
        alocacaoMembroService.alocarMembro(projetoId, membroId, papel);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{membroId}")
    public ResponseEntity<Void> desalocarMembro(@PathVariable Long projetoId,
            @PathVariable Long membroId) {
        alocacaoMembroService.desalocarMembro(projetoId, membroId);
        return ResponseEntity.noContent().build();
    }
}
