package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.ProjetoDAO;
import br.com.alura.loja.modelo.Projeto;

@Path("projetos")
public class ProjetoResource {

	private ProjetoDAO projetoDAO = new ProjetoDAO();

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Projeto busca(@PathParam("id") long id){
		return projetoDAO.busca(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response adicionar(Projeto projetoRequest){
		projetoDAO.adiciona(projetoRequest);
		String uri = "/projetos/" + projetoRequest.getId();
		return Response.created(URI.create(uri)).build();
	}
	
	@DELETE
	@Path("/projetos/{id}")
	public Response remove(@PathParam("id") long id){
		projetoDAO.remove(id);
		return Response.ok().build();
	}
	
}
