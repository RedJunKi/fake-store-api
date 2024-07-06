package com.project.fake_store_api.domain.user.embeded_class;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameDto {
    private String firstName;
    private String lastName;
}
