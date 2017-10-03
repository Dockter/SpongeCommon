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
package org.spongepowered.common.event.tracking.phase.general;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.common.event.tracking.PhaseTracker;
import org.spongepowered.common.event.tracking.PhaseData;
import org.spongepowered.common.event.tracking.TrackingUtil;
import org.spongepowered.common.event.tracking.capture.CaptureBlockPos;

import javax.annotation.Nullable;
import java.util.Optional;

public final class ExplosionContext extends GeneralPhaseContext<ExplosionContext> {

    private Explosion explosion;
    private CaptureBlockPos captureBlockPos = new CaptureBlockPos();

    public ExplosionContext() {
        super(GeneralPhase.State.EXPLOSION);
    }

    public ExplosionContext populateFromCurrentState() {
        final PhaseData currentPhaseData = PhaseTracker.getInstance().getCurrentPhaseData();
        currentPhaseData.state.getPhase().appendContextPreExplosion(this, currentPhaseData);
        return this;
    }

    public ExplosionContext potentialExplosionSource(WorldServer worldServer, @Nullable Entity entityIn) {
        if (entityIn != null) {
            this.source(entityIn);
        } else {
            this.source(worldServer);
        }
        return this;
    }
    
    public ExplosionContext explosion(Explosion explosion) {
        this.explosion = explosion;
        return this;
    }
    
    public Explosion getExplosion() {
        return this.explosion;
    }

    public org.spongepowered.api.world.explosion.Explosion getSpongeExplosion() {
        return (org.spongepowered.api.world.explosion.Explosion) this.explosion;
    }

    public CaptureBlockPos getCaptureBlockPos() throws IllegalStateException {
        if (this.captureBlockPos == null) {
            throw TrackingUtil.throwWithContext("Intended to capture a block position!", this).get();
        }
        return this.captureBlockPos;
    }

    public Optional<BlockPos> getBlockPosition() {
        return getCaptureBlockPos()
                .getPos();
    }

    @Override
    public PrettyPrinter printCustom(PrettyPrinter printer) {
        return super.printCustom(printer)
                .add("    - %s: %s", "CapturedBlockPosition", this.captureBlockPos)
                .add("    - %s: %s", "Explosion", this.explosion);
    }
}
