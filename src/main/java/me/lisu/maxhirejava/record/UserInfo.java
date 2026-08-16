package me.lisu.maxhirejava.record;

import me.lisu.maxhirejava.model.User;

public record UserInfo(
        String id,
        String email,
        String name,
        String surname,
        String phone,
        String photo
) {
    public UserInfo(User entity) {
        this(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getSurname(),
                entity.getPhone(),
                entity.getPhoto()
        );
    }
}