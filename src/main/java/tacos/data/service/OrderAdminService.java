package tacos.data.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tacos.data.OrderRepository;

@Service
@AllArgsConstructor
public class OrderAdminService {

    private OrderRepository orderRepo;

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllOrders() {
        orderRepo.deleteAll();
    }
}
