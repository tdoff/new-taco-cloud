package tacos.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.service.OrderAdminService;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private OrderAdminService adminService;

    @GetMapping
    public String showAdminPage() {
        return "admin";
    }


    @PostMapping("/deleteOrders")
    public String deleteOrders() {
        adminService.deleteAllOrders();
        return "redirect:/admin";
    }
}
