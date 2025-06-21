package com.teatro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um ingresso vendido
 * 
 * Implementa a lógica de ocupação de poltronas com chave única:
 * - evento_id + data + horário + area_id + poltrona_id
 * 
 * Uma vez que uma poltrona é reservada para uma combinação específica,
 * ela fica ocupada para todos os usuários até que qualquer parâmetro mude.
 */
@Entity
@Table(name = "ingressos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Sessão é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @NotNull(message = "Área é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @NotNull(message = "Número da poltrona é obrigatório")
    @Min(value = 1, message = "Número da poltrona deve ser maior que zero")
    @Column(name = "numero_poltrona", nullable = false)
    private Integer numeroPoltrona;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusIngresso status = StatusIngresso.VALIDO;

    @CreatedDate
    @Column(name = "data_compra", nullable = false, updatable = false)
    private LocalDateTime dataCompra;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    /**
     * Enum que define os status possíveis de um ingresso
     */
    public enum StatusIngresso {
        VALIDO("Válido"),
        UTILIZADO("Utilizado"),
        CANCELADO("Cancelado"),
        REEMBOLSADO("Reembolsado");

        private final String descricao;

        StatusIngresso(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    /**
     * Construtor para criação de ingresso
     */
    public Ingresso(Usuario usuario, Sessao sessao, Area area, Integer numeroPoltrona, BigDecimal valor) {
        this.usuario = usuario;
        this.sessao = sessao;
        this.area = area;
        this.numeroPoltrona = numeroPoltrona;
        this.valor = valor;
        this.codigo = gerarCodigoUnico();
        this.status = StatusIngresso.VALIDO;
    }

    /**
     * Gera um código único para o ingresso
     */
    private String gerarCodigoUnico() {
        return "ING-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Verifica se o ingresso é válido
     */
    public boolean isValido() {
        return StatusIngresso.VALIDO.equals(this.status);
    }

    /**
     * Verifica se o ingresso pode ser utilizado
     */
    public boolean podeSerUtilizado() {
        return isValido() && !this.sessao.isPassada();
    }

    /**
     * Marca o ingresso como utilizado
     */
    public void marcarComoUtilizado() {
        if (isValido()) {
            this.status = StatusIngresso.UTILIZADO;
        }
    }

    /**
     * Cancela o ingresso
     */
    public void cancelar() {
        if (isValido()) {
            this.status = StatusIngresso.CANCELADO;
        }
    }

    /**
     * Reembolsa o ingresso
     */
    public void reembolsar() {
        if (isValido() || StatusIngresso.UTILIZADO.equals(this.status)) {
            this.status = StatusIngresso.REEMBOLSADO;
        }
    }

    /**
     * Retorna o valor formatado como string
     */
    public String getValorFormatado() {
        return String.format("R$ %.2f", this.valor);
    }

    /**
     * Retorna a data da sessão formatada
     */
    public String getDataSessaoFormatada() {
        return this.sessao.getDataSessao().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Retorna o horário da sessão formatado
     */
    public String getHorarioSessaoFormatado() {
        return this.sessao.getHorario().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Retorna o nome do evento
     */
    public String getNomeEvento() {
        return this.sessao.getEvento().getNome();
    }

    /**
     * Retorna informações resumidas do ingresso
     */
    public String getResumo() {
        return String.format("%s - %s - %s - Poltrona %d", 
            getNomeEvento(),
            getDataSessaoFormatada(),
            getHorarioSessaoFormatado(),
            this.numeroPoltrona);
    }

    /**
     * Verifica se o ingresso pertence a um usuário específico
     */
    public boolean pertenceAoUsuario(Long usuarioId) {
        return this.usuario != null && this.usuario.getId().equals(usuarioId);
    }

    /**
     * Retorna a chave única de ocupação da poltrona
     * Formato: evento_id:data:horario:area_id:poltrona_id
     */
    public String getChaveOcupacao() {
        return String.format("%d:%s:%s:%d:%d",
            this.sessao.getEvento().getId(),
            this.sessao.getDataSessao(),
            this.sessao.getHorario(),
            this.area.getId(),
            this.numeroPoltrona);
    }
} 