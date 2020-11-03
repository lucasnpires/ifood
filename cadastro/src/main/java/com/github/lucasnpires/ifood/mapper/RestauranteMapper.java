package com.github.lucasnpires.ifood.mapper;

import org.mapstruct.Mapper;

import com.github.lucasnpires.ifood.dto.RestauranteDTO;
import com.github.lucasnpires.ifood.entity.Restaurante;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {
	
	public Restaurante toRestaurante(RestauranteDTO dto);

}
