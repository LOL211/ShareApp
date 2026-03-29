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

    public UserListDto getSingleList(String userId, String listName)
    {
        String username = userService.getUsernameForUuid(UUID.fromString(userId));

        if (StringUtils.isEmpty(username))
        {
            return null;
        }

        UserList list = listRepository.findListOfUserWithListName(UUID.fromString(userId), listName).orElseThrow(() -> new IllegalArgumentException("list not found"));

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

    public String createListItem(String userId, String listName, ListItemDto listItemDto) {
        List<UserList> lists = listRepository.findAllListsOfAUserWithItems(UUID.fromString(userId));

        UserList list = lists.stream().filter(userList -> userList.getListName()
                .equalsIgnoreCase(listName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("List not found!"));

        ListItem listItem = new ListItem(null, listItemDto.link(), listItemDto.description(), list);

        itemsRepository.save(listItem);

        return listItem.getDescription();
    }

    public String createShareListLink(String userId, String listName) throws Exception
    {
        UserList list = listRepository.findListOfUserWithListName(UUID.fromString(userId), listName)
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
        return new UserListDto(userList.getListName(),  userService.getUsernameForUuid(userList.getCreatedBy()),
                userList.getItems().stream().map(x -> new ListItemDto(x.getLink(), x.getDescription())).toList()
        );
    }

    public void deleteList(String userId, String listName) {
        UserList matchedList = getMatchedList(userId, listName);

        if (!matchedList.getCreatedBy().equals(UUID.fromString(userId)))
        {
            throw new IllegalArgumentException("Cannot delete a list shared by someone else!");
        }

        listRepository.delete(matchedList);
    }

    public void deleteListItem(String userId, String listName, String listItem) {
        UserList matchedList = getMatchedList(userId, listName);

        ListItem itemToDelete = matchedList
                .getItems().stream()
                .filter(x -> x.getDescription().equals(listItem))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Item "+listItem+" not found in list "+listName));
        matchedList.getItems().remove(itemToDelete);
        listRepository.save(matchedList);
    }

    private UserList getMatchedList(String userId, String listName) {
        List<UserList> lists = listRepository.findAllListsOfAUser(UUID.fromString(userId));

        return lists.stream().filter(list -> list.getListName().equalsIgnoreCase(listName))
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
        listToShare.getSharedWith().add(UUID.fromString(userId));
        listRepository.save(listToShare);

        request.setRequestStatus(ShareRequestStatus.USED);
        shareRequestRepository.save(request);
        return listToShare.getListName();
    }
}
