package com.github.lucasnpires.ifood.resource;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
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

import com.github.lucasnpires.ifood.dto.PratoDTO;
import com.github.lucasnpires.ifood.dto.RestauranteDTO;
import com.github.lucasnpires.ifood.entity.Prato;
import com.github.lucasnpires.ifood.entity.Restaurante;
import com.github.lucasnpires.ifood.mapper.PratoMapper;
import com.github.lucasnpires.ifood.mapper.RestauranteMapper;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {
	
	@Inject
	private RestauranteMapper restauranteMapper;
	
	@Inject
	private PratoMapper pratoMapper;
	
	@GET
	@Tag(name = "restaurante")
	public List<Restaurante> listAll() {
		return Restaurante.listAll();
	}

	@POST
	@Tag(name = "restaurante")
	@Transactional
	public Response create(RestauranteDTO dto) {
		Restaurante restaurante = restauranteMapper.toRestaurante(dto);
		restaurante.persist();
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	@Tag(name = "restaurante")
	public void update(@PathParam("id") Long id, RestauranteDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException();
		}

		Restaurante restaurante = restauranteOp.get();
		restaurante.setNome(dto.getNome());
		restaurante.setCnpj(dto.getCnpj());
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
	public Response createPrato(@PathParam("idRestaurante") Long idRestaurante, PratoDTO dto) {
		
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		
		if(restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		
		Prato prato = pratoMapper.toPrato(dto);
		prato.setRestaurante(restauranteOp.get());
		prato.persist();
		
		return Response.status(Status.CREATED).build();
	}
	
	@PUT
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	@Tag(name = "prato")
	public void updatePrato(@PathParam("idRestaurante") Long idRestaurante,@PathParam("id") Long id, PratoDTO dto) {
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
		prato.setDescricao(dto.getDescricao());
		prato.setNome(dto.getNome());
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
