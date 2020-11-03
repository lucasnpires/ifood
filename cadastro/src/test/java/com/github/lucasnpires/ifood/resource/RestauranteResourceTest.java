package com.github.lucasnpires.ifood.resource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response.Status;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.lucasnpires.ifood.config.CadastroTestLifecycleManager;
import com.github.lucasnpires.ifood.entity.Restaurante;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(value = CadastroTestLifecycleManager.class)
class RestauranteResourceTest {

	@Test
	@DataSet("restaurantes-cenario-1.yml")
	void testBuscarRestaurantes() {
		String resultado = given().when().get("/restaurantes").then().statusCode(Status.OK.getStatusCode()).extract()
				.asString();

		Approvals.verifyJson(resultado);
	}

	@Test
	void testCadastrarRestaurante() {
		Restaurante restaurante = new Restaurante("ID do Keyclock 3", "Restaurante 3");
		given().contentType(ContentType.JSON).when().body(restaurante).post("/restaurantes").then()
				.statusCode(Status.CREATED.getStatusCode());

		Restaurante findById = Restaurante.findById(1L);

		assertThat(restaurante.getNome()).isEqualTo(findById.getNome());
		assertThat(restaurante.getProprietario()).isEqualTo(findById.getProprietario());
	}

	@Test
	@DataSet("restaurantes-cenario-1.yml")
	void testUpdateRestaurante() {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome("Novo Restaurante 1");

		given().contentType(ContentType.JSON).when().body(restaurante).put("/restaurantes/1").then()
				.statusCode(Status.NO_CONTENT.getStatusCode());

		Restaurante findById = Restaurante.findById(1L);

		assertThat(restaurante.getNome()).isEqualTo(findById.getNome());
	}

	@Test
	void testUpdateRestauranteNotFound() {
		given().contentType(ContentType.JSON).when().body(new Restaurante()).put("/restaurantes/6").then()
				.statusCode(Status.NOT_FOUND.getStatusCode());
	}

	@Test
	@DataSet("restaurantes-cenario-1.yml")
	void testDeleteRestaurante() {
		given().contentType(ContentType.JSON).when().delete("/restaurantes/1").then()
				.statusCode(Status.NO_CONTENT.getStatusCode());
	}

	@Test
	void testDeleteRestauranteNotFound() {
		given().contentType(ContentType.JSON).when().delete("/restaurantes/10").then()
				.statusCode(Status.NOT_FOUND.getStatusCode());
	}
}
