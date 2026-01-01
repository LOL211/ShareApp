package org.kush.share.api.controller;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.controller.services.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/list")
@RequiredArgsConstructor
public class ListController
{
    private final ListService listService;

    @GetMapping
    public ResponseEntity<List<UserListDto>> getUserList(Authentication authentication)
    {
        return ResponseEntity.ok(listService.getList((String) authentication.getPrincipal()));
    }

    @PostMapping
    public ResponseEntity<String> createNewUserList(Authentication authentication, @RequestBody UserListDto userListDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body("Created list "+ listService.createUserList((String) authentication.getPrincipal(), userListDto));
    }
}
