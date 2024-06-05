package com.hrs.shared.helper;

import java.lang.reflect.Field;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class GenericPatcher<T> {

  @SneakyThrows
  public void patch(T existingObject, T updateObject) {
    Class<?> existingClass = existingObject.getClass();
    Field[] fields = existingClass.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      // Check for non-null values in the update object before patching
      Object updateValue = field.get(updateObject);
      if (updateValue != null) {
        field.set(existingObject, updateValue);
      }
      field.setAccessible(false);
    }
  }
}
