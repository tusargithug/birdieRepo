package net.thrymr.services.impl;

import graphql.language.StringValue;
import graphql.schema.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateScalar implements Coercing<LocalDate,String > {

//    public DateScalar() {
//
//
//        super ("LocalDate", "Local Date Type", new Coercing<LocalDate, String>() {
//            @Override
//            public String serialize(@org.jetbrains.annotations.NotNull Object dataFetcherResult) throws CoercingSerializeException {
//                return null;
//            }
//
//            @Override
//            public @org.jetbrains.annotations.NotNull LocalDate parseValue(@org.jetbrains.annotations.NotNull Object input) throws CoercingParseValueException {
//                return null;
//            }
//
//            @Override
//            public @org.jetbrains.annotations.NotNull LocalDate parseLiteral(@org.jetbrains.annotations.NotNull Object input) throws CoercingParseLiteralException {
//                return null;
//            }
//        });
//    }

    @Override
    public String serialize(@NotNull Object result) throws CoercingSerializeException {
        if (result instanceof LocalDate) {
            return ((LocalDate) result).toString();
        }
        return null;
    }
    @Override
    public @NotNull LocalDate parseValue(@NotNull Object input) throws CoercingParseValueException {
            if(input instanceof String){
                return LocalDate.parse((String) input);
            }

        return null;
    }

    @Override
    public @NotNull LocalDate parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
        if(!(input instanceof StringValue)) return null;


        return LocalDate.parse((String) input);
    }

//    public static final GraphQLScalarType EMAIL = new GraphQLScalarType("LocalDate", "A custom scalar that handles emails", new Coercing<LocalDate,String>() {
//        @Override
//        public String serialize(Object result) throws CoercingParseLiteralException {
//if(result instanceof LocalDate){
//    return ((LocalDate) result).toString();
//}
//            return null;
//        }
//
//        @Override
//        public @NotNull LocalDate parseValue(Object input) throws CoercingParseLiteralException{
//            return null;
//        }
//
//        @Override
//        public @NotNull LocalDate parseLiteral(Object input) throws CoercingParseLiteralException{
//            return null;
//        }
//    });
}
