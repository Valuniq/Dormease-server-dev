package dormease.dormeasedev.domain.users.admin.domain;

import dormease.dormeasedev.domain.users.user.domain.User;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Admin extends User {

    private String securityCode;
}
