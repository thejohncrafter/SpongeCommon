package org.spongepowered.common.data.manipulator.generation;

import org.spongepowered.api.data.value.BaseValue;

@FunctionalInterface
public interface ValueGenerationStrategy<E, T extends BaseValue<E>> {

    T generate(E value);

}
