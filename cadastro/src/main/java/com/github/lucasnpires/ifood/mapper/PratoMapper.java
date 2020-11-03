package com.github.lucasnpires.ifood.mapper;

import org.mapstruct.Mapper;

import com.github.lucasnpires.ifood.dto.PratoDTO;
import com.github.lucasnpires.ifood.entity.Prato;

@Mapper(componentModel = "cdi")
public interface PratoMapper {
	
	public Prato toPrato(PratoDTO dto);

}
