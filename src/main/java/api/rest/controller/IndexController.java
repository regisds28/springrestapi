package api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.rest.model.Usuario;
import api.rest.repository.TelefoneRepository;
import api.rest.repository.UsuarioRepository;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> init() {

		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> usuario(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/codVenda/{venda}", produces = "application/json")
	public ResponseEntity<Usuario> venda(@PathVariable(value = "id") Long id,
			@PathVariable(value = "venda") Long venda) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
		
		for (int pos = 0; pos < usuario.getTelefone().size(); pos++) {
			usuario.getTelefone().get(pos).setUsuario(usuario);			
		}	
		
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}
	

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefone().size(); pos++) {
			usuario.getTelefone().get(pos).setUsuario(usuario);
		}

		Usuario usuarioTemp = usuarioRepository.findByLogin(usuario.getSenha());
		
		if(usuarioTemp.getSenha().equals(usuario.getLogin())){
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public ResponseEntity deletar(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return new ResponseEntity(HttpStatus.OK);

	}

}
