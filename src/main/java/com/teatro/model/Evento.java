package com.teatro.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um evento teatral
 * 
 * Um evento pode ter múltiplas sessões em diferentes datas e horários. Exemplos: Hamlet, O Fantasma
 * da Opera, O Auto da Compadecida
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

    @Column(name = "poster", length = 255)
    private String poster;

    @Positive(message = "Duração deve ser positiva")
    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos = 120;

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
        this.duracaoMinutos = 120;
    }

    /**
     * Construtor para criação de evento completo
     */
    public Evento(String nome, String descricao, String poster, Integer duracaoMinutos) {
        this.nome = nome;
        this.descricao = descricao;
        this.poster = poster;
        this.duracaoMinutos = duracaoMinutos != null ? duracaoMinutos : 120;
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
        return this.sessoes.stream().filter(Sessao::isAtiva).toList();
    }

    /**
     * Retorna apenas as sessões futuras do evento
     */
    public List<Sessao> getSessoesFuturas() {
        LocalDateTime agora = LocalDateTime.now();
        return this.sessoes.stream()
                .filter(s -> s.isAtiva() && s.getDataSessao().isAfter(agora.toLocalDate()))
                .toList();
    }

    /**
     * Retorna apenas as sessões passadas do evento
     */
    public List<Sessao> getSessoesPassadas() {
        LocalDateTime agora = LocalDateTime.now();
        return this.sessoes.stream().filter(s -> s.getDataSessao().isBefore(agora.toLocalDate()))
                .toList();
    }

    /**
     * Ativa o evento
     */
    public void ativar() {
        this.ativo = true;
    }

    /**
     * Desativa o evento
     */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Retorna a duração formatada em horas e minutos
     */
    public String getDuracaoFormatada() {
        if (this.duracaoMinutos == null) {
            return "120 min";
        }

        int horas = this.duracaoMinutos / 60;
        int minutos = this.duracaoMinutos % 60;

        if (horas > 0) {
            return String.format("%dh %dmin", horas, minutos);
        } else {
            return String.format("%d min", minutos);
        }
    }
}
