package org.kush.share.api.controller.services;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.ListItemDto;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.database.models.ListItem;
import org.kush.share.api.database.models.UserList;
import org.kush.share.api.database.repository.ItemsRepository;
import org.kush.share.api.database.repository.ListRepository;
import org.kush.share.api.service.UserService;
import org.springframework.stereotype.Service;

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

    public String createUserList(String userId, UserListDto userListDto) throws Exception {
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

    private UserListDto convertUserListToDto(UserList userList)
    {
        return new UserListDto(userList.getListName(),  userService.getUsernameForUuid(userList.getCreatedBy()),
                userList.getItems().stream().map(x -> new ListItemDto(x.getLink(), x.getDescription())).toList()
        );
    }
}
