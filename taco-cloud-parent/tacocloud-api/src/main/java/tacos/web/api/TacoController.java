package tacos.web.api;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/tacos",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://tacocloud:8080")
@AllArgsConstructor
public class TacoController {

    private TacoRepository tacoRepo;

    private OrderRepository orderRepo;

    @GetMapping(params = "recent")
    public Iterable<Taco> recentTacos() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepo.findAll(pageable).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable Long id) {
        Optional<Taco> tacoOpt = tacoRepo.findById(id);
        return tacoOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Taco> postTaco(@RequestBody Taco taco) {
        return ResponseEntity.created(URI.create("/api/tacos")).body(tacoRepo.save(taco));
    }

    @PutMapping(path = "/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TacoOrder> putOrder(@PathVariable Long orderId, @RequestBody TacoOrder order) {
        order.setId(orderId);
        return ResponseEntity.ok(orderRepo.save(order));

    }

    @PatchMapping(path = "/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TacoOrder> patchOrder(@PathVariable Long orderId, @RequestBody TacoOrder patch) {
        TacoOrder order = orderRepo.findById(orderId).get();
        if (patch.getDeliveryName() != null) {
            order.setDeliveryName(patch.getDeliveryName());
        }
        if (patch.getDeliveryStreet() != null) {
            order.setDeliveryStreet(patch.getDeliveryStreet());
        }
        if (patch.getDeliveryCity() != null) {
            order.setDeliveryCity(patch.getDeliveryCity());
        }
        if (patch.getDeliveryState() != null) {
            order.setDeliveryState(patch.getDeliveryState());
        }
        if (patch.getDeliveryZip() != null) {
            order.setDeliveryZip(patch.getDeliveryZip());
        }
        if (patch.getCcNumber() != null) {
            order.setCcNumber(patch.getCcNumber());
        }
        if (patch.getCcExpiration() != null) {
            order.setCcExpiration(patch.getCcExpiration());
        }
        if (patch.getCcCVV() != null) {
            order.setCcCVV(patch.getCcCVV());
        }
        return ResponseEntity.ok(orderRepo.save(order));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderRepo.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }
}
