package com.github.chevyself.babel.lookup;

import static sun.security.pkcs.PKCS8Key.version;

import com.github.chevyself.babel.util.Versions;
import com.github.chevyself.reflect.util.ReflectUtil;
import com.github.chevyself.reflect.wrappers.WrappedClass;
import com.github.chevyself.reflect.wrappers.WrappedField;
import com.github.chevyself.reflect.wrappers.WrappedMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public abstract class LookUp<O, T>  {

  @NonNull
  protected final TreeMap<Versions.BukkitVersion, String> nameMap = new TreeMap<>();
  @NonNull
  protected final WrappedClass<?> clazz;
  @Nullable
  protected final Class<O> type;
  protected boolean exact = false;
  protected Class<?>[] params = new Class<?>[0];


  protected LookUp(@NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
    this.clazz = clazz;
    this.type = type;
  }

  @NonNull
  public static <O> LookUp<O, WrappedField<O>> fieldOn(@NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
    return new FieldLookUp<>(clazz, null);
  }

  @NonNull
  public static <O> LookUp<O, WrappedField<O>> fieldOn(@NonNull WrappedClass<?> clazz) {
    return new FieldLookUp<>(clazz, null);
  }

  @NonNull
  public static <O> LookUp<O, WrappedMethod<O>> methodOn(@NonNull WrappedClass<?> clazz, @Nullable Class<O> returnType) {
    return new MethodLookUp<>(clazz, returnType);
  }

  @NonNull
  public static <O> LookUp<O, WrappedMethod<O>> methodOn(@NonNull WrappedClass<?> clazz) {
    return new MethodLookUp<>(clazz, null);
  }

  @NonNull
  public LookUp<O, T> findExact(boolean exact) {
    this.exact = exact;
    return this;
  }

  @NonNull
  public LookUp<O, T> usingParams(Class<?>... params) {
    this.params = params;
    return this;
  }

  @NonNull
  public LookUp<O, T> since(int major, int minor, @NonNull String name) {
    this.nameMap.put(new Versions.BukkitVersion(major, minor), name);
    return this;
  }

  @NonNull
  public LookUp<O, T> since(int major, @NonNull String name) {
    return this.since(major, 0, name);
  }

  @NonNull
  protected String getName() {
    return this.getName(Versions.getBukkit());
  }

  @NonNull
  public String getName(Versions.BukkitVersion bukkit) {
    return ReflectUtil.nonNull(this.nameMap.floorEntry(bukkit), new NullPointerException("Could not match a name for " + this + " in " + bukkit)).getValue();
  }



  @NonNull
  public abstract T find();

  private static class FieldLookUp<O> extends LookUp<O, WrappedField<O>> {

    public FieldLookUp(@NonNull WrappedClass<?> clazz, @Nullable Class<O> type) {
      super(clazz, null);
    }

    @Override
    public @NonNull WrappedField<O> find() {
      return this.clazz.getField(this.type, this.getName(), this.exact);
    }
  }

  private static class MethodLookUp<O> extends LookUp<O, WrappedMethod<O>> {

    public MethodLookUp(WrappedClass<?> clazz, @Nullable Class<O> type) {
      super(clazz, type);
    }

    @Override
    public @NonNull WrappedMethod<O> find() {
      return this.clazz.getMethod(this.type, this.getName(), this.exact, this.params);
    }
  }
}
