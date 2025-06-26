package com.teatro.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.SessaoDTO;
import com.teatro.exception.SessaoJaExisteException;
import com.teatro.exception.SessaoNaoEncontradaException;
import com.teatro.service.SessaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a sessões
 *
 * Endpoints: - POST /api/sessoes - Cadastrar sessão - GET /api/sessoes - Listar sessões - GET
 * /api/sessoes/{id} - Buscar sessão por ID - PUT /api/sessoes/{id} - Atualizar sessão - DELETE
 * /api/sessoes/{id} - Remover sessão - GET /api/sessoes/ativas - Listar sessões ativas - GET
 * /api/sessoes/evento/{eventoId} - Listar sessões por evento - GET
 * /api/sessoes/horarios-disponiveis - Listar horários dinâmicos disponíveis - PUT
 * /api/sessoes/{id}/status - Alterar status da sessão
 */
@RestController
@RequestMapping("/sessoes")
@CrossOrigin(origins = "*")
@Tag(name = "Sessões", description = "Endpoints para gerenciamento de sessões teatrais")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @PostMapping
    public ResponseEntity<SessaoDTO> cadastrar(@Valid @RequestBody SessaoDTO sessaoDTO) {
        try {
            SessaoDTO sessaoSalva = sessaoService.cadastrarSessao(sessaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(sessaoSalva);
        } catch (SessaoJaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SessaoDTO>> listarSessoes() {
        List<SessaoDTO> sessoes = sessaoService.listarSessoesAtivas();
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<SessaoDTO>> listarSessoesAtivas() {
        List<SessaoDTO> sessoes = sessaoService.listarSessoesAtivas();
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<SessaoDTO>> listarSessoesPorEvento(@PathVariable Long eventoId) {
        List<SessaoDTO> sessoes = sessaoService.listarSessoesPorEvento(eventoId);
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/evento/{eventoId}/ativas")
    public ResponseEntity<List<SessaoDTO>> listarSessoesAtivasPorEvento(
            @PathVariable Long eventoId) {
        List<SessaoDTO> sessoes = sessaoService.listarSessoesAtivasPorEvento(eventoId);
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/tipo/{tipoSessao}")
    public ResponseEntity<List<SessaoDTO>> listarSessoesPorTipo(@PathVariable String tipoSessao) {
        try {
            List<SessaoDTO> sessoes = sessaoService.listarSessoesPorTipo(tipoSessao);
            return ResponseEntity.ok(sessoes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/data")
    public ResponseEntity<List<SessaoDTO>> listarSessoesPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<SessaoDTO> sessoes = sessaoService.listarSessoesPorData(data);
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/futuras")
    public ResponseEntity<List<SessaoDTO>> listarSessoesFuturas() {
        List<SessaoDTO> sessoes = sessaoService.listarSessoesFuturas();
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<SessaoDTO>> listarSessoesDisponiveisParaCompra() {
        List<SessaoDTO> sessoes = sessaoService.listarSessoesDisponiveisParaCompra();
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/disponiveis/evento/{eventoId}")
    public ResponseEntity<List<SessaoDTO>> listarSessoesDisponiveisParaCompraPorEvento(
            @PathVariable Long eventoId) {
        List<SessaoDTO> sessoes =
                sessaoService.listarSessoesDisponiveisParaCompraPorEvento(eventoId);
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoDTO> buscarPorId(@PathVariable Long id) {
        try {
            SessaoDTO sessao = sessaoService.buscarPorId(id);
            return ResponseEntity.ok(sessao);
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessaoDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody SessaoDTO sessaoDTO) {
        try {
            SessaoDTO sessaoAtualizada = sessaoService.atualizarSessao(id, sessaoDTO);
            return ResponseEntity.ok(sessaoAtualizada);
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        } catch (SessaoJaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            sessaoService.removerSessao(id);
            return ResponseEntity.noContent().build();
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SessaoDTO> alterarStatus(@PathVariable Long id,
            @RequestParam boolean ativo) {
        try {
            SessaoDTO sessao = sessaoService.alterarStatusSessao(id, ativo);
            return ResponseEntity.ok(sessao);
        } catch (SessaoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/horarios-disponiveis")
    public ResponseEntity<List<String>> listarHorariosDisponiveis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<String> horarios = sessaoService.listarHorariosDisponiveisPorData(data);
        return ResponseEntity.ok(horarios);
    }
}
