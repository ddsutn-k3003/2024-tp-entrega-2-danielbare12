package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class Vianda {
  private long id = -1;
  private String qr;
  private long colaboradorId;
  private Integer heladeraId;
  private EstadoViandaEnum estado;
  private LocalDateTime fechaElaboracion;

  public Vianda(String qr, long colaboradorId, Integer heladeraId, EstadoViandaEnum estado) {
    this.qr = qr;
    this.colaboradorId = colaboradorId;
    this.heladeraId = heladeraId;
    this.estado = estado;
    this.fechaElaboracion = LocalDateTime.now();
  }
}
