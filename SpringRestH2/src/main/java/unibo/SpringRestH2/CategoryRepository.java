package unibo.SpringRestH2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;


@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("UPDATE Category a SET a.description = a.description + ?1 WHERE a.id = ?2")
    @Modifying
    @Transactional
    public void changeDescr(String newDscr, Integer id);



}
