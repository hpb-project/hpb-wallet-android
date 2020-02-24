package com.zhaoxi.Open_source_Android.web3.datatypes;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * des:
 * Created by ztt on 2018/8/1.
 */
public abstract class TypeReference<T extends com.zhaoxi.Open_source_Android.web3.datatypes.Type>
        implements Comparable<TypeReference<T>> {

    private final Type type;

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public int compareTo(TypeReference<T> o) {
        // taken from the blog post comments - this results in an errror if the
        // type parameter is left out.
        return 0;
    }

    public Type getType() {
        return type;
    }

    /**
     * Workaround to ensure type does not come back as T due to erasure, this enables you to
     * create a TypeReference via {@link Class Class&lt;T&gt;}.
     *
     * @return the parameterized Class type if applicable, otherwise a regular class
     * @throws ClassNotFoundException if the class type cannot be determined
     */
    @SuppressWarnings("unchecked")
    public Class<T> getClassType() throws ClassNotFoundException {
        Type clsType = getType();

        if (getType() instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) clsType).getRawType();
        } else {
            return (Class<T>) Class.forName(((Class) clsType).getName());
        }
    }

    public static <T extends com.zhaoxi.Open_source_Android.web3.datatypes.Type> TypeReference<T> create(
            final Class<T> cls) {
        return new TypeReference<T>() {
            @Override
            public Type getType() {
                return cls;
            }
        };
    }

    public abstract static class StaticArrayTypeReference<T extends com.zhaoxi.Open_source_Android.web3.datatypes.Type>
            extends TypeReference<T> {

        private final int size;

        protected StaticArrayTypeReference(int size) {
            super();
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
}
