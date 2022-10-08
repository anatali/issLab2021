package unibo.account;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountApi {
	public static AccountService apiService;

	private AccountService service;
	
	public AccountApi(AccountService service) {
		this.service = service;
		apiService   = service;
	}

	@GetMapping
	public List<Account> listAll() {
		System.out.println("%%% listAll"  );
		List<Account> listAccounts = service.listAll();
		return listAccounts;
	}

	
	@GetMapping("/{id}")
	public HttpEntity<Account> getOne(@PathVariable("id") Integer id) {
		System.out.println("%%% getOne id="+id  );
		try {
			Account account = service.get(id);
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public HttpEntity<Account> add(@RequestBody Account account) {
		System.out.println("%%% add account="+account  );
		Account savedAccount = service.save(account);
		return new ResponseEntity<>(savedAccount, HttpStatus.OK);
	}
	
	@PutMapping
	public HttpEntity<Account> replace(@RequestBody Account account) {
		System.out.println("%%% replace account="+account  );
		Account updatedAccount = service.save(account);
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
	}
	//curl -X PATCH -H "Content-Type: application/json" -d "{\"amount\": 100}"} localhost:8080/api/accounts/3/deposits
	@PatchMapping("/{id}/deposits")
	public HttpEntity<Account> deposit(@PathVariable("id") Integer id, @RequestBody Amount amount) {
		System.out.println("%%% deposit id="+id + " amount="+amount);
		Account updatedAccount = service.deposit(amount.getAmount(), id);
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
	}

	//curl -X PATCH -H "Content-Type: application/json" -d "{\"amount\": 100}"} localhost:8080/api/accounts/2/withdrawal
	@PatchMapping("/{id}/withdrawal")
	public HttpEntity<Account> withdraw(@PathVariable("id") Integer id, @RequestBody Amount amount) {
		System.out.println("%%% withdraw id="+id + " amount="+amount);
		Account updatedAccount = service.withdraw(amount.getAmount(), id);
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		System.out.println("%%% delete id="+id  );
		service.delete(id);
		return ResponseEntity.noContent().build();
	}


}

