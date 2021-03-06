package services;

import java.util.ArrayList;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Carpeta;
import domain.DenunciaValoracion;
import domain.Evento;
import domain.Pregunta;
import domain.Reserva;
import domain.Usuario;
import forms.UsuarioRegistroForm;

import repositories.UsuarioRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class UsuarioService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	// Supporting services ----------------------------------------------------

	
	@Autowired
	private CarpetaService carpetaService;
	
	// Constructors -----------------------------------------------------------

	public UsuarioService() {
		super();
		
	}
	// Simple CRUD methods ----------------------------------------------------

	public Usuario create(){
		Usuario usuario = new Usuario();
		
		Authority authority = new Authority();
		UserAccount userAccount = new UserAccount();
				
		authority.setAuthority(Authority.USUARIO);
		userAccount.addAuthority(authority);
		usuario.setUserAccount(userAccount);
		
		Collection<Reserva> reservas	= new ArrayList<Reserva>();
		Collection<Evento> eventos		= new ArrayList<Evento>();
		Collection<Pregunta> preguntas	= new ArrayList<Pregunta>();
		Collection<Carpeta> carpetas	= new ArrayList<Carpeta>();
		Collection<DenunciaValoracion> denuncias = new ArrayList<DenunciaValoracion>();
		
		usuario.setReservas(reservas);
		usuario.setEventos(eventos);	
		usuario.setPreguntas(preguntas);	
		usuario.setCarpetas(carpetas);
		usuario.setDenuncias(denuncias);
		
		return usuario;
	}
	
	public void save(Usuario usuario){
		Assert.notNull(usuario);
		
		String password = usuario.getUserAccount().getPassword();
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);
		
		usuario.getUserAccount().setPassword(password);
		
		Usuario saved = usuarioRepository.save(usuario);
		carpetaService.createDefaultCarpetas(saved);
					
		usuarioRepository.save(saved);
				
	}
	
	// Other business methods ------------------------------------------------

	public Usuario findByUserAccount(int id){
		Assert.isTrue(id != 0);
		Usuario res;
		 
		 res = this.usuarioRepository.findByUserAccountId(id);
		 Assert.notNull(res);
		 
		 return res;
	}
	
	public Usuario findOne(int id){
		Assert.isTrue(id != 0);
		Usuario res;
		 
		 res = this.usuarioRepository.findOne(id);
		 Assert.notNull(res);
		 
		 return res;
	}
	
	public Usuario findByPrincipal(){
		
		UserAccount principalAccount = LoginService.getPrincipal();
		
		Usuario principal = findByUserAccount(principalAccount.getId());
		return principal;		
	}
	
	public Collection<Usuario> findAll(){
		return usuarioRepository.findAll();
	}		
		
	public void checkPrincipal(){
		UserAccount userAccount = LoginService.getPrincipal();
		
		Collection<Authority> authorities = userAccount.getAuthorities();
		
		Authority auth = new Authority();
		auth.setAuthority("ATHLETE");
		
		Assert.isTrue(authorities.contains(auth));
	 }

	
	public Usuario reconstruct(UsuarioRegistroForm form) {
		
		Usuario usuario=create();
		
		usuario.setEmail(form.getRegistroForm().getEmail());
		usuario.setNombre(form.getRegistroForm().getNombre());
		usuario.setApellidos(form.getRegistroForm().getApellidos());
		
		usuario.setTelefono(form.getRegistroForm().getTelefono());
		usuario.setFechaNacimiento(form.getRegistroForm().getFechaNacimiento());
		usuario.setNacionalidad(form.getRegistroForm().getNacionalidad());
		usuario.setDireccion(form.getRegistroForm().getDireccion());
		usuario.setSexo(form.getRegistroForm().getSexo());
		
		usuario.setEstadoActual(form.getEstadoActual());
		usuario.setNivelColaborador(form.getNivelColaborador());
		usuario.setPuntos(form.getPuntos());
		
		usuario.getUserAccount().setUsername(form.getRegistroForm().getUsername());
		usuario.getUserAccount().setPassword(form.getRegistroForm().getPassword());
		
		return usuario;
	}


	}
