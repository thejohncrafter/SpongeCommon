package org.spongepowered.common.data.manipulator.generation;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;
import java.util.Set;

public class AbstractImmutableDataManipulator<I extends ImmutableDataManipulator<I, M>, M extends DataManipulator<M, I>> implements ImmutableDataManipulator<I, M> {

    @Override
    public M asMutable() {
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
    public Set<Key<?>> getKeys() {
        return null;
    }

    @Override
    public Set<ImmutableValue<?>> getValues() {
        return null;
    }
}
