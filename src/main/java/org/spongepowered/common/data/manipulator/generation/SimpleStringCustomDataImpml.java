package org.spongepowered.common.data.manipulator.generation;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.generator.testing.SimpleStringCustomData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.value.mutable.SpongeValue;

import java.util.Optional;
import java.util.Set;

public class SimpleStringCustomDataImpml implements DataManipulator<SimpleStringCustomDataImpml, ImmutableSimpleStringCustomDataImpl> {

    private String myStringKey;
    private final String defaultString = "DefaultString";
    private final ValueGenerationStrategy<String, Value<String>> myStringKeyValue = (value) -> new SpongeValue<>(SimpleStringCustomData.MY_STRING,
            this.myStringKey, this.defaultString);

    public SimpleStringCustomDataImpml() {
        this("DefaultString");
    }

    public SimpleStringCustomDataImpml(String value) {
        this.myStringKey = value;
    }


    @Override
    public Optional<SimpleStringCustomDataImpml> fill(DataHolder dataHolder, MergeFunction overlap) {
        return null;
    }

    @Override
    public Optional<SimpleStringCustomDataImpml> from(DataContainer container) {
        return null;
    }

    @Override
    public <E> SimpleStringCustomDataImpml set(Key<? extends BaseValue<E>> key, E value) {
        return null;
    }

    @Override
    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        return null;
    }

    @Override
    public <E, V extends BaseValue<E>> Optional<V> getValue(Key<V> key) {
        return null;
    }

    @Override
    public boolean supports(Key<?> key) {
        return false;
    }

    @Override
    public SimpleStringCustomDataImpml copy() {
        return null;
    }

    @Override
    public Set<Key<?>> getKeys() {
        return null;
    }

    @Override
    public Set<ImmutableValue<?>> getValues() {
        return null;
    }

    @Override
    public ImmutableSimpleStringCustomDataImpl asImmutable() {
        return null;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return null;
    }
}
