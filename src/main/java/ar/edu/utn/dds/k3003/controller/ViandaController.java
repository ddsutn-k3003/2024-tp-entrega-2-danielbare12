package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.model.HeladeraDestino;
import ar.edu.utn.dds.k3003.model.Respuesta;
import io.javalin.http.HttpStatus;
import io.javalin.http.Context;

public class ViandaController {
  private final Fachada fachada;

  public ViandaController(Fachada fachada){
    this.fachada = fachada;
  }

  public void agregar(Context context){
    var ViandaDto = context.bodyAsClass(ViandaDTO.class);
    var ViandaDtoRta = this.fachada.agregar(ViandaDto);
    context.json(ViandaDtoRta);
    context.status(HttpStatus.CREATED);
  }

  public void buscarPorColaboradorIdMesYAnio(Context context){
    Long colabId = Long.valueOf(context.queryParam("colaboradorId"));
    Integer mes = Integer.valueOf(context.queryParam("colaboradorId"));
    Integer anio = Integer.valueOf(context.queryParam("colaboradorId"));
    var ViandaDtoRta = this.fachada.viandasDeColaborador(colabId,mes,anio);
    context.json(ViandaDtoRta);
  }

  public void buscarPorQr(Context context){
    String qr = context.queryParam("qr");
    var ViandaDtoRta = this.fachada.buscarXQR(qr);
    context.json(ViandaDtoRta);
  }

  public void verificarVencimiento(Context context){
    String qr = context.queryParam("qr");
    var respuesta = new Respuesta(this.fachada.evaluarVencimiento(qr));
    context.json(respuesta);
  }

  public void modificarHeladera(Context context){
    String qr = context.queryParam("qr");
    HeladeraDestino heladera = context.bodyAsClass(HeladeraDestino.class);
    var ViandaDtoRta = this.fachada.modificarHeladera(qr,heladera.getHeladeraDestino());
    context.json(ViandaDtoRta);
  }
}
