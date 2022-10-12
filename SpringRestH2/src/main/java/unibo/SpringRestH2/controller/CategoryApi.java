package unibo.SpringRestH2.controller;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unibo.SpringRestH2.Category;
import java.util.List;
import java.util.NoSuchElementException;

/*
If we define @RestController, the HAL controller does not work anymore
 */
@RestController
//@RepositoryRestController
public class CategoryApi {

    private CategoryService categoryService ;

    public CategoryApi(CategoryService service) {
        System.out.println("CategoryApi CREATED service=" + service);
        this.categoryService = service;
     }

    //@GetMapping("/")
    //Lasciamo libero - risponde HAL

    @RequestMapping(value = "/allCategories", method = RequestMethod.GET)
    public List<Category> getCategories() {
        List<Category> listCategs = categoryService.listAll();
        return listCategs;
    }

    @GetMapping("/{categoryid}")
    public HttpEntity<Category> getOneCategory(@PathVariable("categoryid") Integer id) {
        System.out.println("%%% getOneCategory id="+id  );
        try {
            Category categ = categoryService.get(id);
            return new ResponseEntity<>(categ, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{id}/changeCategoryDescription")
    public HttpEntity<Category> changeCategDescr(@PathVariable("id") Integer id, @RequestBody String newdescr) {
        System.out.println("%%% changeCategDescr id="+id + " newdescr="+newdescr);
        Category updated  = categoryService.updateDescr(newdescr, id);
        return new ResponseEntity<Category>(updated , HttpStatus.OK);
    }
}