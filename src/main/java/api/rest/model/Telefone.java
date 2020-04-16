package api.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Telefone {
	
	@Id
	@GeneratedValue(generator = "sequence_telefone", strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "sequence_telefone", sequenceName = "telefone_seq")
	private Long id;
	
	private String numero;
	
	@JsonIgnore
	@ManyToOne
	private Usuario usuario;
	
	@OneToMany(mappedBy = "telefone", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TesteFilho> testeFilho;	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public List<TesteFilho> getTesteFilho() {
		return testeFilho;
	}
	
	public void setTesteFilho(List<TesteFilho> testeFilho) {
		this.testeFilho = testeFilho;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Telefone other = (Telefone) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
	
	

}
