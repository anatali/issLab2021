package unibo.SpringRestH2.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unibo.SpringRestH2.Category;
import unibo.SpringRestH2.Product;

import java.util.List;

@RestController
public class ProductApi {

    private ProductService productService ;

    public ProductApi(ProductService service) {
        System.out.println("ProductApi CREATED service=" + service);
        this.productService = service;
    }


    @RequestMapping(value = "/allProducts", method = RequestMethod.GET)
    public List<Product> getCategories() {
        List<Product> listCategs = productService.listAll();
        return listCategs;
    }

    @PatchMapping("/{id}/changeProductPrice")
    public HttpEntity<Product> changeProductPrice(@PathVariable("id") Integer id, @RequestBody Double newprice) {
        System.out.println("%%% changeProductPrice id="+id + " newprice="+newprice);
        Product updated  = productService.updatePrice(newprice, id);
        return new ResponseEntity<Product>(updated , HttpStatus.OK);
    }
}


