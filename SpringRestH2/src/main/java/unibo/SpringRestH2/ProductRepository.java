package unibo.SpringRestH2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;


@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("UPDATE Product a SET a.description = a.description + ?1 WHERE a.id = ?2")
    @Modifying
    @Transactional
    public void changeDescr(String newDscr, Integer id);

    @Query("UPDATE Product a SET a.price = a.price + ?1 WHERE a.id = ?2")
    @Modifying
    @Transactional
    public void changePrice(Double newPrice, Integer id);

}
