package com.teatro.model;

import java.math.BigDecimal;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma área do teatro
 * 
 * Cada área tem: - Nome específico (Plateia A, Plateia B, Camarotes, etc.) - Preço fixo por
 * poltrona - Capacidade total de poltronas - Relacionamento com sessões
 */
@Entity
@Table(name = "areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da área é obrigatório")
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @NotNull(message = "Capacidade total é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser pelo menos 1")
    @Column(name = "capacidade_total", nullable = false)
    private Integer capacidadeTotal;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamento com Sessões (N:N)
    @ManyToMany(mappedBy = "areas", fetch = FetchType.LAZY)
    private List<Sessao> sessoes = new ArrayList<>();

    // Relacionamento com Ingressos (1:N)
    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ingresso> ingressos = new ArrayList<>();

    // Relacionamento com Reservas (1:N)
    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    /**
     * Construtor para criação de área básica
     */
    public Area(String nome, BigDecimal preco, Integer capacidadeTotal) {
        this.nome = nome;
        this.preco = preco;
        this.capacidadeTotal = capacidadeTotal;
        this.ativo = true;
    }

    /**
     * Construtor para criação de área completa
     */
    public Area(String nome, BigDecimal preco, Integer capacidadeTotal, String descricao) {
        this.nome = nome;
        this.preco = preco;
        this.capacidadeTotal = capacidadeTotal;
        this.descricao = descricao;
        this.ativo = true;
    }

    /**
     * Verifica se a área está ativa
     */
    public boolean isAtivo() {
        return this.ativo != null && this.ativo;
    }

    /**
     * Calcula o número de poltronas disponíveis para uma sessão específica
     */
    public int getPoltronasDisponiveis(Long sessaoId) {
        if (sessaoId == null) {
            return this.capacidadeTotal;
        }

        long poltronasOcupadas = this.ingressos.stream()
                .filter(ingresso -> ingresso.getSessao().getId().equals(sessaoId)
                        && ingresso.getStatus() != Ingresso.Status.CANCELADO)
                .count();

        return this.capacidadeTotal - (int) poltronasOcupadas;
    }

    /**
     * Verifica se uma poltrona específica está disponível para uma sessão
     */
    public boolean isPoltronaDisponivel(Long sessaoId, Integer numeroPoltrona) {
        if (sessaoId == null || numeroPoltrona == null) {
            return false;
        }

        return this.ingressos.stream()
                .noneMatch(ingresso -> ingresso.getSessao().getId().equals(sessaoId)
                        && ingresso.getNumeroPoltrona().equals(numeroPoltrona)
                        && ingresso.getStatus() != Ingresso.Status.CANCELADO);
    }

    /**
     * Retorna o percentual de ocupação para uma sessão
     */
    public double getPercentualOcupacao(Long sessaoId) {
        if (this.capacidadeTotal == 0) {
            return 0.0;
        }

        int poltronasDisponiveis = getPoltronasDisponiveis(sessaoId);
        int poltronasOcupadas = this.capacidadeTotal - poltronasDisponiveis;

        return (double) poltronasOcupadas / this.capacidadeTotal * 100;
    }

    /**
     * Retorna o faturamento total da área para uma sessão
     */
    public BigDecimal getFaturamentoSessao(Long sessaoId) {
        return this.ingressos.stream()
                .filter(ingresso -> ingresso.getSessao().getId().equals(sessaoId)
                        && ingresso.getStatus() != Ingresso.Status.CANCELADO)
                .map(Ingresso::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Retorna o preço formatado como string
     */
    public String getPrecoFormatado() {
        return String.format("R$ %.2f", this.preco);
    }

    /**
     * Verifica se a área tem poltronas disponíveis para uma sessão
     */
    public boolean temPoltronasDisponiveis(Long sessaoId) {
        return getPoltronasDisponiveis(sessaoId) > 0;
    }

    /**
     * Retorna apenas os ingressos ativos (não cancelados)
     */
    public List<Ingresso> getIngressosAtivos() {
        return this.ingressos.stream().filter(i -> i.getStatus() != Ingresso.Status.CANCELADO)
                .toList();
    }

    /**
     * Retorna apenas as reservas ativas
     */
    public List<Reserva> getReservasAtivas() {
        return this.reservas.stream().filter(Reserva::isAtiva).toList();
    }

    /**
     * Ativa a área
     */
    public void ativar() {
        this.ativo = true;
    }

    /**
     * Desativa a área
     */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Retorna a capacidade formatada
     */
    public String getCapacidadeFormatada() {
        return String.format("%d poltronas", this.capacidadeTotal);
    }
}
