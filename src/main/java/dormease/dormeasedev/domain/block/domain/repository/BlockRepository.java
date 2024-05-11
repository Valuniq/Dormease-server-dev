package dormease.dormeasedev.domain.block.domain.repository;

import dormease.dormeasedev.domain.block.domain.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
}
