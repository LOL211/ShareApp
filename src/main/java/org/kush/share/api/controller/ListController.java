package org.kush.share.api.controller;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.ListItemDto;
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

    @GetMapping("{listName}")
    public ResponseEntity<UserListDto> getUserList(Authentication authentication, @PathVariable("listName") String listName)
    {
        return ResponseEntity.ok(listService.getSingleList((String) authentication.getPrincipal(), listName));
    }

    @PostMapping
    public ResponseEntity<String> createNewUserList(Authentication authentication, @RequestBody UserListDto userListDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Created list "+ listService.createUserList((String) authentication.getPrincipal(), userListDto));
    }

    @PostMapping("{listName}")
    public ResponseEntity<String> createNewListItem(Authentication authentication, @PathVariable("listName") String listName, @RequestBody ListItemDto listItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Created item with desc "+ listService.createListItem((String) authentication.getPrincipal(), listName, listItemDto));
    }

    @DeleteMapping("{listName}")
    public ResponseEntity<String> deleteList(Authentication authentication, @PathVariable("listName") String listName)
    {
        listService.deleteList((String)authentication.getPrincipal(), listName);
        return ResponseEntity.status(HttpStatus.OK).body("List "+listName+" deleted");
    }

    @DeleteMapping("{listName}/{listItem}")
    public ResponseEntity<String> deleteListItem(Authentication authentication,
                                                 @PathVariable("listName") String listName,
                                                 @PathVariable("listItem") String listItem)
    {
        listService.deleteListItem((String)authentication.getPrincipal(), listName, listItem);
        return ResponseEntity.status(HttpStatus.OK).body("List "+listName+"'s item "+listItem+" deleted");
    }

    @GetMapping("share/{listName}")
    public ResponseEntity<String> createShareLinkForList(Authentication authentication, @PathVariable("listName") String listName) throws Exception
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(listService.createShareListLink((String) authentication.getPrincipal(), listName));
    }
}
