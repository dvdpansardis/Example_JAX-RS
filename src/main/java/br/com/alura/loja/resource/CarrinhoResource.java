package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

@Path("carrinhos")
public class CarrinhoResource {

	//JAXB padrão JAX-RS
	
	private CarrinhoDAO carrinhoDAO = new CarrinhoDAO();

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Carrinho busca(@PathParam("id") long id) {
		Carrinho carrinho = carrinhoDAO.busca(id);
		return carrinho;
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(Carrinho requestCarrinho) {
		//JAXB
		carrinhoDAO.adiciona(requestCarrinho);
		String uriResponse = "/carrinhos/" + requestCarrinho.getId();
		return Response.created(URI.create(uriResponse)).build();
	}

	@DELETE
	@Path("{id}/produtos/{produtoId}")
	public Response delete(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = carrinhoDAO.busca(id);
		carrinho.remove(produtoId);
		return Response.ok().build();
	}
	
	@PUT
	@Path("{id}/produtos/{produtoId}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response atualizar(@PathParam("id") long id, @PathParam("produtoId") long produtoId, Produto requestProduto){
		Carrinho carrinho = carrinhoDAO.busca(id);
		carrinho.troca(requestProduto);
		return Response.ok().build();
	}

	@PUT
	@Path("{id}/produtos/{produtoId}/quantidade")
	@Consumes(MediaType.APPLICATION_XML)
	public Response atualizarQuantidade(@PathParam("id") long id, @PathParam("produtoId") long produtoId, Produto requestProduto){
		Carrinho carrinho = carrinhoDAO.busca(id);
		carrinho.trocaQuantidade(requestProduto);
		return Response.ok().build();
	}
}
