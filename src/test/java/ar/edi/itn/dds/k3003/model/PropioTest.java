package ar.edi.itn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.tests.TestTP;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.tests.TestTP;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith({MockitoExtension.class})
public class PropioTest implements TestTP<FachadaViandas> {
  private static final Long COLABORADOR_ID = 12L;
  private static final Integer HELADERA_ID = 122;
  FachadaViandas instancia;
  ViandaDTO vianda;
  final LocalDateTime now = LocalDateTime.now();
  @Mock
  FachadaHeladeras fachadaHeladeras;
  public PropioTest() {
  }
  @SneakyThrows
  @BeforeEach
  void setUp() {
    try {
      this.instancia = (FachadaViandas)this.instance();
      this.instancia.setHeladerasProxy(this.fachadaHeladeras);
      this.vianda = new ViandaDTO("unQr", this.now, EstadoViandaEnum.PREPARADA, COLABORADOR_ID, HELADERA_ID);
    } catch (Throwable var2) {
      throw var2;
    }
  }

  @Test
  @DisplayName("Agregar vianda al repo")
  void testAgregarVianda() {
    this.instancia.agregar(this.vianda);
    this.instancia.agregar(new ViandaDTO("otroQr", this.now, EstadoViandaEnum.RETIRADA, COLABORADOR_ID, HELADERA_ID));
    this.instancia.agregar(new ViandaDTO("otroQrMas", this.now, EstadoViandaEnum.RETIRADA, COLABORADOR_ID, HELADERA_ID));
    Assertions.assertEquals(3, this.instancia.viandasDeColaborador(COLABORADOR_ID, this.now.getMonthValue(), this.now.getYear()).size());
  }
  @Test
  @DisplayName("Agregar viandas a colaborador")
  void testAgregarViandaAColaborador() {
    this.instancia.agregar(new ViandaDTO("otroQr", this.now, EstadoViandaEnum.RETIRADA, COLABORADOR_ID, HELADERA_ID));
    this.instancia.agregar(new ViandaDTO("otroQrMas", this.now, EstadoViandaEnum.RETIRADA, 11L, HELADERA_ID));
    Assertions.assertEquals(1, this.instancia.viandasDeColaborador(11L, this.now.getMonthValue(), this.now.getYear()).size());
  }

  @Test
  @DisplayName("Modificar estado de una vianda")
  void testModificarEstadoDeVianda() {
    this.instancia.agregar(this.vianda);
    this.instancia.modificarEstado(this.vianda.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
    EstadoViandaEnum estado = this.instancia.buscarXQR(this.vianda.getCodigoQR()).getEstado();
    Assertions.assertEquals(EstadoViandaEnum.DEPOSITADA, estado, "Se cambió el estado de una vianda pero no parece haberse guardado");
  }
  @Test
  @DisplayName("Buscar Por Qr una vianda")
  void testBuscarPorQr() {
    ViandaDTO viandaAgregada = this.instancia.agregar(this.vianda);
    this.instancia.agregar(new ViandaDTO("otroQr", this.now, EstadoViandaEnum.RETIRADA, COLABORADOR_ID, HELADERA_ID));
    this.instancia.agregar(new ViandaDTO("otroQrMas", this.now, EstadoViandaEnum.RETIRADA, COLABORADOR_ID, HELADERA_ID));
    ViandaDTO viandaEncontrada = this.instancia.buscarXQR("otroQrMas");
    Assertions.assertEquals(viandaAgregada.getColaboradorId(), viandaEncontrada.getColaboradorId(), "Al buscarXQR no se retorna la vianda correcta.");
  }
  @Test
  @DisplayName("Modificar heladera de la vianda")
  void testModificarHeladeraDeLaVianda() {
    this.instancia.agregar(this.vianda);
    int heladeraDestino = 2;
    this.instancia.modificarHeladera(this.vianda.getCodigoQR(), heladeraDestino);
    Assertions.assertEquals(heladeraDestino, this.instancia.buscarXQR(this.vianda.getCodigoQR()).getHeladeraId(), "No funcionó cambiar la vianda de heladera");
  }
  @Test
  @DisplayName("La fachada proxy funciona")
  void testEvaluarFachadaProxy() {
    this.instancia.setHeladerasProxy(this.fachadaHeladeras);
    ViandaDTO viandaAgregada = this.instancia.agregar(this.vianda);
    Mockito.when(this.fachadaHeladeras.obtenerTemperaturas(HELADERA_ID)).thenReturn(List.of(new TemperaturaDTO(3, HELADERA_ID, this.now), new TemperaturaDTO(-10, HELADERA_ID, this.now)));
    Assertions.assertEquals(this.fachadaHeladeras.obtenerTemperaturas(HELADERA_ID).size(),2, "La heladera tiene no tiene 2 temperaturas");
  }
  @Test
  @DisplayName("Ver si la vianda venció")
  void testEvaluarVencimiento() {
    this.instancia.setHeladerasProxy(this.fachadaHeladeras);
    ViandaDTO viandaAgregada = this.instancia.agregar(this.vianda);
    Mockito.when(this.fachadaHeladeras.obtenerTemperaturas(HELADERA_ID)).thenReturn(List.of(new TemperaturaDTO(3, HELADERA_ID, this.now), new TemperaturaDTO(-10, HELADERA_ID, this.now)));
    Assertions.assertFalse(this.instancia.evaluarVencimiento(viandaAgregada.getCodigoQR()), "La heladera tiene una temperatura mayor a 5 grados, asi que deberia considerar a la vianda como vencida");
  }

  public String paquete() {
    return "ar.edu.utn.dds.k3003.tests.viandas";
  }

  public Class<FachadaViandas> clase() {
    return FachadaViandas.class;
  }

}
