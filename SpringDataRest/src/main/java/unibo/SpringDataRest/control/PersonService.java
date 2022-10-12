package unibo.SpringDataRest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unibo.SpringDataRest.model.Person;
import unibo.SpringDataRest.repo.PersonRepository;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    PersonRepository  personRepository;

    @Transactional
    public List<Person> getByName(String name) {
        return personRepository.findByLastName(name);
    }
}
