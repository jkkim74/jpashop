package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("itemForm",new BookForm());
        return "items/createItemForm";
    }
    @PostMapping("/items/new")
    public String create(@Valid BookForm bookForm, BindingResult result){

        if(result.hasErrors()){
            return "items/createItemForm";
        }

        Book book = new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.saveItem(book);

        return "redirect:/items";

    }

    @GetMapping("/items")
    public String itemList(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);

        BookForm itemForm = new BookForm();
        itemForm.setId(item.getId());
        itemForm.setName(item.getName());
        itemForm.setPrice(item.getPrice());
        itemForm.setStockQuantity(item.getStockQuantity());
        itemForm.setAuthor(item.getAuthor());
        itemForm.setIsbn(item.getIsbn());

        model.addAttribute("itemForm",itemForm);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(BookForm itemForm, Model model, BindingResult result){

        if(result.hasErrors()){
            return "items/createItemForm";
        }

//        Book book = new Book();
//        book.setId(itemForm.getId());//id를 넣으면 업데이트 쿼리가 발생함..
//        book.setName(itemForm.getName());
//        book.setPrice(itemForm.getPrice());
//        book.setStockQuantity(itemForm.getStockQuantity());
//        book.setAuthor(itemForm.getAuthor());
//        book.setIsbn(itemForm.getIsbn());
        BookDto bookDto = BookDto.builder().name(itemForm.getName()).price(itemForm.getPrice()).stockQuantity(itemForm.getStockQuantity()).build();
        itemService.updateItem(itemForm.getId(), bookDto);
        return "redirect:/items";

    }
}
