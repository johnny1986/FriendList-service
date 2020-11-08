package com.afilias.friendlist.model.dto;

import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private long id;

    private String name;

    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return id == userDTO.id &&
                Objects.equals(name, userDTO.name) &&
                Objects.equals(city, userDTO.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city);
    }
}
