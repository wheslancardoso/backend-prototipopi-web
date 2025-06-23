package com.teatro.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma avaliação de sessão por um usuário
 * 
 * Permite que usuários avaliem as sessões que assistiram
 */
@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @Min(value = 1, message = "A nota deve ser no mínimo 1")
    @Max(value = 5, message = "A nota deve ser no máximo 5")
    @Column(name = "nota", nullable = false)
    private Integer nota;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @CreatedDate
    @Column(name = "data_avaliacao", nullable = false, updatable = false)
    private LocalDateTime dataAvaliacao;

    /**
     * Construtor para criação de avaliação
     */
    public Avaliacao(Usuario usuario, Sessao sessao, Integer nota) {
        this.usuario = usuario;
        this.sessao = sessao;
        this.nota = nota;
    }

    /**
     * Construtor para criação de avaliação com comentário
     */
    public Avaliacao(Usuario usuario, Sessao sessao, Integer nota, String comentario) {
        this.usuario = usuario;
        this.sessao = sessao;
        this.nota = nota;
        this.comentario = comentario;
    }

    /**
     * Verifica se a avaliação tem comentário
     */
    public boolean temComentario() {
        return this.comentario != null && !this.comentario.trim().isEmpty();
    }

    /**
     * Retorna a descrição da nota
     */
    public String getDescricaoNota() {
        return switch (this.nota) {
            case 1 -> "Péssimo";
            case 2 -> "Ruim";
            case 3 -> "Regular";
            case 4 -> "Bom";
            case 5 -> "Excelente";
            default -> "Não avaliado";
        };
    }

    /**
     * Verifica se a avaliação é positiva (nota >= 4)
     */
    public boolean isPositiva() {
        return this.nota >= 4;
    }

    /**
     * Verifica se a avaliação é negativa (nota <= 2)
     */
    public boolean isNegativa() {
        return this.nota <= 2;
    }

    /**
     * Verifica se a avaliação é neutra (nota = 3)
     */
    public boolean isNeutra() {
        return this.nota == 3;
    }
} 