package com.myflx.controller;

import com.google.common.collect.Lists;
import com.myflx.validation.IValid;
import com.myflx.validation.annotation.ValidParam;
import com.myflx.validation.payload.dto.Author;
import com.myflx.validation.payload.dto.Book;
import com.myflx.validation.payload.dto.Reader;
import com.myflx.validation.payload.severity.Address;
import com.myflx.validation.payload.validator.ContextValidatorGetter;
import com.myflx.validator.ValidatorService;
import com.myflx.vo.UserVO;
import com.myflx.vo.UserVO2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * 控制器
 *
 * @author LuoShangLin
 * @date 2019/8/23 21:27
 * @since 1.0
 **/
@Validated
@RestController
@Api(tags = {"用户控制器", "用户", "V1.0"})
public class UserController {

    @ExceptionHandler(ConstraintViolationException.class)
    public String validHandler(ConstraintViolationException e){
        final List<ConstraintViolation> objects = Lists.newArrayList();
        objects.addAll(e.getConstraintViolations());
        return objects.get(0).getMessage();
    }

    @Autowired
    private ContextValidatorGetter contextValidatorGetter;

    @Autowired
    private ValidatorService validatorService;

    @RequestMapping("/create")
    @ApiOperation(value = "新增", notes = "新增", tags = {"V1.0"}, httpMethod = "POST")
    public UserVO hello(@ValidParam(UserVO.AddUserGroup.class) @RequestBody UserVO user) {
        return user;
    }

    @GetMapping("/getCity")
    public String getCity() {
        Address address = new Address();
        Set<ConstraintViolation<Address>> validate = contextValidatorGetter.getValidator().validate(address);
        validate.forEach(addressConstraintViolation -> {
            Set<Class<? extends Payload>> payload = addressConstraintViolation.getConstraintDescriptor().getPayload();
            payload.forEach(p -> {
                System.out.println("payload class-->" + p.getName());
            });
            System.out.println("错误信息：" + addressConstraintViolation.getMessage());
        });
        return address.getCity();
    }

    @GetMapping("/getBook")
    public Book getBook() {
        Author author = new Author();
        author.setCompany("ACME");
        Book book = new Book();
        book.setTitle("");
        book.setAuthor(author);
        Set<ConstraintViolation<Book>> constraintViolations = contextValidatorGetter.getValidator().validate(book);
        constraintViolations.forEach(addressConstraintViolation -> {
            System.out.println("错误信息：" + addressConstraintViolation.getMessage());
        });
        //BeanDescriptor constraintsForClass = contextValidatorGetter.getValidator().getConstraintsForClass(Book.class);
        return book;
    }


    @RequestMapping("/showReader")
    public Reader showAuthor(@Valid @RequestBody Reader author) {
        return author;
    }

    @GetMapping("/valid")
    public String valid() {
        Address address = new Address();
        validatorService.validBase(address);

        validatorService.valid(address);
        return address.getCity();
    }


    @RequestMapping("/wrongGroup")
    public UserVO2 wrongGroup(@ValidParam(UserVO2.class) @RequestBody UserVO2 user) {
        return user;
    }

    @RequestMapping("/group")
    public UserVO2 group(@ValidParam(UserVO2.AddUserGroup.class) @RequestBody UserVO2 user) {
        return user;
    }


    @RequestMapping("/failFast")
    public String failFast(@ValidParam(UserVO2.AddUserGroup.class) @RequestBody UserVO2 user) {
        return "hello word!";
    }


    @RequestMapping("/testDigits")
    public String testDigits(@ValidParam(UserVO2.DigitGroup.class) @RequestBody UserVO2 user) {
        return "";
    }


    @RequestMapping("/testI18n")
    public String testI18n(@ValidParam(UserVO2.I18nGroup.class) @RequestBody UserVO2 user) {
        return "";
    }


    @RequestMapping("/testTypeValid")
    public String testTypeValid(@ValidParam(IValid.class) @RequestBody UserVO user) {
        return "ok";
    }


    @Autowired
    UserService userService;
    @RequestMapping("/testMethodValid")
    @ApiOperation(value = "新增", notes = "新增", tags = {"V1.0"}, httpMethod = "POST")
    public String testMethodValid(@RequestBody @Validated UserVO user) {
        final String language = userService.getLanguage("1");
        final String aDefault = userService.getDefault();
        System.out.println(language);
        System.out.println(aDefault);
        return "Hello,world!";
    }
}
