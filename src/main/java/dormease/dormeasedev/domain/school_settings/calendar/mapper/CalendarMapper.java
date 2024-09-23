package dormease.dormeasedev.domain.school_settings.calendar.mapper;

import dormease.dormeasedev.domain.school_settings.calendar.domain.Calendar;
import dormease.dormeasedev.domain.school_settings.calendar.dto.request.UpdateCalendarReq;
import dormease.dormeasedev.infrastructure.mapstruct.JsonNullableMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalendarMapper extends JsonNullableMapper {

    @InheritConfiguration
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCalender(UpdateCalendarReq updateCalendarReq, @MappingTarget Calendar calendar);
}
