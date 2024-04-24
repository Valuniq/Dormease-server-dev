package dormease.dormeasedev.domain.blacklist.domain.repository;

import dormease.dormeasedev.domain.blacklist.domain.BlackList;
import dormease.dormeasedev.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    BlackList findByUser(User user);
}
