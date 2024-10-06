package dormease.dormeasedev.domain.school_settings.standard_setting.mapper;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.CreateStandardSettingReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.ModifyStandardSettingReq;
import dormease.dormeasedev.infrastructure.mapstruct.JsonNullableMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StandardSettingMapper extends JsonNullableMapper {

    @InheritConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStandardSettingFromDto(ModifyStandardSettingReq modifyStandardSettingReq, @MappingTarget StandardSetting standardSetting);

    StandardSetting createStandardSettingReqToStandardSetting(School school, CreateStandardSettingReq createStandardSettingReq);
}
