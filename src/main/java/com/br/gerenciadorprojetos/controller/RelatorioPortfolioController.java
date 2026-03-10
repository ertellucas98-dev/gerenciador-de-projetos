package com.br.gerenciadorprojetos.controller;

import com.br.gerenciadorprojetos.dto.relatorio.PortfolioResumoDto;
import com.br.gerenciadorprojetos.service.RelatorioPortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios/portfolio")
public class RelatorioPortfolioController {

    private final RelatorioPortfolioService relatorioPortfolioService;

    public RelatorioPortfolioController(RelatorioPortfolioService relatorioPortfolioService) {
        this.relatorioPortfolioService = relatorioPortfolioService;
    }

    @GetMapping
    public ResponseEntity<PortfolioResumoDto> gerarResumo() {
        return ResponseEntity.ok(relatorioPortfolioService.gerarResumo());
    }
}
