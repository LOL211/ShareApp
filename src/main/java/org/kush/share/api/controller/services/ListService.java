package org.kush.share.api.controller.services;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.ListItemDto;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.database.models.UserList;
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

    public List<UserListDto> getList(String uuid)
    {
        String username = userService.getUsernameForUuid(UUID.fromString(uuid));

        if (StringUtils.isEmpty(username))
        {
            return Collections.emptyList();
        }

        List<UserList> lists = listRepository.findAllListsOfAUserWithItems(UUID.fromString(uuid));

        return lists.stream().map(list ->
            new UserListDto(list.getListName(),  userService.getUsernameForUuid(list.getCreatedBy()),
                    list.getItems().stream().map(x -> new ListItemDto(x.getLink(), x.getDescription())).toList()
            )
        ).toList();
    }
}
