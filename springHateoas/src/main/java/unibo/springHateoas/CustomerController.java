package unibo.springHateoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class CustomerController {
//private Order ord = new Order("glass",100,10);
//private List<Order> orders = new Vector<Order>();

/*
public CustomerController(){
    ord=new Order("food",100,10);
    orders.add(ord);
}
*/

    //@Autowired
    private OrderService orderService;
/*
	@RequestMapping("/")
	Greet greet(){
		return new Greet("Hello World!");
	}
*/
	//http://localhost:8080/greeting?name=xxx
    @RequestMapping("/greeting")
    @ResponseBody
    public HttpEntity<Greet> greeting(@RequestParam(value = "name", required = false, defaultValue = "HATEOAS") String name) {
        Greet greet = new Greet("Hello " + name);
        Link l = linkTo(methodOn(CustomerController.class).greeting(name)).withSelfRel();
        greet.add(l);
        return new ResponseEntity<Greet>(greet, HttpStatus.OK);
    }


    @GetMapping("/{customerId}/{orderId}/start")
    public Order getOrderById(@PathVariable final String customerId, @PathVariable final String orderId) {
        System.out.println("qqqqqqqqqqqqqqqqqqqq " + customerId);
        return orderService.getOrderByIdForCustomer(customerId, orderId);
    }

    //http://localhost:8080/22/orders
    //curl http://localhost:8080/spring-security-rest/api/customers
    @GetMapping(value = "/{customerId}/orders", produces = { "application/hal+json" })
    public CollectionModel<Order> getOrdersForCustomer(@PathVariable final String customerId) {
 	    System.out.println("............" + customerId);

        final List<Order> orders = orderService.getAllOrdersForCustomer(customerId);
        for (final Order order : orders) {
            final Link selfLink = linkTo(
                    methodOn(CustomerController.class).getOrderById(customerId, order.getOrderId())).withSelfRel();
            order.add(selfLink);
        }

        Link link = linkTo(methodOn(CustomerController.class).getOrdersForCustomer(customerId)).withSelfRel();
        CollectionModel<Order> result = CollectionModel.of(orders, link);
        return result;
	    /*
        List<Order> orders = orderService.getAllOrdersForCustomer(customerId);
        for (final Order order : orders) {
            Link selfLink = linkTo(methodOn(GreetingController.class).getOrderById(customerId, order.getOrderId())).withSelfRel();
            order.add(selfLink);
        }



        //Link selfLink = linkTo(methodOn(GreetingController.class).getOrdersForCustomer(customerId)).withSelfRel();
        //ord.add(selfLink);

        Link link = linkTo(methodOn(GreetingController.class).getOrdersForCustomer(customerId)).withSelfRel();
        CollectionModel<Order> result = CollectionModel.of(orders, link);
        return result;
 */
	}
}
/*
https://sookocheff.com/post/api/on-choosing-a-hypermedia-format/

HAL is a lightweight media type that uses the idea of
Resources and Links to model your JSON responses.
Resources can contain State defined by key-value pairs of data,
Links leading to additional resources and Embedded Resources which are
children of the current resource embedded in the representation for convenience.

Links in HAL are identified as a JSON object named _links.
Keys within _links are the name of the link and should describe
the relationship between the current resource and the link.
At a minimum the _links property should contain a self entry
pointing to the current resource.

 */