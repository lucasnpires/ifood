package com.github.lucasnpires.ifood.config;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class CadastroTestLifecycleManager implements QuarkusTestResourceLifecycleManager {

	public static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:12.2");

	@Override
	public Map<String, String> start() {
		POSTGRESQL.start();

		Map<String, String> propriedades = new HashMap<>();
		propriedades.put("quarkus.datasource.jdbc.url", POSTGRESQL.getJdbcUrl());
		propriedades.put("quarkus.datasource.username", POSTGRESQL.getUsername());
		propriedades.put("quarkus.datasource.password", POSTGRESQL.getPassword());

		return propriedades;
	}

	@Override
	public void stop() {
		if(POSTGRESQL != null) {
			POSTGRESQL.stop();
		}	
	}

}
