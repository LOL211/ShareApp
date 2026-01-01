package org.kush.share.api;

import lombok.RequiredArgsConstructor;
import org.kush.share.api.database.models.ListItem;
import org.kush.share.api.database.models.UserList;
import org.kush.share.api.database.repository.ItemsRepository;
import org.kush.share.api.database.repository.ListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("local")
public class TestRunner implements CommandLineRunner
{
    private final ListRepository listRepository;
    private final ItemsRepository itemsRepository;

    @Override
    public void run(String... args)
    {
        UUID userId = UUID.fromString("a5e8c0c9-1213-4223-8c9a-283cb9143862");
        UserList userList = new UserList();
        userList.setCreatedBy(userId);
        userList.setListName("my new list");
        userList = listRepository.save(userList);

        ListItem listItem = new ListItem();

        listItem.setUserList(userList);
        listItem.setLink("https://www.google.com");
        listItem.setDescription("this is a test");
        listItem = itemsRepository.save(listItem);

        listItem = new ListItem();
        listItem.setUserList(userList);
        listItem.setLink("https://www.google.com");
        listItem.setDescription("this is a test2");
        listItem = itemsRepository.save(listItem);

        userList = new UserList();
        userList.setCreatedBy(userId);
        userList.setListName("my 2nd list");
        userList = listRepository.save(userList);

        listItem = new ListItem();
        listItem.setUserList(userList);
        listItem.setLink("https://www.google.com");
        listItem.setDescription("this is a test");
        listItem = itemsRepository.save(listItem);
    }
}
