package org.kush.share.api.controller;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.controller.services.ListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/list")
@RequiredArgsConstructor
public class UserListController
{
    private final ListService listService;

    @GetMapping
    public List<UserListDto> getUserList()
    {
        return listService.getList();
    }
}
