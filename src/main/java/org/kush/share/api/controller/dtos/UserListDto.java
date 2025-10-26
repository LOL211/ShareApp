package org.kush.share.api.controller.dtos;

import java.util.List;

public record UserListDto(
        String listName,
        String createdBy,
        List<ListItemDto> listItems
) {}