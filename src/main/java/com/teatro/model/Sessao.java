package com.teatro.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma sessão de um evento teatral
 * 
 * Implementa a lógica de horários dinâmicos: - Horários fixos por período (Manhã, Tarde, Noite) -
 * Disponibilidade baseada na data e hora atual - Validação de datas passadas
 */
@Entity
@Table(name = "sessoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100)
    private String nome;

    @NotNull(message = "Tipo de sessão é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sessao", nullable = false)
    private TipoSessao tipoSessao;

    @NotNull(message = "Data da sessão é obrigatória")
    @Column(name = "data_sessao", nullable = false)
    private LocalDate dataSessao;

    @NotNull(message = "Horário da sessão é obrigatório")
    @Column(name = "horario", nullable = false)
    private LocalTime horario;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;

    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamento com Evento (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    // Relacionamento com Áreas (N:N)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sessoes_areas", joinColumns = @JoinColumn(name = "sessao_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private List<Area> areas = new ArrayList<>();

    // Relacionamento com Ingressos (1:N)
    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ingresso> ingressos = new ArrayList<>();

    // Relacionamento com Reservas (1:N)
    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    // Relacionamento com Avaliações (1:N)
    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    /**
     * Enum que define os tipos de sessão e seus horários fixos
     */
    public enum TipoSessao {
        MANHA("Manhã", List.of("08:00", "09:30", "11:00")), TARDE("Tarde",
                List.of("13:00", "14:30", "16:00")), NOITE("Noite",
                        List.of("18:00", "19:30", "21:00"));

        private final String descricao;
        private final List<String> horarios;

        TipoSessao(String descricao, List<String> horarios) {
            this.descricao = descricao;
            this.horarios = horarios;
        }

        public String getDescricao() {
            return descricao;
        }

        public List<String> getHorarios() {
            return horarios;
        }

        /**
         * Retorna os horários disponíveis para uma data específica baseado na data e hora atual
         */
        public List<String> getHorariosDisponiveis(LocalDate data, LocalTime horaAtual) {
            LocalDate hoje = LocalDate.now();

            // Se a data for passada, não há horários disponíveis
            if (data.isBefore(hoje)) {
                return new ArrayList<>();
            }

            // Se for hoje, filtra horários futuros
            if (data.equals(hoje)) {
                return this.horarios.stream().map(LocalTime::parse)
                        .filter(horario -> horario.isAfter(horaAtual)).map(LocalTime::toString)
                        .toList();
            }

            // Se for data futura, todos os horários estão disponíveis
            return new ArrayList<>(this.horarios);
        }
    }

    /**
     * Construtor para criação de sessão
     */
    public Sessao(TipoSessao tipoSessao, LocalDate dataSessao, LocalTime horario, Evento evento) {
        this.tipoSessao = tipoSessao;
        this.dataSessao = dataSessao;
        this.horario = horario;
        this.evento = evento;
        this.ativa = true;
    }

    /**
     * Construtor para criação de sessão com nome
     */
    public Sessao(String nome, TipoSessao tipoSessao, LocalDate dataSessao, LocalTime horario,
            Evento evento) {
        this.nome = nome;
        this.tipoSessao = tipoSessao;
        this.dataSessao = dataSessao;
        this.horario = horario;
        this.evento = evento;
        this.ativa = true;
    }

    /**
     * Verifica se a sessão está ativa
     */
    public boolean isAtiva() {
        return this.ativa != null && this.ativa;
    }

    /**
     * Verifica se a sessão já aconteceu (data/hora passada)
     */
    public boolean isPassada() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataHoraSessao = LocalDateTime.of(this.dataSessao, this.horario);
        return dataHoraSessao.isBefore(agora);
    }

    /**
     * Verifica se a sessão está disponível para compra
     */
    public boolean isDisponivelParaCompra() {
        return isAtiva() && !isPassada();
    }

    /**
     * Verifica se a sessão é hoje
     */
    public boolean isHoje() {
        return this.dataSessao.equals(LocalDate.now());
    }

    /**
     * Verifica se a sessão é amanhã
     */
    public boolean isAmanha() {
        return this.dataSessao.equals(LocalDate.now().plusDays(1));
    }

    /**
     * Verifica se a sessão é no futuro (mais de 1 dia)
     */
    public boolean isFutura() {
        return this.dataSessao.isAfter(LocalDate.now().plusDays(1));
    }

    /**
     * Adiciona uma área à sessão
     */
    public void adicionarArea(Area area) {
        this.areas.add(area);
    }

    /**
     * Remove uma área da sessão
     */
    public void removerArea(Area area) {
        this.areas.remove(area);
    }

    /**
     * Retorna a data e hora completa da sessão
     */
    public LocalDateTime getDataHoraCompleta() {
        return LocalDateTime.of(this.dataSessao, this.horario);
    }

    /**
     * Retorna o nome completo da sessão (Evento - Data - Horário)
     */
    public String getNomeCompleto() {
        if (this.evento != null) {
            return String.format("%s - %s - %s", this.evento.getNome(),
                    this.dataSessao
                            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    this.horario.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        }
        return this.nome;
    }

    /**
     * Retorna apenas as áreas ativas da sessão
     */
    public List<Area> getAreasAtivas() {
        return this.areas.stream().filter(Area::isAtivo).toList();
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
     * Calcula a média das avaliações da sessão
     */
    public double getMediaAvaliacoes() {
        if (this.avaliacoes.isEmpty()) {
            return 0.0;
        }

        return this.avaliacoes.stream().mapToInt(Avaliacao::getNota).average().orElse(0.0);
    }

    /**
     * Retorna a quantidade de avaliações da sessão
     */
    public int getQuantidadeAvaliacoes() {
        return this.avaliacoes.size();
    }

    /**
     * Ativa a sessão
     */
    public void ativar() {
        this.ativa = true;
    }

    /**
     * Desativa a sessão
     */
    public void desativar() {
        this.ativa = false;
    }
}
