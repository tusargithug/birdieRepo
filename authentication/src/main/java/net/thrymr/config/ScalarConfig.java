package net.thrymr.config;


import graphql.language.StringValue;

import graphql.scalars.ExtendedScalars;
import graphql.schema.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;


@Configuration
public class ScalarConfig {
//
//    @Bean
//    public GraphQLScalarType nonNegativeTime(){
//      return   ExtendedScalars.Time;
//    }
//
//    @Bean
//    public GraphQLScalarType nonDate(){
//        return   ExtendedScalars.Date;
//    }
//



//
//
//        @Bean
//        public GraphQLScalarType dateScalar() {
//            return GraphQLScalarType.newScalar()
//                    .name("Date")
//                    .description("Java 8 LocalDate as scalar.")
//                    .coercing(new Coercing<LocalDate, String>() {
//                        @Override
//                        public String serialize(final Object dataFetcherResult) {
//                            if (dataFetcherResult instanceof LocalDate) {
//                                return dataFetcherResult.toString();
//                            } else {
//                                throw new CoercingSerializeException("Expected a LocalDate object.");
//                            }
//                        }
//
//                        @Override
//                        public LocalDate parseValue(final Object input) {
//                            try {
//                                if (input instanceof String) {
//                                    return LocalDate.parse((String) input);
//                                } else {
//                                    throw new CoercingParseValueException("Expected a String");
//                                }
//                            } catch (DateTimeParseException e) {
//                                throw new CoercingParseValueException(String.format("Not a valid date: '%s'.", input), e
//                                );
//                            }
//                        }
//
//                        @Override
//                        public LocalDate parseLiteral(final Object input) {
//                            if (input instanceof StringValue) {
//                                try {
//                                    return LocalDate.parse(((StringValue) input).getValue());
//                                } catch (DateTimeParseException e) {
//                                    throw new CoercingParseLiteralException(e);
//                                }
//                            } else {
//                                throw new CoercingParseLiteralException("Expected a StringValue.");
//                            }
//                        }
//                    }).build();
//        }
//
//
//
//
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
//
//    @Override
//    public String serialize(final Object input) throws CoercingSerializeException {
//        TemporalAccessor temporalAccessor;
//        if (input instanceof TemporalAccessor) {
//            temporalAccessor = (TemporalAccessor) input;
//        } else if (input instanceof String) {
//            temporalAccessor = parseTime(input.toString(), CoercingSerializeException::new);
//        } else {
//            throw new CoercingSerializeException(
//                    "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
//            );
//        }
//        try {
//            return DATE_FORMATTER.format(temporalAccessor);
//        } catch (DateTimeException e) {
//            throw new CoercingSerializeException(
//                    "Unable to turn TemporalAccessor into full time because of : '" + e.getMessage() + "'."
//            );
//        }
//    }
//
//    @Override
//    public LocalTime parseValue(final Object input) throws CoercingParseValueException {
//        TemporalAccessor temporalAccessor;
//        if (input instanceof TemporalAccessor) {
//            temporalAccessor = (TemporalAccessor) input;
//        } else if (input instanceof String) {
//            temporalAccessor = parseTime(input.toString(), CoercingParseValueException::new);
//        } else {
//            throw new CoercingParseValueException(
//                    "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
//            );
//        }
//        try {
//            return LocalTime.from(temporalAccessor);
//        } catch (DateTimeException e) {
//            throw new CoercingParseValueException(
//                    "Unable to turn TemporalAccessor into full time because of : '" + e.getMessage() + "'."
//            );
//        }
//    }
//
//    @Override
//    public LocalTime parseLiteral(final Object input) throws CoercingParseLiteralException {
//        if (!(input instanceof StringValue)) {
//            throw new CoercingParseLiteralException(
//                    "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
//            );
//        }
//        return parseTime(((StringValue) input).getValue(), CoercingParseLiteralException::new);
//    }
//
//    private static LocalTime parseTime(String s, Function<String, RuntimeException> exceptionMaker) {
//        try {
//            TemporalAccessor temporalAccessor = DATE_FORMATTER.parse(s);
//            return LocalTime.from(temporalAccessor);
//        } catch (DateTimeParseException e) {
//            throw exceptionMaker.apply("Invalid local time value : '" + s + "'. because of : '" + e.getMessage() + "'");
//        }
//    }
//    private static GraphQLScalarType DATE_SCALAR = GraphQLScalarType.newScalar().name("Date").coercing(new Coercing<LocalDate, LocalDate>() {
//        @Override
//        public LocalDate serialize(Object input) {
//            return input == null ? null : LocalDate.parse(input.toString());
//        }
//
//        @Override
//        public LocalDate parseValue(Object input) {
//            return input == null ? null : LocalDate.parse(input.toString());
//        }
//
//        @Override
//        public LocalDate parseLiteral(Object input) {
//            if (input instanceof StringValue) {
//                try {
//                    return LocalDate.parse(((StringValue) input).getValue());
//                } catch (DateTimeParseException e) {
//                    throw new CoercingParseLiteralException(e);
//                }
//            }
//
//            throw new CoercingParseLiteralException();
//        }
//    }).build();



}

