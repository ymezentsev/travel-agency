package com.epam.finaltask.dto;

import lombok.Builder;

@Builder
public record UserSearchParamsDto(String[] usernames, String[] roles,
                                  String[] phoneNumbers, String[] emails,
                                  Boolean isUnlocked) {
}
