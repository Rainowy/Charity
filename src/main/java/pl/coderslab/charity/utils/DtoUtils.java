package pl.coderslab.charity.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.lang.reflect.Type;
import java.util.List;

public class DtoUtils {

    public DtoEntity convertToDto(Object obj, DtoEntity mapper) {
        return new ModelMapper().map(obj, mapper.getClass());
    }

    public Object convertToEntity(Object obj, DtoEntity mapper) {
        return new ModelMapper().map(mapper, obj.getClass());
    }

    public <T, P> List<T> convertToDtoList(List<P> obj, Type typeToken, PropertyMap propertyMap) {
        if (propertyMap == null) {
            return new ModelMapper().map(obj, typeToken);
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(propertyMap);
        return modelMapper.map(obj, typeToken);
    }
}