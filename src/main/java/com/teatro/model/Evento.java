package com.teatro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um evento teatral
 * 
 * Um evento pode ter múltiplas sessões em diferentes datas e horários.
 * Exemplos: Hamlet, O Fantasma da Opera, O Auto da Compadecida
 */
@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do evento é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    @Column(name = "classificacao_indicativa")
    private String classificacaoIndicativa;

    @Column(name = "url_poster")
    private String urlPoster;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamento com Sessões (1:N)
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sessao> sessoes = new ArrayList<>();

    /**
     * Construtor para criação de evento básico
     */
    public Evento(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true;
    }

    /**
     * Adiciona uma sessão ao evento
     */
    public void adicionarSessao(Sessao sessao) {
        this.sessoes.add(sessao);
        sessao.setEvento(this);
    }

    /**
     * Remove uma sessão do evento
     */
    public void removerSessao(Sessao sessao) {
        this.sessoes.remove(sessao);
        sessao.setEvento(null);
    }

    /**
     * Verifica se o evento está ativo
     */
    public boolean isAtivo() {
        return this.ativo != null && this.ativo;
    }

    /**
     * Retorna apenas as sessões ativas do evento
     */
    public List<Sessao> getSessoesAtivas() {
        return this.sessoes.stream()
                .filter(Sessao::isAtiva)
                .toList();
    }
} 