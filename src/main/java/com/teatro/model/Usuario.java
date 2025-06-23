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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um usuário do sistema de teatro
 * 
 * Suporta dois tipos de usuário:
 * - ADMIN: Acesso completo ao sistema
 * - COMUM: Acesso limitado (compra e visualização de ingressos)
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00")
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "endereco", length = 200)
    private String endereco;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Column(name = "senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario = TipoUsuario.COMUM;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @CreatedDate
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamentos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ingresso> ingressos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PontosFidelidade> pontosFidelidade = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notificacao> notificacoes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    /**
     * Enum que define os tipos de usuário no sistema
     */
    public enum TipoUsuario {
        ADMIN("Administrador"),
        COMUM("Usuário Comum");

        private final String descricao;

        TipoUsuario(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    /**
     * Construtor para criação de usuário com dados básicos
     */
    public Usuario(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = TipoUsuario.COMUM;
        this.ativo = true;
    }

    /**
     * Construtor para criação de usuário admin
     */
    public Usuario(String nome, String cpf, String email, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
    }

    /**
     * Verifica se o usuário é administrador
     */
    public boolean isAdmin() {
        return TipoUsuario.ADMIN.equals(this.tipoUsuario);
    }

    /**
     * Verifica se o usuário está ativo
     */
    public boolean isAtivo() {
        return this.ativo != null && this.ativo;
    }

    /**
     * Calcula o total de pontos de fidelidade do usuário
     */
    public int getTotalPontosFidelidade() {
        return this.pontosFidelidade.stream()
                .mapToInt(pf -> {
                    if (pf.getTipoOperacao() == PontosFidelidade.TipoOperacao.GANHO) {
                        return pf.getPontos();
                    } else {
                        return -pf.getPontos();
                    }
                })
                .sum();
    }

    /**
     * Retorna apenas as notificações não lidas
     */
    public List<Notificacao> getNotificacoesNaoLidas() {
        return this.notificacoes.stream()
                .filter(n -> !n.isLida())
                .toList();
    }

    /**
     * Retorna apenas os ingressos ativos (não cancelados)
     */
    public List<Ingresso> getIngressosAtivos() {
        return this.ingressos.stream()
                .filter(i -> i.getStatus() != Ingresso.Status.CANCELADO)
                .toList();
    }
} 