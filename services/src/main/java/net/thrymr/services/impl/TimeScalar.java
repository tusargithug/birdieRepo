//package net.thrymr.services.impl;
//
//import graphql.language.StringValue;
//import graphql.schema.Coercing;
//import graphql.schema.CoercingParseLiteralException;
//import graphql.schema.CoercingParseValueException;
//import graphql.schema.CoercingSerializeException;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//@Component
//public class TimeScalar implements Coercing<LocalTime,String> {
//    @Override
//    public String serialize(@NotNull Object result) throws CoercingSerializeException {
//        if (result instanceof LocalDate) {
//            return ((LocalTime) result).toString();
//        }
//        return null;
//    }
//
//    @Override
//    public @NotNull LocalTime parseValue(@NotNull Object input) throws CoercingParseValueException {
//        if(input instanceof String){
//            return LocalTime.parse((String) input);
//        }
//
//        return null;
//    }
//
//    @Override
//    public @NotNull LocalTime parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
//        if(!(input instanceof StringValue)) return null;
//
//
//        return LocalTime.parse((String) input);
//    }
//}
