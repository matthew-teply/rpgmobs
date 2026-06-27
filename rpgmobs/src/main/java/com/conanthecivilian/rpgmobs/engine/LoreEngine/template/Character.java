package com.conanthecivilian.rpgmobs.engine.LoreEngine.template;

import java.util.Optional;

public record Character(
    String name,
    int dateBirth,
    Optional<Integer> dateDeath,
    CharacterTrait[] traits
) {
    public int getAge(World world) {
        return Math.abs(this.dateBirth - world.age());
    }

    public int getLifespan(World world) {
        return this.dateDeath.map(integer -> integer - dateBirth).orElseGet(() -> this.getAge(world));
    }

    public boolean isDead() {
        return this.dateDeath.isPresent();
    }
}
