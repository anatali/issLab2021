package unibo.SpringRestH2.controller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unibo.SpringRestH2.Category;
import unibo.SpringRestH2.CategoryRepository;
import unibo.SpringRestH2.Product;
import unibo.SpringRestH2.ProductRepository;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private ProductRepository repo;

    public ProductService(ProductRepository repo){
        System.out.println("CategoryService CREATED repo=" + repo);
        this.repo = repo;
    }

    List<Product> listAll() {
        return repo.findAll();
    }

    Product get(Integer id) {
        return repo.findById(id).get();
    }

    Product updateDescr( String newDscr, Integer id ){
        repo.changeDescr(newDscr, id);
        return repo.findById(id).get();
    }
    Product updatePrice( Double newPrice, Integer id ){
        repo.changePrice(newPrice, id);
        return repo.findById(id).get();
    }


}
