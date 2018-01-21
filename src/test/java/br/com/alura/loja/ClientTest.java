package br.com.alura.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;

public class ClientTest {

	private static Client client;
	private static HttpServer server;

	@BeforeClass
	public static void setup() {
		server = Servidor.inicializaServidor();
		
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		
		client = ClientBuilder.newClient(config);
	}

	@AfterClass
	public static void endTest() {
		server.stop();
	}

	@Test
	public void testaQueAConexaoComOServidorExternoQueFunciona() {
		WebTarget target = client.target("http://www.mocky.io");
		String response = target.path("/v2/52aaf5bbee7ba8c60329fb7b").request().get(String.class);
		assertTrue(response.contains("Rua Vergueiro"));
	}

	@Test
	public void testaBuscaDeUmCarrinho() {
		WebTarget target = client.target("http://localhost:8080");
		//JAXB
		Carrinho carrinho = target.path("/carrinhos/1").request().get(Carrinho.class);
		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	@Test
	public void testaBuscaUmProjeto() {
		WebTarget target = client.target("http://localhost:8080");
		Projeto projeto = target.path("/projetos/1").request().get(Projeto.class);
		assertEquals("Minha loja", projeto.getNome());
	}

	@Test
	public void testaAdicionarUmCarrinhoValido() {
		WebTarget target = client.target("http://localhost:8080");

		Carrinho carrinho = new Carrinho();
		carrinho.setCidade("SJC");
		carrinho.setRua("Av. 2");
		carrinho.adiciona(new Produto(100, "Xbox", 1500.0, 1));

		//JAXB
		Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);
		Response response = target.path("/carrinhos").request().post(entity);

		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusInfo().getStatusCode());
		String location = response.getHeaderString("Location");
		Carrinho responseAfterCreated = client.target(location).request().get(Carrinho.class);
		assertTrue(responseAfterCreated.getProdutos().get(0).getNome().equals("Xbox"));
	}

	@Test
	public void testaAdicionarUmProjetoValido() {
		WebTarget target = client.target("http://localhost:8080");
		Projeto projeto = new Projeto(2, "DVD", 1988);
		Entity<Projeto> entity = Entity.entity(projeto, MediaType.APPLICATION_XML);

		Response response = target.path("/projetos").request().post(entity);
		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusInfo().getStatusCode());

		Projeto responseAfterCreated = client.target(response.getHeaderString("Location")).request().get(Projeto.class);
		assertTrue(responseAfterCreated.getNome().equals("DVD"));
	}

	@Test
	public void testaDeveDeletarUmProdutoDeUmCarrinhoPorId(){
		WebTarget target = client.target("http://localhost:8080");
		Response response = target.path("/carrinhos/1/produtos/6237").request().delete();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
	}
}
