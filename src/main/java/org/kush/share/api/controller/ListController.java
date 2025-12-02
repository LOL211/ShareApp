package org.kush.share.api.controller;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.controller.services.ListService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
