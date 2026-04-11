package org.kush.share.api.controller.services;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.ListItemDto;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.database.models.ListItem;
import org.kush.share.api.database.models.ShareRequest;
import org.kush.share.api.database.models.ShareRequestStatus;
import org.kush.share.api.database.models.UserList;
import org.kush.share.api.database.repository.ItemsRepository;
import org.kush.share.api.database.repository.ListRepository;
import org.kush.share.api.database.repository.ShareRequestRepository;
import org.kush.share.api.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListService
{
    private final ListRepository listRepository;
    private final UserService userService;
    private final ItemsRepository itemsRepository;
    private final ShareRequestRepository shareRequestRepository;

    public List<UserListDto> getList(String userId)
    {
        String username = userService.getUsernameForUuid(UUID.fromString(userId));

        if (StringUtils.isEmpty(username))
        {
            return Collections.emptyList();
        }

        List<UserList> lists = listRepository.findAllListsOfAUserWithItems(UUID.fromString(userId));

        return lists.stream().map(this::convertUserListToDto).toList();
    }

    public UserListDto getSingleList(String userId, UUID listId)
    {
        String username = userService.getUsernameForUuid(UUID.fromString(userId));

        if (StringUtils.isEmpty(username))
        {
            return null;
        }

        UserList list = listRepository.findListOfUserWithListId(UUID.fromString(userId), listId).orElseThrow(() -> new IllegalArgumentException("list not found"));

        return convertUserListToDto(list);
    }

    public String createUserList(String userId, UserListDto userListDto) {
        List<UserList> lists = listRepository.findAllListsOfAUserWithItems(UUID.fromString(userId));

        if (lists.stream().map(list -> list.getListName().toLowerCase())
                .anyMatch(listName -> listName.equalsIgnoreCase(userListDto.listName())))
        {
            throw new IllegalArgumentException("List name already used!");
        }
        UserList userList = new UserList();
        userList.setCreatedBy(UUID.fromString(userId));
        userList.setListName(userListDto.listName());

        listRepository.save(userList);

        itemsRepository.saveAll(userListDto.listItems()
                .stream()
                .map(listItemDto -> new ListItem(null, listItemDto.link(), listItemDto.description(), userList)).toList());

        return userList.getListName();
    }

    public String createListItem(String userId, UUID listId, ListItemDto listItemDto) {
        List<UserList> lists = listRepository.findAllListsOfAUserWithItems(UUID.fromString(userId));

        UserList list = lists.stream().filter(userList -> userList.getId()
                .equals(listId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("List not found!"));

        ListItem listItem = new ListItem(null, listItemDto.link(), listItemDto.description(), list);

        itemsRepository.save(listItem);

        return listItem.getDescription();
    }

    public String createShareListLink(String userId, UUID listId) throws Exception
    {
        UserList list = listRepository.findListOfUserWithListId(UUID.fromString(userId), listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found!"));

        if (!list.getCreatedBy().equals(UUID.fromString(userId)))
        {
            throw new Exception("List not created by you!");
        }

        ShareRequest shareRequest = ShareRequest.builder()
                .sharedList(list)
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()))
                .build();

        shareRequest = shareRequestRepository.save(shareRequest);

        return shareRequest.getShareId().toString();
    }

    private UserListDto convertUserListToDto(UserList userList)
    {
        return new UserListDto(userList.getId().toString(), userList.getListName(),  userService.getUsernameForUuid(userList.getCreatedBy()),
                userList.getItems().stream().map(x -> new ListItemDto(x.getLink(), x.getDescription(), x.getId().toString())).toList()
        );
    }

    public void deleteList(String userId, UUID listId) {
        UserList matchedList = getMatchedList(userId, listId);
        UUID userUuid = UUID.fromString(userId);

        if (matchedList.getCreatedBy().equals(userUuid))
        {
            listRepository.delete(matchedList);
            return;
        }

        Set<UUID> sharedWith = matchedList.getSharedWith();
        if (sharedWith == null || !sharedWith.contains(userUuid))
        {
            throw new IllegalArgumentException("List is not shared with you!");
        }

        sharedWith.remove(userUuid);
        listRepository.save(matchedList);
    }

    public void deleteListItem(String userId, UUID listId, UUID listItemId) {
        UserList matchedList = getMatchedList(userId, listId);

        ListItem itemToDelete = matchedList
                .getItems().stream()
                .filter(x -> x.getId().equals(listItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in list "+matchedList.getListName()));
        matchedList.getItems().remove(itemToDelete);
        listRepository.save(matchedList);
    }

    private UserList getMatchedList(String userId, UUID listId) {
        List<UserList> lists = listRepository.findAllListsOfAUser(UUID.fromString(userId));

        return lists.stream().filter(list -> list.getId().equals(listId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("list not found!"));
    }

    @Transactional
    public String shareUserList(String userId, String requestId)
    {
        ShareRequest request = shareRequestRepository.findById(UUID.fromString(requestId))
                .orElseThrow(() -> new IllegalArgumentException("Request not found, it could be expired or used!"));

        UserList listToShare = request.getSharedList();

        if (listToShare.getCreatedBy().equals(UUID.fromString(userId)))
        {
            throw new IllegalArgumentException("Cannot share a list created by yourself");
        }

        if (request.getRequestStatus() == ShareRequestStatus.USED)
        {
            throw new IllegalArgumentException("Link is already used!");
        }

        listToShare.getSharedWith().add(UUID.fromString(userId));
        listRepository.save(listToShare);

        request.setRequestStatus(ShareRequestStatus.USED);
        shareRequestRepository.save(request);
        return listToShare.getListName();
    }
}
