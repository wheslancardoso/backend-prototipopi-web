package com.teatro.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma reserva temporária de poltrona
 * 
 * As reservas têm um tempo de expiração e podem ser convertidas em ingressos ou expiradas
 * automaticamente.
 */
@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reserva {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sessao_id", nullable = false)
  private Sessao sessao;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "area_id", nullable = false)
  private Area area;

  @Column(name = "numero_poltrona", nullable = false)
  private Integer numeroPoltrona;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @CreatedDate
  @Column(name = "data_reserva", nullable = false, updatable = false)
  private LocalDateTime dataReserva;

  @Column(name = "expira_em", nullable = false)
  private LocalDateTime expiraEm;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status = Status.ATIVA;

  /**
   * Enum que define os status da reserva
   */
  public enum Status {
    ATIVA("Ativa"), EXPIRADA("Expirada"), CONVERTIDA("Convertida em Ingresso"), CANCELADA(
        "Cancelada");

    private final String descricao;

    Status(String descricao) {
      this.descricao = descricao;
    }

    public String getDescricao() {
      return descricao;
    }
  }

  /**
   * Construtor para criação de reserva
   */
  public Reserva(Sessao sessao, Area area, Integer numeroPoltrona, Usuario usuario,
      LocalDateTime expiraEm) {
    this.sessao = sessao;
    this.area = area;
    this.numeroPoltrona = numeroPoltrona;
    this.usuario = usuario;
    this.expiraEm = expiraEm;
    this.status = Status.ATIVA;
  }

  /**
   * Verifica se a reserva está expirada
   */
  public boolean isExpirada() {
    return LocalDateTime.now().isAfter(this.expiraEm);
  }

  /**
   * Verifica se a reserva está ativa
   */
  public boolean isAtiva() {
    return Status.ATIVA.equals(this.status) && !isExpirada();
  }

  /**
   * Marca a reserva como convertida em ingresso
   */
  public void marcarComoConvertida() {
    this.status = Status.CONVERTIDA;
  }

  /**
   * Marca a reserva como expirada
   */
  public void marcarComoExpirada() {
    this.status = Status.EXPIRADA;
  }

  /**
   * Cancela a reserva
   */
  public void cancelar() {
    this.status = Status.CANCELADA;
  }
}
