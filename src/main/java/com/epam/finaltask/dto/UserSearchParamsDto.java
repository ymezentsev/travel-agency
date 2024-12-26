package com.epam.finaltask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchParamsDto {
    private String[] usernames;
    private String[] roles;
    private String[] phoneNumbers;
    private String[] emails;
    private String isUnlocked;
}
