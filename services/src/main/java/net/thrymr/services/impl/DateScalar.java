//package net.thrymr.services.impl;
//
//import graphql.language.ScalarTypeDefinition;
//import graphql.language.ScalarTypeExtensionDefinition;
//import graphql.language.StringValue;
//import graphql.language.Value;
//import graphql.schema.*;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Component;
//
//import java.io.UnsupportedEncodingException;
//import java.lang.invoke.MethodHandles;
//import java.nio.charset.Charset;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.stream.IntStream;
//import java.util.stream.Stream;
//
//@Component
//public class DateScalar extends GraphQLScalarType {
////Coercing<LocalDate,String >
//    public DateScalar(String name, String description, Coercing<?, ?> coercing, List<GraphQLDirective> directives, List<GraphQLAppliedDirective> appliedDirectives, ScalarTypeDefinition definition, List<ScalarTypeExtensionDefinition> extensionDefinitions, String specifiedByUrl) {
//
//
//        super ("LocalDate", "Local Date Type", new Coercing<LocalDate, String>() {
//            @Override
//            public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
//                return null;
//            }
//
//            @Override
//            public @NotNull LocalDate parseValue(@NotNull Object input) throws CoercingParseValueException {
//                return null;
//            }
//
//            @Override
//            public @NotNull LocalDate parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
//                return null;
//            }
//
//            @Override
//            public @NotNull Value valueToLiteral(@NotNull Object input) {
//                return null;
//            }
//
//            @Override
//            public @NotNull LocalDate parseLiteral(Object input, Map<String, Object> variables) throws CoercingParseLiteralException {
//                return null;
//            }
//
//            public int length() {
//                return specifiedByUrl.length();
//            }
//
//            public boolean isEmpty() {
//                return specifiedByUrl.isEmpty();
//            }
//
//            public char charAt(int index) {
//                return specifiedByUrl.charAt(index);
//            }
//
//            public int codePointAt(int index) {
//                return specifiedByUrl.codePointAt(index);
//            }
//
//            public int codePointBefore(int index) {
//                return specifiedByUrl.codePointBefore(index);
//            }
//
//            public int codePointCount(int beginIndex, int endIndex) {
//                return specifiedByUrl.codePointCount(beginIndex, endIndex);
//            }
//
//            public int offsetByCodePoints(int index, int codePointOffset) {
//                return specifiedByUrl.offsetByCodePoints(index, codePointOffset);
//            }
//
//            public void getChars(int srcBegin, int srcEnd, @NotNull char[] dst, int dstBegin) {
//                specifiedByUrl.getChars(srcBegin, srcEnd, dst, dstBegin);
//            }
//
//            @Deprecated(since = "1.1")
//            public void getBytes(int srcBegin, int srcEnd, @NotNull byte[] dst, int dstBegin) {
//                specifiedByUrl.getBytes(srcBegin, srcEnd, dst, dstBegin);
//            }
//
//            public byte[] getBytes(@NotNull String charsetName) throws UnsupportedEncodingException {
//                return specifiedByUrl.getBytes(charsetName);
//            }
//
//            public byte[] getBytes(@NotNull Charset charset) {
//                return specifiedByUrl.getBytes(charset);
//            }
//
//            public byte[] getBytes() {
//                return specifiedByUrl.getBytes();
//            }
//
//            public boolean contentEquals(@NotNull StringBuffer sb) {
//                return specifiedByUrl.contentEquals(sb);
//            }
//
//            public boolean contentEquals(@NotNull CharSequence cs) {
//                return specifiedByUrl.contentEquals(cs);
//            }
//
//            public boolean equalsIgnoreCase(String anotherString) {
//                return specifiedByUrl.equalsIgnoreCase(anotherString);
//            }
//
//            public int compareTo(@NotNull String anotherString) {
//                return specifiedByUrl.compareTo(anotherString);
//            }
//
//            public int compareToIgnoreCase(@NotNull String str) {
//                return specifiedByUrl.compareToIgnoreCase(str);
//            }
//
//            public boolean regionMatches(int toffset, @NotNull String other, int ooffset, int len) {
//                return specifiedByUrl.regionMatches(toffset, other, ooffset, len);
//            }
//
//            public boolean regionMatches(boolean ignoreCase, int toffset, @NotNull String other, int ooffset, int len) {
//                return specifiedByUrl.regionMatches(ignoreCase, toffset, other, ooffset, len);
//            }
//
//            public boolean startsWith(@NotNull String prefix, int toffset) {
//                return specifiedByUrl.startsWith(prefix, toffset);
//            }
//
//            public boolean startsWith(@NotNull String prefix) {
//                return specifiedByUrl.startsWith(prefix);
//            }
//
//            public boolean endsWith(@NotNull String suffix) {
//                return specifiedByUrl.endsWith(suffix);
//            }
//
//            public int indexOf(int ch) {
//                return specifiedByUrl.indexOf(ch);
//            }
//
//            public int indexOf(int ch, int fromIndex) {
//                return specifiedByUrl.indexOf(ch, fromIndex);
//            }
//
//            public int lastIndexOf(int ch) {
//                return specifiedByUrl.lastIndexOf(ch);
//            }
//
//            public int lastIndexOf(int ch, int fromIndex) {
//                return specifiedByUrl.lastIndexOf(ch, fromIndex);
//            }
//
//            public int indexOf(@NotNull String str) {
//                return specifiedByUrl.indexOf(str);
//            }
//
//            public int indexOf(@NotNull String str, int fromIndex) {
//                return specifiedByUrl.indexOf(str, fromIndex);
//            }
//
//            public int lastIndexOf(@NotNull String str) {
//                return specifiedByUrl.lastIndexOf(str);
//            }
//
//            public int lastIndexOf(@NotNull String str, int fromIndex) {
//                return specifiedByUrl.lastIndexOf(str, fromIndex);
//            }
//
//            public String substring(int beginIndex) {
//                return specifiedByUrl.substring(beginIndex);
//            }
//
//            public String substring(int beginIndex, int endIndex) {
//                return specifiedByUrl.substring(beginIndex, endIndex);
//            }
//
//            public CharSequence subSequence(int beginIndex, int endIndex) {
//                return specifiedByUrl.subSequence(beginIndex, endIndex);
//            }
//
//            public String concat(@NotNull String str) {
//                return specifiedByUrl.concat(str);
//            }
//
//            public String replace(char oldChar, char newChar) {
//                return specifiedByUrl.replace(oldChar, newChar);
//            }
//
//            public boolean matches(@NotNull String regex) {
//                return specifiedByUrl.matches(regex);
//            }
//
//            public boolean contains(@NotNull CharSequence s) {
//                return specifiedByUrl.contains(s);
//            }
//
//            public String replaceFirst(@NotNull String regex, @NotNull String replacement) {
//                return specifiedByUrl.replaceFirst(regex, replacement);
//            }
//
//            public String replaceAll(@NotNull String regex, @NotNull String replacement) {
//                return specifiedByUrl.replaceAll(regex, replacement);
//            }
//
//            public String replace(@NotNull CharSequence target, @NotNull CharSequence replacement) {
//                return specifiedByUrl.replace(target, replacement);
//            }
//
//            public String[] split(@NotNull String regex, int limit) {
//                return specifiedByUrl.split(regex, limit);
//            }
//
//            public String[] split(@NotNull String regex) {
//                return specifiedByUrl.split(regex);
//            }
//
//            public  String join(CharSequence delimiter, CharSequence... elements) {
//                return String.join(delimiter, elements);
//            }
//
//            public  String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
//                return String.join(delimiter, elements);
//            }
//
//            public String toLowerCase(@NotNull Locale locale) {
//                return specifiedByUrl.toLowerCase(locale);
//            }
//
//            public String toLowerCase() {
//                return specifiedByUrl.toLowerCase();
//            }
//
//            public String toUpperCase(@NotNull Locale locale) {
//                return specifiedByUrl.toUpperCase(locale);
//            }
//
//            public String toUpperCase() {
//                return specifiedByUrl.toUpperCase();
//            }
//
//            public String trim() {
//                return specifiedByUrl.trim();
//            }
//
//            public String strip() {
//                return specifiedByUrl.strip();
//            }
//
//            public String stripLeading() {
//                return specifiedByUrl.stripLeading();
//            }
//
//            public String stripTrailing() {
//                return specifiedByUrl.stripTrailing();
//            }
//
//            public boolean isBlank() {
//                return specifiedByUrl.isBlank();
//            }
//
//            public Stream<String> lines() {
//                return specifiedByUrl.lines();
//            }
//
//            public String indent(int n) {
//                return specifiedByUrl.indent(n);
//            }
//
//            public String stripIndent() {
//                return specifiedByUrl.stripIndent();
//            }
//
//            public String translateEscapes() {
//                return specifiedByUrl.translateEscapes();
//            }
//
//            public <R> R transform(Function<? super String, ? extends R> f) {
//                return specifiedByUrl.transform(f);
//            }
//
//            public IntStream chars() {
//                return specifiedByUrl.chars();
//            }
//
//            public IntStream codePoints() {
//                return specifiedByUrl.codePoints();
//            }
//
//            public char[] toCharArray() {
//                return specifiedByUrl.toCharArray();
//            }
//
//            public  String format(@NotNull String format, Object... args) {
//                return String.format(format, args);
//            }
//
//            public  String format(Locale l, @NotNull String format, Object... args) {
//                return String.format(l, format, args);
//            }
//
//            public String formatted(Object... args) {
//                return specifiedByUrl.formatted(args);
//            }
//
//            public  String valueOf(Object obj) {
//                return String.valueOf(obj);
//            }
//
//            public  String valueOf(@NotNull char[] data) {
//                return String.valueOf(data);
//            }
//
//            public  String valueOf(@NotNull char[] data, int offset, int count) {
//                return String.valueOf(data, offset, count);
//            }
//
//            public  String copyValueOf(@NotNull char[] data, int offset, int count) {
//                return String.copyValueOf(data, offset, count);
//            }
//
//            public  String copyValueOf(@NotNull char[] data) {
//                return String.copyValueOf(data);
//            }
//
//            public  String valueOf(boolean b) {
//                return String.valueOf(b);
//            }
//
//            public  String valueOf(char c) {
//                return String.valueOf(c);
//            }
//
//            public  String valueOf(int i) {
//                return String.valueOf(i);
//            }
//
//            public  String valueOf(long l) {
//                return String.valueOf(l);
//            }
//
//            public  String valueOf(float f) {
//                return String.valueOf(f);
//            }
//
//            public  String valueOf(double d) {
//                return String.valueOf(d);
//            }
//
//            public String intern() {
//                return specifiedByUrl.intern();
//            }
//
//            public String repeat(int count) {
//                return specifiedByUrl.repeat(count);
//            }
//
//            public Optional<String> describeConstable() {
//                return specifiedByUrl.describeConstable();
//            }
//
//            public String resolveConstantDesc(MethodHandles.Lookup lookup) {
//                return specifiedByUrl.resolveConstantDesc(lookup);
//            }
//
//            public  int compare(CharSequence cs1, CharSequence cs2) {
//                return CharSequence.compare(cs1, cs2);
//            }
//        });
//    }
//
////    @Override
////    public String serialize(@NotNull Object result) throws CoercingSerializeException {
////        if (result instanceof LocalDate) {
////            return ((LocalDate) result).toString();
////        }
////        return null;
////    }
////    @Override
////    public @NotNull LocalDate parseValue(@NotNull Object input) throws CoercingParseValueException {
////            if(input instanceof String){
////                return LocalDate.parse((String) input);
////            }
////
////        return null;
////    }
////
////    @Override
////    public @NotNull LocalDate parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
////        if(!(input instanceof StringValue)) return null;
////
////
////        return LocalDate.parse((String) input);
////    }
//
////    public static final GraphQLScalarType EMAIL = new GraphQLScalarType("LocalDate", "A custom scalar that handles emails", new Coercing<LocalDate,String>() {
////        @Override
////        public String serialize(Object result) throws CoercingParseLiteralException {
////if(result instanceof LocalDate){
////    return ((LocalDate) result).toString();
////}
////            return null;
////        }
////
////        @Override
////        public @NotNull LocalDate parseValue(Object input) throws CoercingParseLiteralException{
////            return null;
////        }
////
////        @Override
////        public @NotNull LocalDate parseLiteral(Object input) throws CoercingParseLiteralException{
////            return null;
////        }
////    });
//}
