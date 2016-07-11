package domain;


import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Administrador extends Actor{

	// Attributes ------------------------------
	
	// Relationships ---------------------------
	
	private Collection<PeticionNegocio> peticionNegocios;
	private Collection<Playa> playas;

	@NotNull
	@Valid
	@OneToMany(mappedBy="administrador")
	public Collection<PeticionNegocio> getPeticionNegocios() {
		return peticionNegocios;
	}

	public void setPeticionNegocios(Collection<PeticionNegocio> peticionNegocios) {
		this.peticionNegocios = peticionNegocios;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy="administrador")
	public Collection<Playa> getPlayas() {
		return playas;
	}

	public void setPlayas(Collection<Playa> playas) {
		this.playas = playas;
	}

}
