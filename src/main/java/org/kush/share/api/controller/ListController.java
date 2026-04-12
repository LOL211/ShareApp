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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/list")
@RequiredArgsConstructor
public class ListController
{
    private final ListService listService;

    @GetMapping
    public ResponseEntity<List<UserListDto>> getUserList(Authentication authentication)
    {
        return ResponseEntity.ok(listService.getList((String) authentication.getPrincipal()));
    }

    @GetMapping("{listId}")
    public ResponseEntity<UserListDto> getUserList(Authentication authentication, @PathVariable("listId") String listId)
    {

        return ResponseEntity.ok(listService.getSingleList((String) authentication.getPrincipal(), convertStringToUuid(listId)));
    }

    @PostMapping
    public ResponseEntity<String> createNewUserList(Authentication authentication, @RequestBody UserListDto userListDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Created list "+ listService.createUserList((String) authentication.getPrincipal(), userListDto));
    }

    @PostMapping("{listId}")
    public ResponseEntity<String> createNewListItem(Authentication authentication, @PathVariable("listId") String listId, @RequestBody ListItemDto listItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Created item with desc "+ listService.createListItem((String) authentication.getPrincipal(), convertStringToUuid(listId), listItemDto));
    }

    @DeleteMapping("{listId}")
    public ResponseEntity<String> deleteList(Authentication authentication, @PathVariable("listId") String listId)
    {
        UUID uListId = convertStringToUuid(listId);
        listService.deleteList((String)authentication.getPrincipal(), uListId);
        return ResponseEntity.status(HttpStatus.OK).body("List "+uListId+" deleted");
    }

    @DeleteMapping("{listId}/{listItemId}")
    public ResponseEntity<String> deleteListItem(Authentication authentication,
                                                 @PathVariable("listId") String listId,
                                                 @PathVariable("listItemId") String listItemId)
    {
        UUID uListId = convertStringToUuid(listId);
        UUID uListItemId = convertStringToUuid(listItemId);
        listService.deleteListItem((String)authentication.getPrincipal(), uListId, uListItemId);
        return ResponseEntity.status(HttpStatus.OK).body("List "+uListId+"'s item "+uListItemId+" deleted");
    }

    @GetMapping("share/{listId}")
    public ResponseEntity<String> createShareLinkForList(Authentication authentication, @PathVariable("listId") String listId) throws Exception
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(listService.createShareListLink((String) authentication.getPrincipal(), convertStringToUuid(listId)));
    }

    @GetMapping("share/request/{requestId}")
    public ResponseEntity<String> shareUserList(Authentication authentication, @PathVariable("requestId") String requestId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(listService.shareUserList((String) authentication.getPrincipal(), requestId) + " shared with user");
    }

    private UUID convertStringToUuid(String listId)
    {
        try
        {
            return UUID.fromString(listId);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Send a valid id!");
        }
    }
}
