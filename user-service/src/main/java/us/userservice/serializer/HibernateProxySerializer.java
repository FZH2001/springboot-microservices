package us.userservice.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class HibernateProxySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object entity, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        System.out.println("serialize");
        if (entity instanceof HibernateProxy) {
            entity = Hibernate.unproxy(entity);
        }
        Long id = null;
        try {
            id = (Long) entity.getClass().getMethod("getId").invoke(entity);
            System.out.println(id);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(e.getMessage());
            gen.writeNull();
            return;
        }
        gen.writeNumber(id);
    }
}