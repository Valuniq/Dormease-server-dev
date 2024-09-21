package dormease.dormeasedev.domain.school_settings.region.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_region_id")
    private Region parentRegion;

    private String fullName;

    private String singleName;

    @Builder
    public Region(Region parentRegion, String fullName, String singleName) {
        this.parentRegion = parentRegion;
        this.fullName = fullName;
        this.singleName = singleName;
    }
}
