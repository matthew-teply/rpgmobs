package com.conanthecivilian.rpgmobs.manager.LoreManager.template;

import java.util.Optional;

public record LoreCharacter(
    String name,
    int dateBirth,
    Optional<Integer> dateDeath
) {
    public int getAge(LoreWorld world) {
        return Math.abs(this.dateBirth - world.age());
    }

    public int getLifespan(LoreWorld world) {
        return this.dateDeath.map(integer -> integer - dateBirth).orElseGet(() -> this.getAge(world));
    }

    public boolean isDead() {
        return this.dateDeath.isPresent();
    }
}
