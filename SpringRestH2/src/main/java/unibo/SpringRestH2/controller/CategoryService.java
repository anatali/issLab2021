package unibo.SpringRestH2.controller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unibo.SpringRestH2.Category;
import unibo.SpringRestH2.CategoryRepository;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    private CategoryRepository repo;

    public CategoryService(CategoryRepository repo){
        System.out.println("CategoryService CREATED repo=" + repo);
        this.repo = repo;
    }

    List<Category> listAll() {
        return repo.findAll();
    }

    Category get(Integer id) {
        return repo.findById(id).get();
    }

    Category updateDescr( String newDscr, Integer id ){
        repo.changeDescr(newDscr, id);
        return repo.findById(id).get();
    }
}
