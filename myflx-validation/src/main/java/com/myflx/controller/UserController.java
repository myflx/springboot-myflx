package com.myflx.controller;

import com.myflx.validation.payload.dto.Author;
import com.myflx.validation.payload.dto.Book;
import com.myflx.validation.payload.dto.Reader;
import com.myflx.validation.payload.severity.Address;
import com.myflx.validation.payload.validator.ContextValidatorGetter;
import com.myflx.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import java.util.Set;

/**
 * 控制器
 *
 * @author LuoShangLin
 * @date 2019/8/23 21:27
 * @since 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ContextValidatorGetter contextValidatorGetter;

    @RequestMapping("/create")
    public UserVO hello(@Valid @RequestBody UserVO user) {
        return user;
    }

    @GetMapping("/getCity")
    public String getCity(){
        Address address = new Address();
        Set<ConstraintViolation<Address>> validate = contextValidatorGetter.getValidator().validate(address);
        validate.forEach(addressConstraintViolation -> {
            Set<Class<? extends Payload>> payload = addressConstraintViolation.getConstraintDescriptor().getPayload();
            payload.forEach(p->{
                System.out.println("payload class-->"+p.getName());
            });
            System.out.println("错误信息："+addressConstraintViolation.getMessage());
        });
        return address.getCity();
    }
    @GetMapping("/getBook")
    public Book getBook(){
        Author author = new Author();
        author.setCompany("ACME");
        Book book = new Book();
        book.setTitle("");
        book.setAuthor(author);
        Set<ConstraintViolation<Book>> constraintViolations = contextValidatorGetter.getValidator().validate(book);
        constraintViolations.forEach(addressConstraintViolation -> {
            System.out.println("错误信息："+addressConstraintViolation.getMessage());
        });
//        BeanDescriptor constraintsForClass = contextValidatorGetter.getValidator().getConstraintsForClass(Book.class);
        return book;
    }


    @RequestMapping("/showReader")
    public Reader showAuthor(@Valid @RequestBody Reader author) {
        return author;
    }
}
