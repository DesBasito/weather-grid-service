package kg.manurov.weathergridservice.mapper;

import kg.manurov.weathergridservice.dto.FieldDto;
import kg.manurov.weathergridservice.entities.Field;
import kg.manurov.weathergridservice.enums.CropType;
import kg.manurov.weathergridservice.enums.EnumInterface;
import kg.manurov.weathergridservice.enums.IrrigationType;
import kg.manurov.weathergridservice.util.GeometryHelper;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",imports = {EnumInterface.class, CropType.class, IrrigationType.class, GeometryHelper.class, Point.class})
public interface FieldMapper {
    @Mapping(target = "latitude", expression = "java(field.getGeometry().getY())")
    @Mapping(target = "longitude", expression = "java(field.getGeometry().getX())")
    @Mapping(target = "irrigationType", expression = "java(EnumInterface.fromString(IrrigationType.class,field.getIrrigationType()))")
    @Mapping(target = "cropType", expression = "java(EnumInterface.fromString(CropType.class,field.getCropType()))")
    FieldDto toDto(Field field);

    @Mapping(target = "geometry", expression = "java(GeometryHelper.createPoint(fieldDto.getLatitude(),fieldDto.getLongitude()))")
    @Mapping(target = "irrigationType", expression = "java(EnumInterface.getType(IrrigationType.class,fieldDto.getIrrigationType()))")
    @Mapping(target = "cropType", expression = "java(EnumInterface.getType(CropType.class,fieldDto.getCropType()))")
    Field toEntity(FieldDto fieldDto);
}
