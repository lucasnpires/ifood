package com.github.lucasnpires.ifood.resource;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import com.github.lucasnpires.ifood.entity.Prato;
import com.github.lucasnpires.ifood.entity.Restaurante;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {
	
	@GET
	@Tag(name = "restaurante")
	public List<Restaurante> listAll() {
		return Restaurante.listAll();
	}

	@POST
	@Tag(name = "restaurante")
	@Transactional
	public Response create(Restaurante dto) {
		dto.persist();
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	@Tag(name = "restaurante")
	public void update(@PathParam("id") Long id, Restaurante dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException();
		}

		Restaurante restaurante = restauranteOp.get();
		restaurante.setNome(dto.getNome());
		restaurante.persist();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	@Tag(name = "restaurante")
	public void update(@PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);

		restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
			throw new NotFoundException();
		});
	}
	
	@GET
	@Path("{idRestaurante}/pratos")
	@Tag(name = "prato")
	public List<Prato> listAllPratos(@PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		return Prato.list("restaurante", restauranteOp.get());
	}
	
	@POST
	@Path("{idRestaurante}/pratos")
	@Transactional
	@Tag(name = "prato")
	public Response createPrato(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		Prato prato = new Prato();
		prato.setNome(dto.getNome());
		prato.setDescricao(dto.getDescricao());
		prato.setPreco(dto.getPreco());
		prato.setRestaurante(restauranteOp.get());
		prato.persist();
		
		return Response.status(Status.CREATED).build();
	}
	
	@PUT
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void updatePrato(@PathParam("idRestaurante") Long idRestaurante,@PathParam("id") Long id, Prato dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);
		
		if(pratoOp.isEmpty()) {
			throw new NotFoundException("Prato não existe");
		}
		
		Prato prato = pratoOp.get();
		prato.setPreco(dto.getPreco());
		prato.persist();
		
	}
	
	@DELETE
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void deletePrato(@PathParam("idRestaurante") Long idRestaurante,@PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);
		
		pratoOp.ifPresentOrElse(Prato::delete, () -> {
			throw new NotFoundException("Prato não existe");
		});
	}
}
