package unibo.account;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@Controller
public class HIController {

    @Value("${spring.application.name}")
    String appName;

    @JsonView(value = { Views.Public.class })
    List<Account> listAccounts ;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("arg", appName);
        return "CmdGui";
    }

    @PostMapping("/cmd")
    public String doCmd(Model model,  @RequestParam( "cmdtodo" ) String cmd )  {
        System.out.println("doCmd:" + cmd);
        if(cmd.equals("/")) {
            listAccounts = AccountApi.apiService.listAll();
            System.out.println("listAccounts:" + listAccounts);
            model.addAttribute("answer", listAccounts);
        }else if(cmd.equals("/deposit")) {

        }
        return  "CmdGui";
    }

    @PostMapping("/cmdJson")
    public ResponseEntity doCmdJson(Model model, @RequestParam( "cmdtodo" ) String cmd )  {
        System.out.println("doCmdJson:" + cmd);
        listAccounts = AccountApi.apiService.listAll();
        System.out.println("listAccounts:" + listAccounts);
        return  new ResponseEntity(listAccounts, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "HIController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }
}
