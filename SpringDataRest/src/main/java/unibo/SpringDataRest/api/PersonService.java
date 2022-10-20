package unibo.SpringDataRest.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.SpringDataRest.model.Person;

public interface PersonService {
    @GetMapping(value="/RestApi/person/{personId}", produces ="application/json")
    Person getPerson(@RequestParam int personId);
}
/*
 @GetMapping: specifica che il metodo getPerson è mappato a una richiesta
              HTTP GET con URL-path=/person/{personId}
              e che il formato della risposta è JSON.
 @PathVariable: specifica che il path variabile name {personId} della GET
              è mappato all'argomento personId.
              Ad esempio una HTTP GET /person/3 si traduce in una chiamata
              a getPerson(3)
 Il metodo getPerson restituisce un POJO di tipo Person,
              che costituisce il Data Transfer Object (DTO) usato per
              trasferire i dati di risposta al caller
 */