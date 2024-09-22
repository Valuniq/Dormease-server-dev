package dormease.dormeasedev.domain.school_settings.standard_setting.mapper;

import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.ModifyStandardSettingReq;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StandardSettingMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStandardSettingFromDto(ModifyStandardSettingReq modifyStandardSettingReq, @MappingTarget StandardSetting standardSetting);
}
