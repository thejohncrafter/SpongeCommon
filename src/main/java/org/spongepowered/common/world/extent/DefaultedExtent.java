/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.world.extent;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Maps;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.TileEntityArchetype;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.DiscreteTransform2;
import org.spongepowered.api.util.DiscreteTransform3;
import org.spongepowered.api.util.PositionOutOfBoundsException;
import org.spongepowered.api.world.extent.ArchetypeVolume;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.extent.ImmutableBiomeArea;
import org.spongepowered.api.world.extent.ImmutableBlockVolume;
import org.spongepowered.api.world.extent.MutableBiomeArea;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.extent.StorageType;
import org.spongepowered.api.world.extent.UnmodifiableBiomeArea;
import org.spongepowered.api.world.extent.UnmodifiableBlockVolume;
import org.spongepowered.api.world.extent.worker.MutableBiomeAreaWorker;
import org.spongepowered.api.world.extent.worker.MutableBlockVolumeWorker;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.util.gen.ByteArrayImmutableBiomeBuffer;
import org.spongepowered.common.util.gen.ByteArrayMutableBiomeBuffer;
import org.spongepowered.common.util.gen.ByteArrayMutableBlockBuffer;
import org.spongepowered.common.util.gen.CharArrayImmutableBlockBuffer;
import org.spongepowered.common.util.gen.CharArrayMutableBlockBuffer;
import org.spongepowered.common.util.gen.IntArrayMutableBlockBuffer;
import org.spongepowered.common.world.extent.worker.SpongeMutableBiomeAreaWorker;
import org.spongepowered.common.world.extent.worker.SpongeMutableBlockVolumeWorker;
import org.spongepowered.common.world.schematic.BimapPalette;
import org.spongepowered.common.world.schematic.SpongeArchetypeVolume;

import java.util.Map;
import java.util.Optional;

/**
 * The Extent interface with extra defaults that are only available in the
 * implementation.
 */
public interface DefaultedExtent extends Extent {

    @Override
    default MutableBiomeArea getBiomeView(Vector2i newMin, Vector2i newMax) {
        if (!containsBiome(newMin.getX(), newMin.getY())) {
            throw new PositionOutOfBoundsException(newMin, getBiomeMin(), getBiomeMax());
        }
        if (!containsBiome(newMax.getX(), newMax.getY())) {
            throw new PositionOutOfBoundsException(newMax, getBiomeMin(), getBiomeMax());
        }
        return new MutableBiomeViewDownsize(this, newMin, newMax);
    }

    @Override
    default MutableBiomeArea getBiomeView(DiscreteTransform2 transform) {
        return new MutableBiomeViewTransform(this, transform);
    }

    @Override
    default UnmodifiableBiomeArea getUnmodifiableBiomeView() {
        return new UnmodifiableBiomeAreaWrapper(this);
    }

    @Override
    default MutableBiomeArea getBiomeCopy(StorageType type) {
        switch (type) {
            case STANDARD:
                return new ByteArrayMutableBiomeBuffer(ExtentBufferUtil.copyToArray(this, getBiomeMin(), getBiomeMax(), getBiomeSize()),
                        getBiomeMin(), getBiomeSize());
            case THREAD_SAFE:
            default:
                throw new UnsupportedOperationException(type.name());
        }
    }

    @Override
    default ImmutableBiomeArea getImmutableBiomeCopy() {
        return ByteArrayImmutableBiomeBuffer.newWithoutArrayClone(ExtentBufferUtil.copyToArray(this, getBiomeMin(), getBiomeMax(), getBiomeSize()),
                getBiomeMin(), getBiomeSize());
    }

    @Override
    default MutableBlockVolume getBlockView(Vector3i newMin, Vector3i newMax) {
        if (!containsBlock(newMin.getX(), newMin.getY(), newMin.getZ())) {
            throw new PositionOutOfBoundsException(newMin, getBlockMin(), getBlockMax());
        }
        if (!containsBlock(newMax.getX(), newMax.getY(), newMax.getZ())) {
            throw new PositionOutOfBoundsException(newMax, getBlockMin(), getBlockMax());
        }
        return new MutableBlockViewDownsize(this, newMin, newMax);
    }

    @Override
    default MutableBlockVolume getBlockView(DiscreteTransform3 transform) {
        return new MutableBlockViewTransform(this, transform);
    }

    @Override
    default UnmodifiableBlockVolume getUnmodifiableBlockView() {
        return new UnmodifiableBlockVolumeWrapper(this);
    }

    @Override
    default MutableBlockVolume getBlockCopy(StorageType type) {
        switch (type) {
            case STANDARD:
                return new CharArrayMutableBlockBuffer(ExtentBufferUtil.copyToArray(this, getBlockMin(), getBlockMax(), getBlockSize()),
                        getBlockMin(), getBlockSize());
            case THREAD_SAFE:
            default:
                throw new UnsupportedOperationException(type.name());
        }
    }

    @Override
    default ImmutableBlockVolume getImmutableBlockCopy() {
        return CharArrayImmutableBlockBuffer.newWithoutArrayClone(ExtentBufferUtil.copyToArray(this, getBlockMin(), getBlockMax(), getBlockSize()),
                getBlockMin(), getBlockSize());
    }

    @Override
    default MutableBiomeAreaWorker<? extends Extent> getBiomeWorker() {
        return new SpongeMutableBiomeAreaWorker<>(this);
    }

    @Override
    default MutableBlockVolumeWorker<? extends Extent> getBlockWorker(Cause cause) {
        return new SpongeMutableBlockVolumeWorker<>(this, cause);
    }

    @Override
    default ArchetypeVolume createArchetypeVolume(Vector3i min, Vector3i max, Vector3i origin) {
        Vector3i tmin = min.min(max);
        Vector3i tmax = max.max(min);
        min = tmin;
        max = tmax;
        Extent area = getExtentView(min, max);
        BimapPalette palette = new BimapPalette();
        area.getBlockWorker(SpongeImpl.getImplementationCause()).iterate((v, x, y, z) -> {
            palette.getOrAssign(v.getBlock(x, y, z));
        });
        int ox = origin.getX();
        int oy = origin.getY();
        int oz = origin.getZ();
        final MutableBlockVolume backing;
        if (palette.getHighestId() <= 0xFF) {
            backing = new ByteArrayMutableBlockBuffer(palette, min.sub(origin), max.sub(min).add(1, 1, 1));
        } else if (palette.getHighestId() <= 0xFFFF) {
            backing = new CharArrayMutableBlockBuffer(palette, min.sub(origin), max.sub(min).add(1, 1, 1));
        } else {
            backing = new IntArrayMutableBlockBuffer(palette, min.sub(origin), max.sub(min).add(1, 1, 1));
        }
        Map<Vector3i, TileEntityArchetype> tiles = Maps.newHashMap();
        area.getBlockWorker(SpongeImpl.getImplementationCause()).iterate((extent, x, y, z) -> {
            BlockState state = extent.getBlock(x, y, z);
            backing.setBlock(x - ox, y - oy, z - oz, state, SpongeImpl.getImplementationCause());
            Optional<TileEntity> tile = extent.getTileEntity(x, y, z);
            if (tile.isPresent()) {
                tiles.put(new Vector3i(x - ox, y - oy, z - oz), tile.get().createArchetype());
            }
        });
        SpongeArchetypeVolume volume = new SpongeArchetypeVolume(backing, tiles);
        return volume;
    }

}
