package tacos.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.DiscountCodeProps;

@Controller
@RequestMapping("/discounts")
@AllArgsConstructor
public class DiscountController {

    private DiscountCodeProps props;

    @GetMapping
    public String displayDiscountCodes(Model model) {
        model.addAttribute("codes", props.getCodes());
        return "discountList";
    }
}
