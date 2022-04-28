package com.dbc.vemserback.ecommerce.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dbc.vemserback.ecommerce.controller.others.Admin;
import com.dbc.vemserback.ecommerce.dto.user.UserCreateDTO;
import com.dbc.vemserback.ecommerce.dto.user.UserPageDTO;
import com.dbc.vemserback.ecommerce.enums.Groups;
import com.dbc.vemserback.ecommerce.exception.BusinessRuleException;
import com.dbc.vemserback.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
@Api(value = "7 - Admin API", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"7 - Admin API"})
public class AdminController implements Admin{
    private final UserService userService;


    @PostMapping(path = "/adm-creat-user", consumes = {MULTIPART_FORM_DATA_VALUE})
    public void admCreateUser(@Valid @ModelAttribute(name = "data") UserCreateDTO userCreateDTO, @RequestPart(name = "file",required = false) MultipartFile file, BindingResult bindingResult) throws BusinessRuleException {
        if(bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            bindingResult.getAllErrors().forEach(err -> builder.append(err.getDefaultMessage()));
            throw new BusinessRuleException(builder.toString());
        }
        userService.createUser(userCreateDTO, file);
    }

    @PutMapping("/adm-set-group-user")
    public void admSetGroupUser(@RequestParam Groups groups, @RequestParam Integer idUser) throws BusinessRuleException {
        userService.updateUserbyAdmin(groups, idUser);
    }

    @GetMapping("/adm-get-all-users")
    public Page<UserPageDTO> admGetAllUsers(@RequestParam int page, @RequestParam(required = false) String fullname) {
        return userService.listUsersForAdmin(page, fullname);
    }
    
//    @GetMapping("/adm-get-all-users-by-full-name")
//    public List<UserPageDTO> admGetAllUsersByFullName(@RequestParam String nome){
//        return userService.getByFullName(nome);
//    }
}
