package org.kush.share.api.controller.services;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.controller.dtos.ListItemDto;
import org.kush.share.api.controller.dtos.UserListDto;
import org.kush.share.api.database.models.User;
import org.kush.share.api.database.models.UserList;
import org.kush.share.api.database.repository.ListRepository;
import org.kush.share.api.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService
{
    private final ListRepository listRepository;
    private final UserRepository userRepository;

    public List<UserListDto> getList(String userEmail)
    {
        User u =  userRepository.findByEmail(userEmail)
                .orElse(null);

        if (u == null)
            return Collections.emptyList();

        List<UserList> lists = listRepository.findAllListsOfAUserWithItems(u);

        return lists.stream().map(list ->
            new UserListDto(list.getListName(), list.getCreatedBy().getEmail(),
                    list.getItems().stream().map(x -> new ListItemDto(x.getLink(), x.getDescription())).toList()
            )
        ).toList();
    }
}
