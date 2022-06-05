package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderService;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 46259a1fd1dcf669c7ebac43a4ca729e95fd3676

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

<<<<<<< HEAD
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems ();

        model.addAttribute ("members",members);
        model.addAttribute ("items",items);

=======
    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping("/order")
    public String orderForm(Model model){

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members",members);
        model.addAttribute("items",items);
>>>>>>> 46259a1fd1dcf669c7ebac43a4ca729e95fd3676
        return "order/orderForm";

    }

<<<<<<< HEAD

=======
>>>>>>> 46259a1fd1dcf669c7ebac43a4ca729e95fd3676
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){
<<<<<<< HEAD
        orderService.order(memberId,itemId,count);
=======
        orderService.order(memberId, itemId, count);
>>>>>>> 46259a1fd1dcf669c7ebac43a4ca729e95fd3676
        return "redirect:/orders";
    }

    @GetMapping("/orders")
<<<<<<< HEAD
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute ("orders",orders);
        return "order/orderList";
    }
=======
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancel(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

>>>>>>> 46259a1fd1dcf669c7ebac43a4ca729e95fd3676
}