package dormease.dormeasedev.domain.users.admin.domain.repository;

import dormease.dormeasedev.domain.users.admin.domain.Admin;
import dormease.dormeasedev.domain.users.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUser(User user);
}
